package org.minecraftnauja.coloredwool.menu;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import org.minecraftnauja.coloredwool.ColorInformations;
import org.minecraftnauja.coloredwool.SavedColors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Menu for saved colors.
 */
@SideOnly(Side.CLIENT)
public class GuiColoredWoolSavedColors extends GuiScreen {

	/**
	 * Previous color button.
	 */
	private static final char PREVIOUS_COLOR = 0;

	/**
	 * Next color button.
	 */
	private static final char NEXT_COLOR = 1;

	/**
	 * Add color button.
	 */
	private static final char ADD_COLOR = 2;

	/**
	 * Delete colors button.
	 */
	private static final char DELETE_COLOR = 3;

	/**
	 * Clear colors button.
	 */
	private static final char CLEAR_COLORS = 4;

	/**
	 * Done button.
	 */
	private static final char DONE = 5;

	/**
	 * Previous color button.
	 */
	private GuiButton previousButton;

	/**
	 * Next color button.
	 */
	private GuiButton nextButton;

	/**
	 * Add color button.
	 */
	private GuiButton addButton;

	/**
	 * Delete color button.
	 */
	private GuiButton deleteButton;

	/**
	 * Clear colors button.
	 */
	private GuiButton clearButton;

	/**
	 * Done button.
	 */
	private GuiButton doneButton;

	/**
	 * Selected color button.
	 */
	private GuiColorButton selectedColorButton;

	private int startIndex;
	private int maxColors;
	private int nbColors;

	/**
	 * Main menu.
	 */
	private final GuiColoredWoolMenu menu;

	/**
	 * Selected color.
	 */
	private Color selectedColor;

	/**
	 * Parent color.
	 */
	private Color parentColor;

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
	 * @param menu
	 *            main menu.
	 */
	public GuiColoredWoolSavedColors(GuiColoredWoolMenu menu) {
		super();
		this.menu = menu;
		selectedColor = menu.selectedColor;
		parentColor = menu.selectedColor;
		startIndex = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui() {
		updateScreenTitle();
		Keyboard.enableRepeatEvents(false);
		buttonList.clear();
		buttonList.add(previousButton = new GuiButton(PREVIOUS_COLOR,
				width / 2 - 154, height - 52, 150, 20, "Previous"));
		buttonList.add(addButton = new GuiButton(ADD_COLOR, width / 2 - 154,
				height - 28, 70, 20, "Add"));
		buttonList.add(deleteButton = new GuiButton(DELETE_COLOR,
				width / 2 - 74, height - 28, 70, 20, "Delete"));
		buttonList.add(nextButton = new GuiButton(NEXT_COLOR, width / 2 + 4,
				height - 52, 150, 20, "Next"));
		buttonList.add(clearButton = new GuiButton(CLEAR_COLORS, width / 2 + 4,
				height - 28, 70, 20, "Clear"));
		buttonList.add(doneButton = new GuiButton(DONE, width / 2 + 84,
				height - 28, 70, 20, "Done"));
		updateColors();
		previousButton.enabled = ((maxColors > 0) && (startIndex > 0));
		nextButton.enabled = ((maxColors > 0) && (startIndex + nbColors < SavedColors
				.getNbColors()));
		addButton.enabled = (!SavedColors.containsColor(parentColor));
		deleteButton.enabled = (selectedColorButton != null);
		clearButton.enabled = (SavedColors.getNbColors() > 0);
	}

	/**
	 * Updates the screen title.
	 */
	protected void updateScreenTitle() {
		if (selectedColorButton == null) {
			screenTitle = "Saved colors";
		} else {
			ColorInformations coloredblockcolorinformations = selectedColorButton.color;
			screenTitle = ("Saved color 0x" + coloredblockcolorinformations
					.getHex());
		}
	}

	/**
	 * Updates the colors.
	 */
	protected void updateColors() {
		int x1 = width / 2 - 154;
		byte byte0 = 60;
		int x2 = width / 2 + 154;
		int k = previousButton.yPosition - 20;
		byte byte1 = 14;
		int l = 0;
		if (byte1 - 1 > 0) {
			l = (308 - byte1 * 20) / (byte1 - 1);
		}
		if (l > 10) {
			l = 10;
		}
		int i1 = (k - byte0) / 20 - 1;
		int j1 = 0;
		if (i1 - 1 > 0) {
			j1 = (k - byte0 - i1 * 20) / (i1 - 1);
		}
		if (j1 > 10) {
			j1 = 10;
		}
		maxColors = (byte1 * i1);
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		Iterator iterator = SavedColors.getColorsIterator();
		while ((iterator.hasNext()) && (i2 < startIndex)) {
			iterator.next();
			i2++;
		}
		i2 = 0;

		while ((iterator.hasNext()) && (l1 < i1)) {
			Map.Entry entry = (Map.Entry) iterator.next();
			ColorInformations color = (ColorInformations) entry.getValue();
			GuiColorButton button = new GuiColorButton(9 + i2, x1 + (20 + l)
					* k1, byte0 + (20 + j1) * l1, 20, 20, color);
			if (color.getColor().equals(selectedColor)) {
				selectedColorButton = button;
				button.selected = true;
			}
			buttonList.add(button);
			i2++;
			k1++;
			if (k1 >= byte1) {
				k1 = 0;
				l1++;
			}
		}
		nbColors = i2;
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
			if ((par1GuiButton instanceof GuiColorButton)) {
				selectedColor((GuiColorButton) par1GuiButton);
			} else {
				switch (par1GuiButton.id) {
				case ADD_COLOR:
					addSelectedColor();
					break;
				case DONE:
					SavedColors.save();
					if (selectedColor == null) {
						selectedColor = parentColor;
					}
					menu.selectedColor = selectedColor;
					ModLoader.openGUI(menu.player, menu);
					break;
				case DELETE_COLOR:
					deleteSelectedColor();
					break;
				case CLEAR_COLORS:
					clearAllColors();
					break;
				case PREVIOUS_COLOR:
					previousColors(maxColors);
					break;
				case NEXT_COLOR:
					nextColors(maxColors);
					break;
				}
			}
		}
	}

	/**
	 * Adds the selected color to saved colors.
	 */
	protected void addSelectedColor() {
		Color color = parentColor;
		SavedColors.addColor("", color);
		selectedColor = color;
		initGui();
	}

	/**
	 * Sets given button as the selected color.
	 * 
	 * @param button
	 *            button.
	 */
	protected void selectedColor(GuiColorButton button) {
		if (selectedColorButton != null) {
			selectedColorButton.selected = false;
		}
		selectedColorButton = button;
		selectedColorButton.selected = true;
		selectedColor = selectedColorButton.color.getColor();
		deleteButton.enabled = true;
		updateScreenTitle();
	}

	/**
	 * Deletes the selected color.
	 */
	protected void deleteSelectedColor() {
		if (selectedColorButton == null) {
			return;
		}
		ColorInformations color = selectedColorButton.color;
		SavedColors.removeColor(color.getColor());
		selectedColorButton.selected = false;
		selectedColorButton = null;
		selectedColor = null;
		if (startIndex >= SavedColors.getNbColors()) {
			startIndex = 0;
		}
		initGui();
	}

	/**
	 * Clears all colors.
	 */
	protected void clearAllColors() {
		if (selectedColorButton != null) {
			selectedColorButton.selected = false;
			selectedColorButton = null;
			selectedColor = null;
		}
		SavedColors.clear();
		startIndex = 0;
		initGui();
	}

	/**
	 * Go to previous colors.
	 * 
	 * @param i
	 *            index.
	 */
	protected void previousColors(int i) {
		startIndex -= i;
		if (startIndex < 0) {
			startIndex = 0;
		}
		initGui();
	}

	/**
	 * Go to next colors.
	 * 
	 * @param i
	 *            index.
	 */
	protected void nextColors(int i) {
		startIndex += i;
		int j = SavedColors.getNbColors();
		if (startIndex > j) {
			startIndex = (j - 1);
		}
		initGui();
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