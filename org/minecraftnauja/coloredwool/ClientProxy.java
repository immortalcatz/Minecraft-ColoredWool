package org.minecraftnauja.coloredwool;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;

import org.minecraftnauja.coloredwool.menu.GuiColoredWoolMenu;
import org.minecraftnauja.coloredwool.menu.GuiModelFactoryMenu;
import org.minecraftnauja.coloredwool.menu.GuiPictureFactoryImage;
import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityPictureFactory;

/**
 * Proxy client-side.
 */
public class ClientProxy extends CommonProxy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openColoredWoolMenu(final EntityPlayer player,
			final TileEntityColoredWool tileEntity, final Color color) {
		ModLoader.openGUI(player, new GuiColoredWoolMenu(player, tileEntity,
				color));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openPictureFactoryImage(final EntityPlayer player,
			final TileEntityPictureFactory tileEntity) {
		ModLoader.openGUI(player, new GuiPictureFactoryImage(tileEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openModelFactoryMenu(final EntityPlayer player,
			final TileEntityModelFactory tileEntity) {
		ModLoader.openGUI(player, new GuiModelFactoryMenu(player, tileEntity));
	}

}
