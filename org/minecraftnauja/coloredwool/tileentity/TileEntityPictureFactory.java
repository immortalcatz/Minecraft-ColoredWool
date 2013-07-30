package org.minecraftnauja.coloredwool.tileentity;

import java.awt.image.BufferedImage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.Config.Factory;
import org.minecraftnauja.coloredwool.block.BlockPictureFactory;

/**
 * Picture factory tile entity.
 */
public class TileEntityPictureFactory extends TileEntityFactory {

	protected BufferedImage image;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRestricted() {
		return ColoredWool.config.pictureFactory.restricted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEntity() {
		final boolean flag = factoryBurnTime > 0;
		boolean flag1 = false;

		if (factoryBurnTime > 0) {
			--factoryBurnTime;
		}

		if (!worldObj.isRemote) {
			// Reloads image if necessary.
			if (!imageName.isEmpty() && image == null) {
				if (!loadImage(imageName)) {
					imageName = "";
				} else {
					BlockPictureFactory.updateFactoryBlockState(isActivated,
							isBurning, worldObj, xCoord, yCoord, zCoord);
				}
			}
			if (factoryBurnTime == 0
					&& isActivated
					&& image != null
					&& (canSmelt() || ColoredWool.config.pictureFactory.dontRequireItems)) {
				currentItemBurnTime = factoryBurnTime = ColoredWool.config.pictureFactory.dontRequireFuel
						? 200
						: TileEntityFurnace
								.getItemBurnTime(factoryItemStacks[COAL]);
				if (factoryBurnTime > 0) {
					flag1 = true;
					if (!ColoredWool.config.pictureFactory.dontRequireFuel
							&& factoryItemStacks[COAL] != null) {
						--factoryItemStacks[COAL].stackSize;
						if (factoryItemStacks[COAL].stackSize == 0) {
							factoryItemStacks[COAL] = factoryItemStacks[COAL]
									.getItem().getContainerItemStack(
											factoryItemStacks[COAL]);
						}
					}
				}
			}

			if (isActivated
					&& isBurning()
					&& image != null
					&& (canSmelt() || ColoredWool.config.pictureFactory.dontRequireItems)) {
				if (ColoredWool.config.pictureFactory.instantCook) {
					factoryCookTime = 200;
				} else {
					factoryCookTime += 16;
				}

				if (factoryCookTime >= 200) {
					factoryCookTime = 0;
					if (!ColoredWool.config.pictureFactory.dontRequireItems) {
						smeltItem();
					}
					if (generateImagePart()) {
						BlockPictureFactory.updateFactoryBlockState(false,
								false, worldObj, xCoord, yCoord, zCoord);
						resetImage();
					}
					updateProgressWidth();
					updateProgressHeight();
					flag1 = true;
				}
			} else {
				factoryCookTime = 0;
			}

			if (flag != factoryBurnTime > 0) {
				flag1 = true;
				BlockPictureFactory.updateFactoryBlockState(isActivated,
						factoryBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
			} else if (isActivated && !isBurning && factoryBurnTime > 0) {
				BlockPictureFactory.updateFactoryBlockState(true, true,
						worldObj, xCoord, yCoord, zCoord);
			}
		}

		if (flag1) {
			onInventoryChanged();
		}
	}

	private boolean generateImagePart() {
		if ((currentX < 0) || (currentX >= imageWidth)) {
			return true;
		}
		if ((currentY < 0) || (currentY >= imageHeight)) {
			return true;
		}
		final int[] pos = nextVisiblePixel(currentX, currentY);
		if (pos == null) {
			return true;
		}
		currentX = pos[0];
		currentY = pos[1];
		final int rgb = pos[2] & 0xFFFFFF;

		final int l = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int x = xCoord;
		int y = yCoord;
		int z = zCoord;
		if (l == 5) {
			x = xCoord - imageWidth / 2 + currentX;
			y = yCoord + 1 + (imageHeight - currentY);
		} else if (l == 6) {
			z = zCoord - imageWidth / 2 + currentX;
			y = yCoord + 1 + (imageHeight - currentY);
		} else if (l == 7) {
			x = xCoord + imageWidth / 2 - currentX;
			y = yCoord + 1 + (imageHeight - currentY);
		} else if (l == 8) {
			z = zCoord + imageWidth / 2 - currentX;
			y = yCoord + 1 + (imageHeight - currentY);
		} else if (l == 1) {
			z = zCoord + imageWidth / 2 - currentX;
			x = xCoord - 1 - (imageHeight - currentY);
		} else if (l == 3) {
			z = zCoord - imageWidth / 2 + currentX;
			x = xCoord + 1 + (imageHeight - currentY);
		} else if (l == 0) {
			x = xCoord + imageWidth / 2 - currentX;
			z = zCoord + 1 + (imageHeight - currentY);
		} else if (l == 2) {
			x = xCoord - imageWidth / 2 + currentX;
			z = zCoord - 1 - (imageHeight - currentY);
		}
		if (!blockAlreadyColored(x, y, z, rgb)) {
			TileEntityColoredWool t = null;
			final TileEntity e = worldObj.getBlockTileEntity(x, y, z);
			if (e != null && e instanceof TileEntityColoredWool) {
				t = (TileEntityColoredWool) e;
			} else {
				t = new TileEntityColoredWool();
				worldObj.setBlock(x, y, z, ColoredWool.coloredWool.blockID);
				if (worldObj.getBlockId(x, y, z) != ColoredWool.coloredWool.blockID) {
					return true;
				} else {
					worldObj.setBlockTileEntity(x, y, z, t);
				}
			}
			t.color = rgb;
			t.sendColorToPlayers();
		}

		currentX += 1;
		if (currentX >= imageWidth) {
			currentX = 0;
			currentY -= 1;
		}
		return currentY < 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Factory getConfig() {
		return ColoredWool.config.pictureFactory;
	}

	public int[] nextVisiblePixel(int column, int line) {
		if ((column < 0) || (column >= imageWidth)) {
			return null;
		}
		if ((line < 0) || (line >= imageHeight)) {
			return null;
		}
		int argb = image.getRGB(column, line);
		while ((argb >> 24 & 0xFF) < 255) {
			column++;
			if (column >= imageWidth) {
				column = 0;
				line--;
			}
			if (line < 0) {
				return null;
			}
			argb = image.getRGB(column, line);
		}
		return new int[]{column, line, argb};
	}

	public void resetImage() {
		if (image != null) {
			currentY = (imageHeight - 1);
		} else {
			currentY = 0;
		}
		currentX = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImageToGenerate(final String name) {
		if (name.equals(imageName)) {
			return;
		}
		imageName = name;
		currentY = 0;
		currentX = 0;
		progressWidth = 0;
		progressHeight = 0;
		if (name.isEmpty()) {
			return;
		} else if (!loadImage(name)) {
			imageName = "";
			return;
		}

		sendImageToPlayers();
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();
		currentY = (imageHeight - 1);
	}

	/**
	 * Loads the image.
	 * 
	 * @param name
	 *            image name.
	 * @return if it has been loaded.
	 */
	private boolean loadImage(final String name) {
		image = ColoredWool.getLocalImage(name);
		return image != null;
	}

}
