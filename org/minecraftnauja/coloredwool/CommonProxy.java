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
	public void openColoredWoolMenu(final EntityPlayer player,
			final TileEntityColoredWool tileEntity, final Color color) {
	}

	/**
	 * Opens the picture factory menu for the image.
	 * 
	 * @param player
	 *            player.
	 * @param tileEntity
	 *            tile entity.
	 */
	public void openPictureFactoryImage(final EntityPlayer player,
			final TileEntityPictureFactory tileEntity) {
	}

	/**
	 * Opens the model factory menu for the image.
	 * 
	 * @param player
	 *            player.
	 * @param tileEntity
	 *            tile entity.
	 */
	public void openModelFactoryMenu(final EntityPlayer player,
			final TileEntityModelFactory tileEntity) {
	}

}
