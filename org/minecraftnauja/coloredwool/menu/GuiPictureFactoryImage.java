package org.minecraftnauja.coloredwool.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;
import org.minecraftnauja.coloredwool.tileentity.TileEntityPictureFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPictureFactoryImage extends GuiScreen {

	/**
	 * Done button.
	 */
	private static final char DONE = 0;

	/**
	 * Tile entity.
	 */
	private final TileEntityPictureFactory entity;

	/**
	 * Name text field.
	 */
	private GuiTextField nameButton;

	/**
	 * Done button.
	 */
	private GuiButton doneButton;

	/**
	 * Screen title.
	 */
	private String screenTitle;

	/**
	 * Initializing constructor.
	 * 
	 * @param entity
	 *            tile entity.
	 */
	public GuiPictureFactoryImage(final TileEntityPictureFactory entity) {
		super();
		this.entity = entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		screenTitle = "Choose image to generate";
		nameButton = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200,
				20);
		final String name = entity.getImageName();
		nameButton.setText(name == null ? "" : name);
		nameButton.setEnabled(true);
		nameButton.setFocused(true);
		nameButton.setMaxStringLength(42);
		buttonList.clear();
		doneButton = new GuiButton(DONE, width / 2 - 100, height / 4 + 120,
				"Done");
		buttonList.add(doneButton);
		checkName();
	}

	/**
	 * Closes the gui.
	 */
	public void close() {
		final String name = nameButton.getText();
		entity.sendImageToServer(name);
		mc.displayGuiScreen(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void actionPerformed(final GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			switch (par1GuiButton.id) {
				case DONE :
					close();
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void keyTyped(final char par1, final int par2) {
		nameButton.textboxKeyTyped(par1, par2);
		checkName();
		if (par1 == '\r') {
			actionPerformed(doneButton);
		}
	}

	/**
	 * Checks the name.
	 * 
	 * @return if it is valid.
	 */
	public boolean checkName() {
		final String s = nameButton.getText();
		final boolean flag = s.length() > 0;
		doneButton.enabled = flag;
		return flag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 40, 16777215);
		nameButton.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

}