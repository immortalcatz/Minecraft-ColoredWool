package org.minecraftnauja.coloredwool.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Colored brush item.
 */
public class ItemColoredBrush extends Item {

	/**
	 * Initializing constructor.
	 * 
	 * @param par1
	 *            item identifier.
	 */
	public ItemColoredBrush(int par1) {
		super(par1);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * {@inheritDoc}
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("ColoredWool:brush");
	}

}