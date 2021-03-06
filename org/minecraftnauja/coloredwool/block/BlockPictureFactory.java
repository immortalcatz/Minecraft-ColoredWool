package org.minecraftnauja.coloredwool.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.menu.Gui;
import org.minecraftnauja.coloredwool.tileentity.TileEntityFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityPictureFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Picture factory block.
 */
public class BlockPictureFactory extends BlockFactory {

	/**
	 * Initializing constructor.
	 * 
	 * @param par1
	 *            block identifier.
	 * @param state
	 *            its state.
	 */
	public BlockPictureFactory(final int par1, final FactoryState state) {
		super(par1, state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getBurningId() {
		return ColoredWool.pictureFactoryBurning.blockID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getActiveId() {
		return ColoredWool.pictureFactoryActive.blockID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getIdleId() {
		return ColoredWool.pictureFactoryIdle.blockID;
	}

	/**
	 * {@inheritDoc}
	 */
	@SideOnly(Side.CLIENT)
	@Override
	protected String getIconPrefix() {
		return "picture";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileEntity createNewTileEntity(final World world) {
		return new TileEntityPictureFactory();
	}

	public static void updateFactoryBlockState(final boolean burn,
			final World par1World, final int x, final int y, final int z) {
		final TileEntity entity = par1World.getBlockTileEntity(x, y, z);
		if (entity == null) {
			return;
		}
		if (!(entity instanceof TileEntityPictureFactory)) {
			return;
		}
		final TileEntityPictureFactory factory = (TileEntityPictureFactory) entity;
		updateFactoryBlockState(factory.isActivated, burn, par1World, x, y, z);
	}

	public static void updateFactoryBlockState(final boolean active,
			final boolean burn, final World par1World, final int x,
			final int y, final int z) {
		final int l = par1World.getBlockMetadata(x, y, z);
		final TileEntity entity = par1World.getBlockTileEntity(x, y, z);
		keepFactoryInventory = true;
		if (active) {
			if (burn) {
				par1World.setBlock(x, y, z,
						ColoredWool.pictureFactoryBurning.blockID);
			} else {
				par1World.setBlock(x, y, z,
						ColoredWool.pictureFactoryActive.blockID);
			}
		} else {
			par1World.setBlock(x, y, z, ColoredWool.pictureFactoryIdle.blockID);
		}
		keepFactoryInventory = false;
		par1World.setBlockMetadataWithNotify(x, y, z, l, 2);
		if (entity != null && entity instanceof TileEntityPictureFactory) {
			final TileEntityPictureFactory factory = (TileEntityPictureFactory) entity;
			factory.isActivated = active;
			factory.isBurning = burn;
			factory.validate();
			par1World.setBlockTileEntity(x, y, z, factory);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClicked(final World world, final int x, final int y,
			final int z, final EntityPlayer player) {
		updateFactoryBlockState(state == FactoryState.Idle,
				state == FactoryState.Burning, world, x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void openMenuImage(final EntityPlayer player,
			final TileEntityFactory entity) {
		ColoredWool.proxy.openPictureFactoryImage(player,
				(TileEntityPictureFactory) entity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getGuiFurnace() {
		return Gui.PictureFactoryFurnace.ordinal();
	}

}
