package org.minecraftnauja.coloredwool;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * Handler for packets.
 */
public class PacketHandler implements IPacketHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPacketData(final INetworkManager manager,
			final Packet250CustomPayload packet, final Player player) {
		if (packet.channel.equals(ColoredWool.MOD_ID)) {
			try {
				final ByteArrayInputStream bis = new ByteArrayInputStream(
						packet.data);
				final DataInputStream dis = new DataInputStream(bis);
				final Packet p = Packet.values()[dis.readInt()];
				p.handle(packet, dis, (EntityPlayer) player);
			} catch (final IOException e) {
				FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
						"Could not handle the packet");
			}
		}
	}

}
