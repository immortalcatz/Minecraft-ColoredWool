package org.minecraftnauja.coloredwool.menu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.minecraftnauja.coloredwool.block.ContainerFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityPictureFactory;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player,
			final World world, final int x, final int y, final int z) {
		final TileEntity e = world.getBlockTileEntity(x, y, z);
		if (e == null) {
			return null;
		}
		switch (Gui.values()[ID]) {
			case PictureFactoryFurnace :
				return new ContainerFactory(player.inventory,
						(TileEntityPictureFactory) e);
			case ModelFactoryFurnace :
				return new ContainerFactory(player.inventory,
						(TileEntityModelFactory) e);
			default :
				return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player,
			final World world, final int x, final int y, final int z) {
		final TileEntity e = world.getBlockTileEntity(x, y, z);
		if (e == null) {
			return null;
		}
		switch (Gui.values()[ID]) {
			case PictureFactoryFurnace :
				return new GuiPictureFactoryFurnace(player.inventory,
						(TileEntityPictureFactory) e);
			case ModelFactoryFurnace :
				return new GuiModelFactoryFurnace(player.inventory,
						(TileEntityModelFactory) e);
			default :
				return null;
		}
	}

}
