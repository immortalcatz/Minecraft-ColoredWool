package org.minecraftnauja.coloredwool.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import org.minecraftnauja.coloredwool.Model;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;

/**
 * Menu for the model factory.
 */
public class GuiModelFactoryMenu extends GuiScreen {

	/**
	 * Flat model button.
	 */
	private static final char FLAT_MODEL = 0;

	/**
	 * Cancel button.
	 */
	private static final char CANCEL = 1;

	/**
	 * Player.
	 */
	private final EntityPlayer player;

	/**
	 * Tile entity.
	 */
	private final TileEntityModelFactory entity;

	/**
	 * Screen title.
	 */
	private String screenTitle;

	/**
	 * Initializing constructor.
	 * 
	 * @param player
	 *            player.
	 * @param entity
	 *            tile entity.
	 */
	public GuiModelFactoryMenu(final EntityPlayer player,
			final TileEntityModelFactory entity) {
		this.player = player;
		this.entity = entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(false);
		screenTitle = "Choose a model";
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, 60, "Flat model"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120,
				"Cancel"));
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
				case FLAT_MODEL :
					ModLoader.openGUI(player, new GuiModelFactoryImage(player,
							entity, Model.Flat));
					break;
				case CANCEL :
					mc.displayGuiScreen(null);
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 40, 16777215);
		super.drawScreen(par1, par2, par3);
	}

}