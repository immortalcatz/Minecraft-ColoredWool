package org.minecraftnauja.coloredwool.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.minecraftnauja.coloredwool.ColorInformations;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Button displaying a color.
 */
@SideOnly(Side.CLIENT)
public class GuiColorButton extends GuiButton {

	/**
	 * Informations about the color.
	 */
	public ColorInformations color;

	/**
	 * X-coordinate.
	 */
	public final int xColor;

	/**
	 * Y-coordinate.
	 */
	public final int yColor;

	/**
	 * Width.
	 */
	public final int widthColor;

	/**
	 * Height.
	 */
	public final int heightColor;

	/**
	 * If the button is selected.
	 */
	public boolean selected;

	/**
	 * Initializing constructor.
	 * 
	 * @param i
	 *            button id.
	 * @param j
	 *            x-coordinate.
	 * @param k
	 *            y-coordinate.
	 * @param w
	 *            width.
	 * @param h
	 *            height.
	 * @param color
	 */
	public GuiColorButton(int i, int j, int k, int w, int h,
			ColorInformations color) {
		super(i, j, k, w, h, "");
		this.color = color;
		this.xColor = (xPosition + width / 6);
		this.yColor = (yPosition + height / 6);
		this.widthColor = (width - width / 3);
		this.heightColor = (height - height / 3);
		this.selected = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getHoverState(boolean par1) {
		return selected ? 2 : super.getHoverState(par1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		super.drawButton(par1Minecraft, par2, par3);
		drawRect(this.xColor, this.yColor, this.xColor + this.widthColor,
				this.yColor + this.heightColor, this.color.getColor().getRGB());
	}
	
}