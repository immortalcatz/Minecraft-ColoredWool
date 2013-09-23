package org.minecraftnauja.coloredwool.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import org.minecraftnauja.coloredwool.ColoredWool;
import org.minecraftnauja.coloredwool.Packet;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Colored wool entity.
 */
public class TileEntityColoredWool extends TileEntity {

	/**
	 * Its color.
	 */
	public int color;

	/**
	 * Current color step.
	 */
	private int colorStep;

	/**
	 * Current mode.
	 */
	private Mode mode;

	/**
	 * Default constructor.
	 */
	public TileEntityColoredWool() {
		super();
		color = ColoredWool.config.coloredWool.initColor;
		colorStep = ColoredWool.config.coloredWool.initColorStep;
		mode = Mode.Red;
	}

	/**
	 * Verifies that the red, green, blue components are correct.
	 */
	private void verifyValues() {
		color &= 0xFFFFFF;
	}

	/**
	 * Verifies the value of a component.
	 * 
	 * @param value
	 *            the value.
	 * @return the value.
	 */
	private int verifyValue(int value) {
		return Math.max(Math.min(value, 255), 0);
	}

	/**
	 * Sets the color.
	 * 
	 * @param red
	 *            value of red component.
	 * @param green
	 *            value of green component.
	 * @param blue
	 *            value of blue component.
	 */
	public void setColor(int red, int green, int blue) {
		color = ((verifyValue(red) & 0xFF) << 16)
				+ ((verifyValue(green) & 0xFF) << 8)
				+ (verifyValue(blue) & 0xFF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		color = par1NBTTagCompound.getInteger("Color");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Color", color);
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

	/**
	 * Left-click on this entity.
	 */
	public void leftClick() {
		mode.apply(this);
	}

	/**
	 * Right-click on this entity.
	 */
	public void rightClick() {
		mode = mode.next(this);
	}

	/**
	 * Plays a note.
	 */
	private void playNote() {
		worldObj.addBlockEvent(xCoord, yCoord, zCoord,
				ColoredWool.coloredWool.blockID, 0, 0);
	}

	/**
	 * Sends the color to players.
	 */
	public void sendColorToPlayers() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.UpdateColoredWoolClient.ordinal());
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			dos.writeInt(color);
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
	 * Sends the color to server.
	 * 
	 * @param color
	 *            the color.
	 */
	public void sendColorToServer(int color) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.UpdateColoredWoolServer.ordinal());
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			dos.writeInt(color);
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
	 * Enums for modes.
	 */
	private static enum Mode {

		Step {

			public Mode next(TileEntityColoredWool entity) {
				return Red;
			}

			public void apply(TileEntityColoredWool entity) {
				entity.colorStep++;
				if (entity.colorStep > ColoredWool.config.coloredWool.maxColorStep) {
					entity.colorStep = 1;
					entity.playNote();
				}
			}

		},

		Red {

			public Mode next(TileEntityColoredWool entity) {
				return Green;
			}

			public void apply(TileEntityColoredWool entity) {
				addColor(entity, 0xFF0000, 0x00FFFF, 16);
			}

		},

		Green {

			public Mode next(TileEntityColoredWool entity) {
				return Blue;
			}

			public void apply(TileEntityColoredWool entity) {
				addColor(entity, 0xFF00, 0xFF00FF, 8);
			}

		},

		Blue {

			public Mode next(TileEntityColoredWool entity) {
				entity.playNote();
				return Step;
			}

			public void apply(TileEntityColoredWool entity) {
				addColor(entity, 0xFF, 0xFFFF00, 0);
			}

		};

		/**
		 * Gets the next mode.
		 * 
		 * @param entity
		 *            the entity.
		 * 
		 * @return the next mode.
		 */
		public abstract Mode next(TileEntityColoredWool entity);

		/**
		 * Applies to given entity.
		 * 
		 * @param entity
		 *            the entity.
		 */
		public abstract void apply(TileEntityColoredWool entity);

		/**
		 * Increases a component of the entity color.
		 * 
		 * @param entity
		 *            the entity.
		 * @param maskRead
		 *            mask for reading the component.
		 * @param maskReset
		 *            mask for reseting the component.
		 * @param offset
		 *            offset of the component.
		 */
		public static void addColor(TileEntityColoredWool entity, int maskRead,
				int maskReset, int offset) {
			int c = ((entity.color & maskRead) >> offset)
					+ (255 / entity.colorStep);
			entity.color &= maskReset;
			if (c > 255) {
				entity.playNote();
			} else {
				entity.color |= ((c & 0xFF) << offset);
			}
			entity.sendColorToPlayers();
		}

	}

}
