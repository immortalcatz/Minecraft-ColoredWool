package org.minecraftnauja.coloredwool.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.Config.Factory;
import org.minecraftnauja.coloredwool.Packet;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Factory tile entity.
 */
public abstract class TileEntityFactory extends TileEntity implements
		ISidedInventory {

	/**
	 * Index of red rose.
	 */
	protected static final int RED_ROSE = 0;

	/**
	 * Index of cactus green.
	 */
	protected static final int CACTUS_GREEN = 1;

	/**
	 * Index of lapis lazuli.
	 */
	protected static final int LAPIS_LAZULI = 2;

	/**
	 * Index of colored dye.
	 */
	protected static final int COLORED_DYE = 3;

	/**
	 * Index of wool.
	 */
	protected static final int WOOL = 4;

	/**
	 * Index of colored wool.
	 */
	protected static final int COLORED_WOOL = 5;

	/**
	 * Index of coal.
	 */
	protected static final int COAL = 6;

	/**
	 * Slots for items.
	 */
	private static final int[] SLOTS_ITEMS = new int[] { RED_ROSE,
			CACTUS_GREEN, LAPIS_LAZULI, COLORED_DYE, WOOL, COLORED_WOOL };

	/**
	 * Slots for coal.
	 */
	private static final int[] SLOTS_COAL = new int[] { COAL };

	protected String invName;
	protected String imageName = "";
	public int factoryBurnTime;
	public int factoryCookTime;
	public int currentItemBurnTime;
	public boolean isActivated;
	public boolean isBurning;
	protected int imageWidth;
	protected int imageHeight;
	protected int currentX;
	protected int currentY;
	public int progressWidth;
	public int progressHeight;

	/**
	 * Item stacks holding items.
	 */
	protected ItemStack[] factoryItemStacks = new ItemStack[7];

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSizeInventory() {
		return factoryItemStacks.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack getStackInSlot(int par1) {
		return factoryItemStacks[par1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (factoryItemStacks[par1] != null) {
			ItemStack itemstack;
			if (factoryItemStacks[par1].stackSize <= par2) {
				itemstack = factoryItemStacks[par1];
				factoryItemStacks[par1] = null;
				return itemstack;
			} else {
				itemstack = factoryItemStacks[par1].splitStack(par2);
				if (factoryItemStacks[par1].stackSize == 0) {
					factoryItemStacks[par1] = null;
				}
				return itemstack;
			}
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (factoryItemStacks[par1] != null) {
			ItemStack itemstack = factoryItemStacks[par1];
			factoryItemStacks[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		factoryItemStacks[par1] = par2ItemStack;
		if (par2ItemStack != null
				&& par2ItemStack.stackSize > getInventoryStackLimit()) {
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInvName() {
		return isInvNameLocalized() ? invName : "container.furnace";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInvNameLocalized() {
		return invName != null && invName.length() > 0;
	}

	/**
	 * Sets the inventory name.
	 * 
	 * @param invName
	 *            new name.
	 */
	public void setInvName(String invName) {
		this.invName = invName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Returns the cooking progression.
	 * 
	 * @return the cooking progression.
	 */
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int i) {
		int p = factoryCookTime * i / 200;
		if (p < 0)
			return 0;
		if (p > 200 * i) {
			return 200 * i;
		}
		return p;
	}

	/**
	 * Returns the remaining burn time.
	 * 
	 * @return the remaining burn time.
	 */
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int i) {
		if (currentItemBurnTime == 0) {
			currentItemBurnTime = 200;
		}
		return factoryBurnTime * i / currentItemBurnTime;
	}

	public void updateProgressWidth() {
		if (imageWidth < 1) {
			progressWidth = 0;
		} else {
			progressWidth = (int) ((currentX * 100.0F) / imageWidth);
		}
	}

	public void updateProgressHeight() {
		if (imageHeight < 1) {
			progressHeight = 0;
		} else {
			progressHeight = (int) (((imageHeight - (currentY + 1)) * 100.0F) / imageHeight);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getImageProgressWidth(int i) {
		return (int) ((progressWidth * i) / 100.0F);
	}

	@SideOnly(Side.CLIENT)
	public int getImageProgressHeight(int i) {
		return (int) ((progressHeight * i) / 100.0F);
	}

	/**
	 * Indicates if the factory is burning.
	 * 
	 * @return if the factory is burning.
	 */
	public boolean isBurning() {
		return factoryBurnTime > 0;
	}

	/**
	 * Indicates if the factory can smelt the items.
	 * 
	 * @return if the factory can smelt the items.
	 */
	protected boolean canSmelt() {
		return isActivated
				&& (hasRedRose() && hasCactusGreen() && hasLapisLazuli() && hasWool())
				|| (hasColoredDye() && hasWool()) || hasColoredWool();
	}

	/**
	 * Indicates if the furnace has a red rose.
	 * 
	 * @return if the furnace has a red rose.
	 */
	private boolean hasRedRose() {
		if (factoryItemStacks[RED_ROSE] == null) {
			return false;
		}
		return (factoryItemStacks[RED_ROSE].getItem().itemID == Item.dyePowder.itemID)
				&& (factoryItemStacks[RED_ROSE].getItemDamage() == 1);
	}

	/**
	 * Indicates if the furnace has a cactus green.
	 * 
	 * @return if the furnace has a cactus green.
	 */
	private boolean hasCactusGreen() {
		if (factoryItemStacks[CACTUS_GREEN] == null) {
			return false;
		}
		return (factoryItemStacks[CACTUS_GREEN].getItem().itemID == Item.dyePowder.itemID)
				&& (factoryItemStacks[CACTUS_GREEN].getItemDamage() == 2);
	}

	/**
	 * Indicates if the furnace has a lapis lazuli.
	 * 
	 * @return if the furnace has a lapis lazuli.
	 */
	private boolean hasLapisLazuli() {
		if (factoryItemStacks[LAPIS_LAZULI] == null) {
			return false;
		}
		return (factoryItemStacks[LAPIS_LAZULI].getItem().itemID == Item.dyePowder.itemID)
				&& (factoryItemStacks[LAPIS_LAZULI].getItemDamage() == 4);
	}

	/**
	 * Indicates if the furnace has a colored dye.
	 * 
	 * @return if the furnace has a colored dye.
	 */
	private boolean hasColoredDye() {
		if (factoryItemStacks[COLORED_DYE] == null) {
			return false;
		}
		return factoryItemStacks[COLORED_DYE].getItem().itemID == ColoredWool.coloredDye.itemID;
	}

	/**
	 * Indicates if the furnace has a wool.
	 * 
	 * @return if the furnace has a wool.
	 */
	private boolean hasWool() {
		if (factoryItemStacks[WOOL] == null) {
			return false;
		}
		return factoryItemStacks[WOOL].getItem().itemID == Block.cloth.blockID;
	}

	/**
	 * Indicates if the furnace has a colored wool.
	 * 
	 * @return if the furnace has a colored wool.
	 */
	private boolean hasColoredWool() {
		if (factoryItemStacks[COLORED_WOOL] == null) {
			return false;
		}
		return factoryItemStacks[COLORED_WOOL].getItem().itemID == ColoredWool.coloredWool.blockID;
	}

	/**
	 * Uses one combination of items.
	 */
	protected void smeltItem() {
		if (canSmelt()) {
			if (hasRedRose() && hasCactusGreen() && hasLapisLazuli()
					&& hasWool()) {
				useItem(RED_ROSE);
				useItem(CACTUS_GREEN);
				useItem(LAPIS_LAZULI);
				useItem(WOOL);
			} else if (hasColoredDye() && hasWool()) {
				useItem(COLORED_DYE);
				useItem(WOOL);
			} else if (hasColoredWool()) {
				useItem(COLORED_WOOL);
			}
		}
	}

	/**
	 * Uses one of the items at given index.
	 * 
	 * @param index
	 *            index in the furnace.
	 */
	private void useItem(int index) {
		factoryItemStacks[index].stackSize--;
		if (factoryItemStacks[index].stackSize <= 0) {
			factoryItemStacks[index] = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openChest() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeChest() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
		if (par1 == COAL) {
			return TileEntityFurnace.isItemFuel(par2ItemStack);
		} else {
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int par1) {
		return par1 == 0 ? SLOTS_COAL : (par1 == 1 ? SLOTS_ITEMS : SLOTS_COAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
		return this.isItemValidForSlot(par1, par2ItemStack);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
		return false;
	}

	/**
	 * Indicates if the block at given coordinates is already colored.
	 * 
	 * @param x
	 *            x-coordinate.
	 * @param y
	 *            y-coordinate.
	 * @param z
	 *            z-coordinate.
	 * @param color
	 *            wanted color.
	 * @return if it is already colored.
	 */
	public boolean blockAlreadyColored(int x, int y, int z, int color) {
		int id = worldObj.getBlockId(x, y, z);
		if ((id > 0)
				&& ((getConfig().dontEraseAnything) || (getConfig().dontEraseTheseIds
						.contains(id + ";")))) {
			return true;
		}
		TileEntity tmp = worldObj.getBlockTileEntity(x, y, z);
		if (tmp == null) {
			return false;
		}
		if (!(tmp instanceof TileEntityColoredWool)) {
			return false;
		}
		TileEntityColoredWool tmp2 = (TileEntityColoredWool) tmp;
		return tmp2.color == color;
	}

	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration.
	 */
	protected abstract Factory getConfig();

	/**
	 * Gets the image name.
	 * 
	 * @return the image name.
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Sets the image name.
	 * 
	 * @param name
	 *            new name.
	 */
	public void setImageName(String name) {
		imageName = name;
	}

	/**
	 * Sets the new image to generate.
	 * 
	 * @param name
	 *            new image name.
	 */
	public abstract void setImageToGenerate(String name);

	/**
	 * Sends the new selected image to players.
	 */
	public void sendImageToPlayers() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.UpdateFactoryImageClient.ordinal());
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			dos.writeUTF(imageName);
			Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = ColoredWool.MOD_ID;
			p.data = bos.toByteArray();
			p.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(p);
		} catch (IOException e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not send packet");
		}
	}

	/**
	 * Sends the new selected image to server.
	 * 
	 * @param name
	 *            image name.
	 */
	public void sendImageToServer(String name) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.UpdateFactoryImageServer.ordinal());
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			dos.writeUTF(name);
			Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = ColoredWool.MOD_ID;
			p.data = bos.toByteArray();
			p.length = bos.size();
			PacketDispatcher.sendPacketToServer(p);
		} catch (IOException e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not send packet");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("ImageName", imageName);
		par1NBTTagCompound.setInteger("ImageWidth", imageWidth);
		par1NBTTagCompound.setInteger("ImageHeight", imageHeight);
		par1NBTTagCompound.setInteger("CurrentX", currentX);
		par1NBTTagCompound.setInteger("CurrentY", currentY);
		par1NBTTagCompound.setBoolean("IsActivated", isActivated);
		par1NBTTagCompound.setBoolean("IsBurning", isBurning);
		par1NBTTagCompound.setShort("ProgressWidth", (short) progressWidth);
		par1NBTTagCompound.setShort("ProgressHeight", (short) progressHeight);
		par1NBTTagCompound.setShort("BurnTime", (short) factoryBurnTime);
		par1NBTTagCompound.setShort("CookTime", (short) factoryCookTime);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < factoryItemStacks.length; i++) {
			if (factoryItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				factoryItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		par1NBTTagCompound.setTag("Items", nbttaglist);
		if (this.isInvNameLocalized()) {
			par1NBTTagCompound.setString("CustomName", invName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		imageName = par1NBTTagCompound.getString("ImageName");
		imageWidth = par1NBTTagCompound.getInteger("ImageWidth");
		imageHeight = par1NBTTagCompound.getInteger("ImageHeight");
		currentX = par1NBTTagCompound.getInteger("CurrentX");
		currentY = par1NBTTagCompound.getInteger("CurrentY");
		isActivated = par1NBTTagCompound.getBoolean("IsActivated");
		isBurning = par1NBTTagCompound.getBoolean("IsBurning");
		progressWidth = par1NBTTagCompound.getShort("ProgressWidth");
		progressHeight = par1NBTTagCompound.getShort("ProgressHeight");
		factoryBurnTime = par1NBTTagCompound.getShort("BurnTime");
		factoryCookTime = par1NBTTagCompound.getShort("CookTime");

		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
		factoryItemStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < factoryItemStacks.length) {
				factoryItemStacks[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		currentItemBurnTime = TileEntityFurnace
				.getItemBurnTime(factoryItemStacks[COAL]);
		if (par1NBTTagCompound.hasKey("CustomName")) {
			invName = par1NBTTagCompound.getString("CustomName");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public net.minecraft.network.packet.Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.data);
	}

}
