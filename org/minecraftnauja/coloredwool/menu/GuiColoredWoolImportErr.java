package org.minecraftnauja.coloredwool.menu;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * Menu for error when importing image.
 */
@SideOnly(Side.CLIENT)
public class GuiColoredWoolImportErr extends GuiScreen {

	/**
	 * Ok button.
	 */
	private static final char OK = 0;

	/**
	 * Error message.
	 */
	private final String errormessage;

	/**
	 * Ok button.
	 */
	private GuiButton okbutton;

	/**
	 * Initializing constructor.
	 * 
	 * @param error
	 *            error message.
	 */
	public GuiColoredWoolImportErr(String error) {
		super();
		this.errormessage = error;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(okbutton = new GuiButton(OK, width / 2 - 35,
				height / 4 + 120, 70, 20, "OK"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		drawCenteredString(fontRenderer, errormessage, width / 2, 40, 16777215);
		super.drawScreen(par1, par2, par3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			switch (par1GuiButton.id) {
			case OK:
				mc.displayGuiScreen(null);
				break;
			}
		}
	}

}