package org.minecraftnauja.coloredwool.menu;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Colored wool menu.
 */
@SideOnly(Side.CLIENT)
public class GuiColoredWoolMenu extends GuiScreen {

	/**
	 * Hexadecimal button.
	 */
	private static final char HEXA = 0;

	/**
	 * Saved colors button.
	 */
	private static final char SAVED_COLORS = 1;

	/**
	 * Last color button.
	 */
	private static final char LAST_COLOR = 2;

	/**
	 * Import image button.
	 */
	private static final char IMPORT_IMAGE = 3;

	/**
	 * Done button.
	 */
	private static final char DONE = 4;

	/**
	 * Player.
	 */
	protected EntityPlayer player;

	/**
	 * Tile entity.
	 */
	private final TileEntityColoredWool tileEntity;

	/**
	 * Last selected color.
	 */
	protected static Color lastColor;

	/**
	 * Selected color.
	 */
	protected Color selectedColor;
	protected String screenTitle;
	protected GuiButton lastColorButton;

	/**
	 * Initializing constructor.
	 * 
	 * @param player
	 *            player.
	 * @param tileEntity
	 *            tile entity.
	 * @param color
	 *            selected color.
	 */
	public GuiColoredWoolMenu(final EntityPlayer player,
			final TileEntityColoredWool tileEntity, final Color color) {
		super();
		this.player = player;
		this.tileEntity = tileEntity;
		selectedColor = color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(false);
		screenTitle = "Choose how to select the color";
		buttonList.clear();
		buttonList.add(new GuiButton(HEXA, width / 2 - 100, 60, "Hexa"));
		buttonList.add(new GuiButton(SAVED_COLORS, width / 2 - 100, 85,
				"Saved colors"));
		lastColorButton = new GuiButton(LAST_COLOR, width / 2 - 100, 110,
				"Last color");
		buttonList.add(new GuiButton(IMPORT_IMAGE, width / 2 - 100, 135,
				"Import Image"));
		lastColorButton.enabled = (lastColor != null);
		buttonList.add(lastColorButton);
		buttonList.add(new GuiButton(DONE, width / 2 - 100, height / 4 + 120,
				"Done"));
	}

	/**
	 * Closes the gui.
	 */
	public void close() {
		Keyboard.enableRepeatEvents(false);
		if (selectedColor != null) {
			lastColor = selectedColor;
			tileEntity.sendColorToServer(selectedColor.getRGB());
		}
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
				case HEXA :
					ModLoader.openGUI(player, new GuiColoredWoolHexa(this));
					break;
				case SAVED_COLORS :
					ModLoader.openGUI(player, new GuiColoredWoolSavedColors(
							this));
					break;
				case LAST_COLOR :
					selectedColor = lastColor;
				case DONE :
					close();
					break;
				case IMPORT_IMAGE :
					ModLoader.openGUI(player, new GuiColoredWoolImport(this,
							tileEntity.xCoord, tileEntity.yCoord,
							tileEntity.zCoord));
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