package org.minecraftnauja.coloredwool.menu;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.minecraftnauja.coloredwool.block.ContainerFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gui for the model factory furnace.
 */
@SideOnly(Side.CLIENT)
public class GuiModelFactoryFurnace extends GuiContainer {

	/**
	 * Gui image.
	 */
	private static final ResourceLocation TEXTURE = new ResourceLocation(
			"ColoredWool:textures/gui/pictureFactory.png");

	/**
	 * Tile entity.
	 */
	private TileEntityModelFactory factory;

	/**
	 * Initializing constructor.
	 * 
	 * @param inventoryplayer
	 *            player's inventory.
	 * @param factory
	 *            tile entity.
	 */
	public GuiModelFactoryFurnace(InventoryPlayer inventoryplayer,
			TileEntityModelFactory factory) {
		super(new ContainerFactory(inventoryplayer, factory));
		this.factory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Model Factory", 48, 6, 4210752);
		fontRenderer.drawString("Inventory", 8, ySize - 96 + 2, 4210752);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (factory.isBurning()) {
			int l = factory.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(x + 81, y + 19 + 12 - l, 176, 12 - l, 14,
					l + 2);
		}

		int i1 = factory.getCookProgressScaled(29);
		if (i1 > 0) {
			if (i1 >= 14)
				drawTexturedModalRect(x + 61, y + 41, 176, 14, 14, 4);
			else {
				drawTexturedModalRect(x + 61, y + 41, 176, 14, i1, 4);
			}
			drawTexturedModalRect(x + 100, y + 35, 176, 18, i1 - 14, 16);
		}

		i1 = factory.getImageProgressHeight(52);
		if (i1 > 0) {
			drawTexturedModalRect(x + 134, y + 17 + 52 - i1, 176,
					34 + (52 - i1), 34, i1);
		}
		if (i1 < 52) {
			int i2 = factory.getImageProgressWidth(34);
			if ((i2 > 0) && (i1 >= 0))
				drawTexturedModalRect(x + 134, y + 17 + 51 - i1, 176, 85 - i1,
						i2, 1);
		}
	}

}