package org.minecraftnauja.coloredwool.menu;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.Orientation;
import org.minecraftnauja.coloredwool.Packet;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Base for the import image menu.
 */
@SideOnly(Side.CLIENT)
public class GuiColoredWoolImport extends GuiScreen {

	/**
	 * Start button.
	 */
	private static final char START = 0;

	/**
	 * Cancel button.
	 */
	private static final char CANCEL = 1;

	/**
	 * X-orientation.
	 */
	private static final char X_ORIENTATION = 2;

	/**
	 * Y-orientation.
	 */
	private static final char Y_ORIENTATION = 3;

	/**
	 * Text field.
	 */
	private GuiTextField fileInput;

	/**
	 * Start button.
	 */
	private GuiButton startButton;

	/**
	 * Cancel button.
	 */
	private GuiButton cancelButton;

	/**
	 * X-orientation button.
	 */
	private GuiButton xOrientButton;

	/**
	 * Y-orientation button.
	 */
	private GuiButton yOrientButton;

	/**
	 * X-orientation.
	 */
	protected Orientation xOrient;

	/**
	 * Y-orientation.
	 */
	protected Orientation yOrient;

	/**
	 * X-coordinate.
	 */
	private final int x;

	/**
	 * Y-coordinate.
	 */
	private final int y;

	/**
	 * Z-coordinate.
	 */
	private final int z;

	/**
	 * Main menu.
	 */
	private final GuiColoredWoolMenu menu;

	/**
	 * Initializing constructor.
	 * 
	 * @param menu
	 *            main menu.
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 * @param z
	 *            z-coordinate.
	 */
	public GuiColoredWoolImport(final GuiColoredWoolMenu menu, final int x,
			final int y, final int z) {
		super();
		this.menu = menu;
		this.x = x;
		this.y = y;
		this.z = z;
		xOrient = Orientation.West;
		yOrient = Orientation.Up;
	}

	/**
	 * Gets the player.
	 * 
	 * @return the player.
	 */
	public EntityPlayer getPlayer() {
		return menu.player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		fileInput = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
		fileInput.setEnabled(true);
		fileInput.setFocused(true);
		fileInput.setText("");
		buttonList.clear();
		buttonList.add(startButton = new GuiButton(START, width / 2 - 80,
				height / 4 + 120, 70, 20, "Import"));
		buttonList.add(cancelButton = new GuiButton(CANCEL, width / 2 + 10,
				height / 4 + 120, 70, 20, "Cancel"));
		buttonList.add(xOrientButton = new GuiButton(X_ORIENTATION,
				width / 2 - 110, height / 4 + 60, 100, 20, xOrient.toString()));
		buttonList.add(yOrientButton = new GuiButton(Y_ORIENTATION,
				width / 2 + 10, height / 4 + 60, 100, 20, yOrient.toString()));
		Keyboard.enableRepeatEvents(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "Type the image file name to import",
				width / 2, 40, 16777215);
		drawCenteredString(fontRenderer, "X Orientation:", width / 2 - 60,
				height / 4 + 40, 16777215);
		drawCenteredString(fontRenderer, "Y Orientation:", width / 2 + 60,
				height / 4 + 40, 16777215);
		fileInput.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void actionPerformed(final GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			switch (par1GuiButton.id) {
				case START :
					sendPacketToServer();
					mc.displayGuiScreen(null);
					break;
				case CANCEL :
					Keyboard.enableRepeatEvents(false);
					ModLoader.openGUI(menu.player, menu);
					break;
				case X_ORIENTATION :
					xOrient = xOrient.getNext();
					xOrientButton.displayString = xOrient.toString();
					break;
				case Y_ORIENTATION :
					yOrient = yOrient.getNext();
					yOrientButton.displayString = yOrient.toString();
			}
		}
	}

	/**
	 * Sends the packet to server.
	 */
	private void sendPacketToServer() {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.ImportImage.ordinal());
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeUTF(fileInput.getText());
			dos.writeInt(xOrient.ordinal());
			dos.writeInt(yOrient.ordinal());
			final Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = ColoredWool.MOD_ID;
			p.data = bos.toByteArray();
			p.length = bos.size();
			PacketDispatcher.sendPacketToServer(p);
		} catch (final Exception e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not send packet");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void keyTyped(final char par1, final int par2) {
		if (fileInput.isFocused()) {
			fileInput.textboxKeyTyped(par1, par2);
		}
	}

}