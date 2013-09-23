package org.minecraftnauja.coloredwool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;

import org.minecraftnauja.coloredwool.menu.GuiColoredWoolImportErr;
import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;
import org.minecraftnauja.coloredwool.tileentity.TileEntityFactory;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * Custom packets.
 */
public enum Packet {

	/**
	 * Imports an image.
	 */
	ImportImage {

		public void handle(Packet250CustomPayload packet, DataInputStream dis,
				EntityPlayer player) throws IOException {
			int x = dis.readInt(), y = dis.readInt(), z = dis.readInt();
			String name = dis.readUTF();
			Orientation xOrient = Orientation.values()[dis.readInt()];
			Orientation yOrient = Orientation.values()[dis.readInt()];
			if (!xOrient.isCompatible(yOrient)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);
				dos.writeInt(ImportImageError.ordinal());
				dos.writeInt(ImportError.Orientation.ordinal());
				Packet250CustomPayload p = new Packet250CustomPayload();
				p.channel = ColoredWool.MOD_ID;
				p.data = bos.toByteArray();
				p.length = bos.size();
				PacketDispatcher.sendPacketToPlayer(p, (Player) player);
			} else {
				BufferedImage image = ColoredWool.getLocalImage(name);
				if (image == null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(bos);
					dos.writeInt(ImportImageError.ordinal());
					dos.writeInt(ImportError.ImageNotFound.ordinal());
					Packet250CustomPayload p = new Packet250CustomPayload();
					p.channel = ColoredWool.MOD_ID;
					p.data = bos.toByteArray();
					p.length = bos.size();
					PacketDispatcher.sendPacketToPlayer(p, (Player) player);
				} else {
					ColoredWool.addImageImport(new ImageImport(player, x, y, z,
							image, xOrient, yOrient));
				}
			}
		}

	},

	/**
	 * Error while importing image.
	 */
	ImportImageError {

		public void handle(Packet250CustomPayload packet, DataInputStream dis,
				EntityPlayer player) throws IOException {
			ImportError e = ImportError.values()[dis.readInt()];
			ModLoader.openGUI(player,
					new GuiColoredWoolImportErr(e.getMessage()));
		}

	},

	/**
	 * Updates the colored wool server-side.
	 */
	UpdateColoredWoolServer {

		public void handle(Packet250CustomPayload packet, DataInputStream dis,
				EntityPlayer player) throws IOException {
			TileEntity e = player.worldObj.getBlockTileEntity(dis.readInt(),
					dis.readInt(), dis.readInt());
			if (e != null && e instanceof TileEntityColoredWool) {
				TileEntityColoredWool w = (TileEntityColoredWool) e;
				w.color = (dis.readInt() & 0xFFFFFF);
				w.sendColorToPlayers();
			}
		}

	},

	/**
	 * Updates the colored wool client-side.
	 */
	UpdateColoredWoolClient {

		public void handle(Packet250CustomPayload packet, DataInputStream dis,
				EntityPlayer player) throws IOException {
			TileEntity e = player.worldObj.getBlockTileEntity(dis.readInt(),
					dis.readInt(), dis.readInt());
			if (e != null && e instanceof TileEntityColoredWool) {
				TileEntityColoredWool w = (TileEntityColoredWool) e;
				w.color = (dis.readInt() & 0xFFFFFF);
				player.worldObj.markBlockForRenderUpdate(e.xCoord, e.yCoord,
						e.zCoord);
			}
		}

	},

	/**
	 * Updates the factory's image server-side.
	 */
	UpdateFactoryImageServer {

		public void handle(Packet250CustomPayload packet, DataInputStream dis,
				EntityPlayer player) throws IOException {
			TileEntity e = player.worldObj.getBlockTileEntity(dis.readInt(),
					dis.readInt(), dis.readInt());
			if (e != null && e instanceof TileEntityFactory) {
				TileEntityFactory w = (TileEntityFactory) e;
				w.setImageToGenerate(dis.readUTF());
			}
		}

	},

	/**
	 * Updates the factory's image client-side.
	 */
	UpdateFactoryImageClient {

		public void handle(Packet250CustomPayload packet, DataInputStream dis,
				EntityPlayer player) throws IOException {
			TileEntity e = player.worldObj.getBlockTileEntity(dis.readInt(),
					dis.readInt(), dis.readInt());
			if (e != null && e instanceof TileEntityFactory) {
				TileEntityFactory w = (TileEntityFactory) e;
				w.setImageName(dis.readUTF());
			}
		}

	};

	/**
	 * Handles the packet.
	 * 
	 * @param packet
	 *            the packet.
	 * @param dis
	 *            input stream.
	 * @param player
	 *            the player.
	 * @throws IOException
	 *             error with IO.
	 */
	public abstract void handle(Packet250CustomPayload packet,
			DataInputStream dis, EntityPlayer player) throws IOException;

}
