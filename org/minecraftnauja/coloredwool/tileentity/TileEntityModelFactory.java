package org.minecraftnauja.coloredwool.tileentity;

import java.awt.image.BufferedImage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.Config.Factory;
import org.minecraftnauja.coloredwool.block.BlockModelFactory;

/**
 * Picture factory tile entity.
 */
public class TileEntityModelFactory extends TileEntityFactory {

	protected BufferedImage imageTop;
	protected BufferedImage imageBottom;
	protected BufferedImage imageLeft;
	protected BufferedImage imageRight;
	protected BufferedImage imageFront;
	protected BufferedImage imageBack;
	protected int imageDepth;
	protected int currentZ;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRestricted() {
		return ColoredWool.config.modelFactory.restricted;
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
			if (!imageName.isEmpty() && imageTop == null) {
				if (!loadImage(imageName)) {
					imageName = "";
				} else {
					BlockModelFactory.updateFactoryBlockState(isActivated,
							isBurning, worldObj, xCoord, yCoord, zCoord);
				}
			}
			if (factoryBurnTime == 0
					&& isActivated
					&& imageTop != null
					&& (canSmelt() || ColoredWool.config.modelFactory.dontRequireItems)) {
				currentItemBurnTime = factoryBurnTime = ColoredWool.config.modelFactory.dontRequireFuel
						? 200
						: TileEntityFurnace
								.getItemBurnTime(factoryItemStacks[COAL]);
				if (factoryBurnTime > 0) {
					flag1 = true;
					if (!ColoredWool.config.modelFactory.dontRequireFuel
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
					&& imageTop != null
					&& (canSmelt() || ColoredWool.config.modelFactory.dontRequireItems)) {
				if (ColoredWool.config.modelFactory.instantCook) {
					factoryCookTime = 200;
				} else {
					factoryCookTime += 16;
				}

				if (factoryCookTime >= 200) {
					factoryCookTime = 0;
					if (!ColoredWool.config.modelFactory.dontRequireItems) {
						smeltItem();
					}
					if (generateImagePart()) {
						BlockModelFactory.updateFactoryBlockState(false, false,
								worldObj, xCoord, yCoord, zCoord);
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
				BlockModelFactory.updateFactoryBlockState(isActivated,
						factoryBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
			} else if (isActivated && !isBurning && factoryBurnTime > 0) {
				BlockModelFactory.updateFactoryBlockState(true, true, worldObj,
						xCoord, yCoord, zCoord);
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
		if ((currentZ < 0) || (currentZ >= imageDepth)) {
			return true;
		}
		final int[] pos = nextVisiblePixel(currentX, currentY, currentZ);
		if (pos == null) {
			return true;
		}
		currentX = pos[0];
		currentY = pos[1];
		currentZ = pos[2];
		final int rgb = pos[3] & 0xFFFFFF;

		final int l = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int x = xCoord;
		int y = yCoord + 1;
		int z = zCoord;
		if (l == 5) {
			x = xCoord - imageWidth / 2 + currentX;
			y = yCoord + 1 + (imageHeight - currentY);
			z = zCoord + imageDepth / 2 - currentZ;
		} else if (l == 6) {
			x = xCoord - imageDepth / 2 + currentZ;
			y = yCoord + 1 + (imageHeight - currentY);
			z = zCoord - imageWidth / 2 + currentX;
		} else if (l == 7) {
			x = xCoord + imageWidth / 2 - currentX;
			y = yCoord + 1 + (imageHeight - currentY);
			z = zCoord - imageDepth / 2 + currentZ;
		} else if (l == 8) {
			x = xCoord + imageDepth / 2 - currentZ;
			y = yCoord + 1 + (imageHeight - currentY);
			z = zCoord + imageWidth / 2 - currentX;
		} else if (l == 1) {
			x = xCoord - 2 - currentZ;
			y = yCoord + imageHeight / 2 - currentY;
			z = zCoord + imageWidth / 2 - currentX;
		} else if (l == 3) {
			x = xCoord + 2 + currentZ;
			y = yCoord + imageHeight / 2 - currentY;
			z = zCoord - imageWidth / 2 + currentX;
		} else if (l == 0) {
			x = xCoord + imageWidth / 2 - currentX;
			y = yCoord + imageHeight / 2 - currentY;
			z = zCoord + 2 + currentZ;
		} else if (l == 2) {
			x = xCoord - imageWidth / 2 + currentX;
			y = yCoord + imageHeight / 2 - currentY;
			z = zCoord - 2 - currentZ;
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

		currentZ -= 1;
		if (currentZ < 0) {
			currentZ = (imageDepth - 1);
			currentX += 1;
			if (currentX >= imageWidth) {
				currentX = 0;
				currentY -= 1;
				if (currentY < 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Factory getConfig() {
		return ColoredWool.config.modelFactory;
	}

	public int[] nextVisiblePixel(final int x, final int y, final int z) {
		if ((x < 0) || (x >= imageWidth)) {
			return null;
		}
		if ((y < 0) || (y >= imageHeight)) {
			return null;
		}
		if ((z < 0) || (z >= imageDepth)) {
			return null;
		}

		if (z == 0) {
			return nextVisiblePixelOnFront(x, y, z);
		}
		if (z == imageDepth - 1) {
			return nextVisiblePixelOnBack(x, y, z);
		}
		return nextVisiblePixelOnSide(x, y, z);
	}

	public int[] nextVisiblePixelOnFront(int x, int y, final int z) {
		final int argb = imageFront.getRGB(x, y);
		if ((argb >> 24 & 0xFF) == 255) {
			return new int[]{x, y, z, argb};
		}
		x++;
		if (x >= imageWidth) {
			x = 0;
			y--;
			if (y < 0) {
				return null;
			}
		}
		return nextVisiblePixelOnBack(x, y, imageDepth - 1);
	}

	public int[] nextVisiblePixelOnBack(final int x, final int y, final int z) {
		final int argb = imageBack.getRGB(imageWidth - (x + 1), y);
		if ((argb >> 24 & 0xFF) == 255) {
			return new int[]{x, y, z, argb};
		}
		return nextVisiblePixelOnFront(x, y, 0);
	}

	public int[] nextVisiblePixelOnSide(final int x, final int y, final int z) {
		if (x == 0) {
			return nextVisiblePixelOnLeft(x, y, z);
		}
		if (x == imageWidth - 1) {
			return nextVisiblePixelOnRight(x, y, z);
		}
		if (y == 0) {
			return nextVisiblePixelOnTop(x, y, z);
		}
		if (y == imageHeight - 1) {
			return nextVisiblePixelOnBottom(x, y, z);
		}
		return nextVisiblePixel(x, y, 0);
	}

	public int[] nextVisiblePixelOnTop(int x, final int y, int z) {
		int argb = imageTop.getRGB(x, imageDepth - (z + 1));
		while ((argb >> 24 & 0xFF) < 255) {
			z--;
			if (z < 0) {
				z = imageDepth - 1;
				x++;
				if (x >= imageWidth) {
					return null;
				}
			}
			argb = imageTop.getRGB(x, imageDepth - (z + 1));
		}
		return new int[]{x, y, z, argb};
	}

	public int[] nextVisiblePixelOnBottom(int x, final int y, int z) {
		int argb = imageBottom.getRGB(x, z);
		while ((argb >> 24 & 0xFF) < 255) {
			z--;
			if (z < 0) {
				z = imageDepth - 1;
				x++;
				if (x >= imageWidth) {
					return nextVisiblePixel(0, y - 1, z);
				}
			}
			argb = imageBottom.getRGB(x, z);
		}
		return new int[]{x, y, z, argb};
	}

	public int[] nextVisiblePixelOnLeft(final int x, final int y, int z) {
		int argb = imageLeft.getRGB(imageDepth - (z + 1), y);
		while ((argb >> 24 & 0xFF) < 255) {
			z--;
			if (z < 0) {
				return nextVisiblePixel(x + 1, y, imageDepth - 1);
			}
			argb = imageLeft.getRGB(imageDepth - (z + 1), y);
		}
		return new int[]{x, y, z, argb};
	}

	public int[] nextVisiblePixelOnRight(final int x, final int y, int z) {
		int argb = imageRight.getRGB(z, y);
		while ((argb >> 24 & 0xFF) < 255) {
			z--;
			if (z < 0) {
				return nextVisiblePixel(0, y - 1, imageDepth - 1);
			}
			argb = imageRight.getRGB(z, y);
		}
		return new int[]{x, y, z, argb};
	}

	public void resetImage() {
		if (imageTop != null) {
			currentY = (imageHeight - 1);
			currentZ = (imageDepth - 1);
		} else {
			currentY = 0;
			currentZ = 0;
		}
		currentX = 0;
		progressWidth = 0;
		progressHeight = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("ImageDepth", imageDepth);
		par1NBTTagCompound.setInteger("CurrentZ", currentZ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		imageDepth = par1NBTTagCompound.getInteger("ImageDepth");
		currentZ = par1NBTTagCompound.getInteger("CurrentZ");
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
		currentX = 0;
		currentY = 0;
		currentZ = 0;
		progressWidth = 0;
		progressHeight = 0;
		if (name.isEmpty()) {
			return;
		} else if (!loadImage(name)) {
			imageName = "";
			return;
		}

		sendImageToPlayers();
		imageWidth = imageFront.getWidth();
		imageHeight = imageFront.getHeight();
		imageDepth = imageLeft.getWidth();
		currentY = (imageHeight - 1);
		currentZ = (imageDepth - 1);
	}

	/**
	 * Loads the image.
	 * 
	 * @param name
	 *            image name.
	 * @return if it has been loaded.
	 */
	private boolean loadImage(final String name) {
		imageTop = ColoredWool.getLocalImage(name + "/top.png");
		if (imageTop == null) {
			return false;
		}
		imageBottom = ColoredWool.getLocalImage(name + "/bottom.png");
		if (imageBottom == null) {
			return false;
		}
		imageLeft = ColoredWool.getLocalImage(name + "/left.png");
		if (imageLeft == null) {
			return false;
		}
		imageRight = ColoredWool.getLocalImage(name + "/right.png");
		if (imageRight == null) {
			return false;
		}
		imageFront = ColoredWool.getLocalImage(name + "/front.png");
		if (imageFront == null) {
			return false;
		}
		imageBack = ColoredWool.getLocalImage(name + "/back.png");
		if (imageBack == null) {
			return false;
		}
		return true;
	}

}
