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
	public GuiColorButton(final int i, final int j, final int k, final int w,
			final int h, final ColorInformations color) {
		super(i, j, k, w, h, "");
		this.color = color;
		xColor = (xPosition + width / 6);
		yColor = (yPosition + height / 6);
		widthColor = (width - width / 3);
		heightColor = (height - height / 3);
		selected = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getHoverState(final boolean par1) {
		return selected ? 2 : super.getHoverState(par1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawButton(final Minecraft par1Minecraft, final int par2,
			final int par3) {
		super.drawButton(par1Minecraft, par2, par3);
		drawRect(xColor, yColor, xColor + widthColor, yColor + heightColor,
				color.getColor().getRGB());
	}

}