package org.minecraftnauja.coloredwool;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;

public class ImageImport {

	public boolean importFinished;
	private EntityPlayer player;
	private BufferedImage image;
	private World worldObj;
	private Orientation xOrient;
	private Orientation yOrient;
	private int xCurrent;
	private int yCurrent;
	private int originX;
	private int originY;
	private int originZ;
	private int imageWidth;
	private int imageHeight;
	private int xxa;
	private int xya;
	private int xza;
	private int yxa;
	private int yya;
	private int yza;

	public ImageImport(EntityPlayer player, int x, int y, int z,
			BufferedImage image, Orientation xOrient, Orientation yOrient) {
		importFinished = false;
		this.player = player;
		this.image = image;
		this.xOrient = xOrient;
		this.yOrient = yOrient;
		worldObj = player.worldObj;
		xCurrent = 0;
		yCurrent = 0;
		originX = x;
		originY = y;
		originZ = z;
		imageHeight = image.getHeight();
		imageWidth = image.getWidth();
		xxa = xOrient.getDX();
		xya = xOrient.getDY();
		xza = xOrient.getDZ();
		yxa = yOrient.getDX();
		yya = yOrient.getDY();
		yza = yOrient.getDZ();
	}

	public void imageTick() {
		int xOffset = 0;
		int yOffset = 0;
		int zOffset = 0;
		int aRGB = image.getRGB(xCurrent, yCurrent);
		Color pix = new Color(aRGB, true);

		if (xxa != 0)
			xOffset = xCurrent * xxa;
		else if (xya != 0)
			yOffset = xCurrent * xya;
		else if (xza != 0) {
			zOffset = xCurrent * xza;
		}

		if (yza != 0)
			zOffset = yCurrent * yza;
		else if (yxa != 0)
			xOffset = yCurrent * yxa;
		else if (yya != 0) {
			yOffset = yCurrent * yya;
		}

		if ((originY + yOffset < 0) || (originY + yOffset > 127)) {
			importFinished = true;
			return;
		}

		if (pix.getAlpha() == 255) {
			TileEntity loadTileEntity = worldObj.getBlockTileEntity(originX
					+ xOffset, originY + yOffset, originZ + zOffset);
			if ((loadTileEntity instanceof TileEntityColoredWool)) {
				TileEntityColoredWool currentBlockEntity = (TileEntityColoredWool) loadTileEntity;
				if (currentBlockEntity != null) {
					currentBlockEntity.color = pix.getRGB();
					currentBlockEntity.sendColorToPlayers();
				}
			}
		}
		xCurrent += 1;

		if (xCurrent == imageWidth) {
			xCurrent = 0;
			yCurrent += 1;
			if (yCurrent == imageHeight) {
				yCurrent = 0;
				importFinished = true;
			}
		}
	}

}