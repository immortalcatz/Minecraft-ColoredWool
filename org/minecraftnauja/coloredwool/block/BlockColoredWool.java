package org.minecraftnauja.coloredwool.block;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Colored cloth block.
 */
public class BlockColoredWool extends Block implements ITileEntityProvider {

	/**
	 * Initializing constructor.
	 * 
	 * @param par1
	 *            block identifier.
	 */
	public BlockColoredWool(final int par1) {
		super(par1, Block.cloth.blockMaterial);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * {@inheritDoc}
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(final int par1, final int par2) {
		return Block.cloth.getIcon(par1, par2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileEntity createNewTileEntity(final World par1World) {
		return new TileEntityColoredWool();
	}

	/**
	 * {@inheritDoc}
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public int colorMultiplier(final IBlockAccess par1IBlockAccess,
			final int par2, final int par3, final int par4) {
		return ((TileEntityColoredWool) par1IBlockAccess.getBlockTileEntity(
				par2, par3, par4)).color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBlockClicked(final World par1World, final int par2,
			final int par3, final int par4, final EntityPlayer par5EntityPlayer) {
		final ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
		if (itemstack == null) {
			return;
		}
		if (itemstack.getItem() != ColoredWool.coloredBrush) {
			return;
		}
		switch (ColoredWool.config.coloredWool.colorSelection) {
			case Manual :
				if (!par1World.isRemote) {
					((TileEntityColoredWool) par1World.getBlockTileEntity(par2,
							par3, par4)).leftClick();
				}
				break;
			default :
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onBlockActivated(final World par1World, final int par2,
			final int par3, final int par4,
			final EntityPlayer par5EntityPlayer, final int par6,
			final float par7, final float par8, final float par9) {
		// Checks if player is holding the brush.
		final ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
		if (itemstack == null) {
			return false;
		}
		if (itemstack.getItem() != ColoredWool.coloredBrush) {
			return false;
		}
		switch (ColoredWool.config.coloredWool.colorSelection) {
			case Manual :
				// Manual color selection.
				if (!par1World.isRemote) {
					((TileEntityColoredWool) par1World.getBlockTileEntity(par2,
							par3, par4)).rightClick();
				}
				return true;
			case Menu :
				// Menu color selection.
				if (par1World.isRemote) {
					final TileEntityColoredWool e = (TileEntityColoredWool) par1World
							.getBlockTileEntity(par2, par3, par4);
					ColoredWool.proxy.openColoredWoolMenu(par5EntityPlayer, e,
							new Color(e.color));
				}
				return true;
			default :
				break;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onBlockEventReceived(final World par1World, final int par2,
			final int par3, final int par4, final int par5, int par6) {
		switch (par5) {
			case 0 :
				par6 = 5;
				par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D,
						par4 + 0.5D, "note.bassattack", 3.0F, 12);
				par1World.spawnParticle("note", par2 + 0.5D, par3 + 1.2D,
						par4 + 0.5D, par6 / 24.0D, 0.0D, 0.0D);
				return true;
			case 1 :
				((TileEntityColoredWool) par1World.getBlockTileEntity(par2,
						par3, par4)).color = par6;
				par1World.markBlockForRenderUpdate(par2, par3, par4);
				return true;
			default :
				return false;
		}
	}

	/*
	 * public int b(xp iblockaccess, int i, int j, int k) { TileEntityColor
	 * tileentitycolor = (TileEntityColor) iblockaccess.b(i, j, k); if
	 * ((ColoredBlock.isMultiplayer()) && (tileentitycolor.needUpdate)) {
	 * tileentitycolor.requestUpdate(); } return
	 * tileentitycolor.getColorMultiplier(); }
	 * 
	 * public void b(fd world, int i, int j, int k, gs player) { if (world.B) {
	 * return; } if (player.t()) { return; } iz itemstack = player.c.b(); if
	 * (itemstack == null) return; if (itemstack.a() != ItemColoredBrush.item) {
	 * return; } if (colorSelection == '\001') { TileEntityColor tileentitycolor
	 * = (TileEntityColor) world .b(i, j, k); tileentitycolor.addColor(world, i,
	 * j, k); world.j(i, j, k); } }
	 */

}