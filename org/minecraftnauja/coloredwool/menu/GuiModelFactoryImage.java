package org.minecraftnauja.coloredwool.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import org.minecraftnauja.coloredwool.Model;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gui for the model factory.
 */
@SideOnly(Side.CLIENT)
public class GuiModelFactoryImage extends GuiScreen {

	/**
	 * Done button.
	 */
	private static final char DONE = 0;

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
	 * Name text field.
	 */
	private GuiTextField nameButton;

	/**
	 * Done button.
	 */
	private GuiButton doneButton;

	/**
	 * Cancel button.
	 */
	private GuiButton cancelButton;

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
	 * @param type
	 *            type.
	 */
	public GuiModelFactoryImage(final EntityPlayer player,
			final TileEntityModelFactory entity, final Model type) {
		super();
		this.player = player;
		this.entity = entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		screenTitle = "Choose model to generate";
		nameButton = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200,
				20);
		final String name = entity.getImageName();
		nameButton.setText(name == null ? "" : name);
		nameButton.setEnabled(true);
		nameButton.setFocused(true);
		nameButton.setMaxStringLength(42);
		buttonList.clear();
		doneButton = new GuiButton(DONE, width / 2 - 105, height / 4 + 120, 90,
				20, "Done");
		cancelButton = new GuiButton(CANCEL, width / 2 + 15, height / 4 + 120,
				90, 20, "Cancel");
		buttonList.add(doneButton);
		buttonList.add(cancelButton);
		checkName();
	}

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
				case 0 :
					close();
					break;
				case CANCEL :
					ModLoader.openGUI(player, new GuiModelFactoryMenu(player,
							entity));
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