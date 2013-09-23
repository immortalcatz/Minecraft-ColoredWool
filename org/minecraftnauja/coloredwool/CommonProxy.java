package org.minecraftnauja.coloredwool;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;

import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityPictureFactory;

/**
 * Proxy server-side.
 */
public class CommonProxy {

	/**
	 * Opens the colored wool menu.
	 * 
	 * @param player
	 *            player.
	 * @param tileEntity
	 *            tile entity.
	 * @param color
	 *            current color.
	 */
	public void openColoredWoolMenu(EntityPlayer player,
			TileEntityColoredWool tileEntity, Color color) {
	}

	/**
	 * Opens the picture factory menu for the image.
	 * 
	 * @param player
	 *            player.
	 * @param tileEntity
	 *            tile entity.
	 */
	public void openPictureFactoryImage(EntityPlayer player,
			TileEntityPictureFactory tileEntity) {
	}

	/**
	 * Opens the model factory menu for the image.
	 * 
	 * @param player
	 *            player.
	 * @param tileEntity
	 *            tile entity.
	 */
	public void openModelFactoryMenu(EntityPlayer player,
			TileEntityModelFactory tileEntity) {
	}

}
