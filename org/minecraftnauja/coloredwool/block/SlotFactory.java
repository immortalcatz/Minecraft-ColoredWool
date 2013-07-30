package org.minecraftnauja.coloredwool.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import org.minecraftnauja.coloredwool.tileentity.TileEntityFactory;

public class SlotFactory extends Slot {

	/**
	 * Instance of the factory.
	 */
	private final TileEntityFactory factory;

	/**
	 * Initializing constructor.
	 * 
	 * @param factory
	 *            instance of the factory.
	 */
	public SlotFactory(final TileEntityFactory factory, final int a,
			final int b, final int c) {
		super(factory, a, b, c);
		this.factory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canTakeStack(final EntityPlayer par1EntityPlayer) {
		if (factory.isRestricted()) {
			if (par1EntityPlayer.getEntityName().equals(factory.getOwner())) {
				return super.canTakeStack(par1EntityPlayer);
			} else {
				return false;
			}
		} else {
			return super.canTakeStack(par1EntityPlayer);
		}
	}
}
