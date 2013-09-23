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
	 * Verifies the value of a component.
	 * 
	 * @param value
	 *            the value.
	 * @return the value.
	 */
	private int verifyValue(final int value) {
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
	public void setColor(final int red, final int green, final int blue) {
		color = ((verifyValue(red) & 0xFF) << 16)
				+ ((verifyValue(green) & 0xFF) << 8)
				+ (verifyValue(blue) & 0xFF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		color = par1NBTTagCompound.getInteger("Color");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Color", color);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public net.minecraft.network.packet.Packet getDescriptionPacket() {
		final NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDataPacket(final INetworkManager net,
			final Packet132TileEntityData pkt) {
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
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.UpdateColoredWoolClient.ordinal());
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			dos.writeInt(color);
			final Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = ColoredWool.MOD_ID;
			p.data = bos.toByteArray();
			p.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(p);
		} catch (final IOException e) {
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
	public void sendColorToServer(final int color) {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final DataOutputStream dos = new DataOutputStream(bos);
			dos.writeInt(Packet.UpdateColoredWoolServer.ordinal());
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			dos.writeInt(color);
			final Packet250CustomPayload p = new Packet250CustomPayload();
			p.channel = ColoredWool.MOD_ID;
			p.data = bos.toByteArray();
			p.length = bos.size();
			PacketDispatcher.sendPacketToServer(p);
		} catch (final IOException e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not send packet");
		}
	}

	/**
	 * Enums for modes.
	 */
	private static enum Mode {

		Step {

			@Override
			public Mode next(final TileEntityColoredWool entity) {
				return Red;
			}

			@Override
			public void apply(final TileEntityColoredWool entity) {
				entity.colorStep++;
				if (entity.colorStep > ColoredWool.config.coloredWool.maxColorStep) {
					entity.colorStep = 1;
					entity.playNote();
				}
			}

		},

		Red {

			@Override
			public Mode next(final TileEntityColoredWool entity) {
				return Green;
			}

			@Override
			public void apply(final TileEntityColoredWool entity) {
				addColor(entity, 0xFF0000, 0x00FFFF, 16);
			}

		},

		Green {

			@Override
			public Mode next(final TileEntityColoredWool entity) {
				return Blue;
			}

			@Override
			public void apply(final TileEntityColoredWool entity) {
				addColor(entity, 0xFF00, 0xFF00FF, 8);
			}

		},

		Blue {

			@Override
			public Mode next(final TileEntityColoredWool entity) {
				entity.playNote();
				return Step;
			}

			@Override
			public void apply(final TileEntityColoredWool entity) {
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
		public static void addColor(final TileEntityColoredWool entity,
				final int maskRead, final int maskReset, final int offset) {
			final int c = ((entity.color & maskRead) >> offset)
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
