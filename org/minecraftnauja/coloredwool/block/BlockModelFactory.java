package org.minecraftnauja.coloredwool.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.menu.Gui;
import org.minecraftnauja.coloredwool.tileentity.TileEntityFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Model factory block.
 */
public class BlockModelFactory extends BlockFactory {

	/**
	 * Initializing constructor.
	 * 
	 * @param par1
	 *            block identifier.
	 * @param state
	 *            its state.
	 */
	public BlockModelFactory(int par1, FactoryState state) {
		super(par1, state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getBurningId() {
		return ColoredWool.modelFactoryBurning.blockID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getActiveId() {
		return ColoredWool.modelFactoryActive.blockID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getIdleId() {
		return ColoredWool.modelFactoryIdle.blockID;
	}

	/**
	 * {@inheritDoc}
	 */
	@SideOnly(Side.CLIENT)
	@Override
	protected String getIconPrefix() {
		return "model";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityModelFactory();
	}

	public static void updateFactoryBlockState(boolean burn, World par1World,
			int x, int y, int z) {
		TileEntity entity = par1World.getBlockTileEntity(x, y, z);
		if (entity == null)
			return;
		if (!(entity instanceof TileEntityModelFactory)) {
			return;
		}
		TileEntityModelFactory factory = (TileEntityModelFactory) entity;
		updateFactoryBlockState(factory.isActivated, burn, par1World, x, y, z);
	}

	public static void updateFactoryBlockState(boolean active, boolean burn,
			World par1World, int x, int y, int z) {
		int l = par1World.getBlockMetadata(x, y, z);
		TileEntity entity = par1World.getBlockTileEntity(x, y, z);
		keepFactoryInventory = true;
		if (active) {
			if (burn)
				par1World.setBlock(x, y, z,
						ColoredWool.modelFactoryBurning.blockID);
			else
				par1World.setBlock(x, y, z,
						ColoredWool.modelFactoryActive.blockID);
		} else {
			par1World.setBlock(x, y, z, ColoredWool.modelFactoryIdle.blockID);
		}
		keepFactoryInventory = false;
		par1World.setBlockMetadataWithNotify(x, y, z, l, 2);
		if (entity != null && entity instanceof TileEntityModelFactory) {
			TileEntityModelFactory factory = (TileEntityModelFactory) entity;
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
	protected void onClicked(World world, int x, int y, int z,
			EntityPlayer player) {
		updateFactoryBlockState(state == FactoryState.Idle,
				state == FactoryState.Burning, world, x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void openMenuImage(EntityPlayer player, TileEntityFactory entity) {
		ColoredWool.proxy.openModelFactoryMenu(player,
				(TileEntityModelFactory) entity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getGuiFurnace() {
		return Gui.ModelFactoryFurnace.ordinal();
	}

}
