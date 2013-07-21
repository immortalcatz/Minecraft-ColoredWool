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
	private EntityPlayer player;

	/**
	 * Tile entity.
	 */
	private TileEntityModelFactory entity;

	/**
	 * Screen title.
	 */
	private String screenTitle;

	/**
	 * Update counter.
	 */
	private int updateCounter;

	/**
	 * Initializing constructor.
	 * 
	 * @param player
	 *            player.
	 * @param entity
	 *            tile entity.
	 */
	public GuiModelFactoryMenu(EntityPlayer player,
			TileEntityModelFactory entity) {
		this.player = player;
		this.entity = entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(false);
		this.screenTitle = "Choose a model";
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 60,
				"Flat model"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100,
				this.height / 4 + 120, "Cancel"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		updateCounter += 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			switch (par1GuiButton.id) {
			case FLAT_MODEL:
				ModLoader.openGUI(this.player, new GuiModelFactoryImage(
						this.player, this.entity, Model.Flat));
				break;
			case CANCEL:
				mc.displayGuiScreen(null);
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 40, 16777215);
		super.drawScreen(par1, par2, par3);
	}

}