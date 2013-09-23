package org.minecraftnauja.coloredwool;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

public class SavedColors {

	private static final Map savedColors = new HashMap();

	private static int nbColors = 0;

	private static File file;

	private static Properties properties;

	public static boolean addColor(final String name, final Color color) {
		if ((name == null) || (color == null)) {
			return false;
		}
		final int rgb = ((char) color.getRed() << '\020')
				+ ((char) color.getGreen() << '\b') + (char) color.getBlue();
		final String hex = Integer.toHexString(rgb);
		final ColorInformations coloredblockcolorinformations = new ColorInformations(
				hex, rgb, color);
		if (savedColors.put(color, coloredblockcolorinformations) == null) {
			nbColors += 1;
		}
		return true;
	}

	public static boolean addColor(final String hex) {
		if (hex == null) {
			return false;
		}
		final Color color = Color.decode("0x" + hex);
		final int rgb = ((char) color.getRed() << '\020')
				+ ((char) color.getGreen() << '\b') + (char) color.getBlue();
		final ColorInformations coloredblockcolorinformations = new ColorInformations(
				hex, rgb, color);
		if (savedColors.put(color, coloredblockcolorinformations) == null) {
			nbColors += 1;
		}
		return true;
	}

	public static boolean removeColor(final Color color) {
		if (savedColors.remove(color) != null) {
			nbColors -= 1;
			return true;
		}
		return false;
	}

	public static boolean containsColor(final Color color) {
		if (color == null) {
			return false;
		}
		return savedColors.containsKey(color);
	}

	public static void clear() {
		savedColors.clear();
		nbColors = 0;
	}

	public static int getNbColors() {
		return nbColors;
	}

	public static Iterator getColorsIterator() {
		return savedColors.entrySet().iterator();
	}

	public static void save() {
		try {
			if (properties != null) {
				properties.clear();
				int i = 0;
				final Iterator iterator = getColorsIterator();
				while (iterator.hasNext()) {
					final Map.Entry entry = (Map.Entry) iterator.next();
					final ColorInformations coloredblockcolorinformations = (ColorInformations) entry
							.getValue();
					properties.put("Color." + i,
							coloredblockcolorinformations.getHex());
					i++;
				}
				final FileOutputStream fos = new FileOutputStream(file);
				properties.store(fos, "");
				fos.close();
			}
		} catch (final Exception e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not save colors");
		}
	}

	public static void load(final String path) {
		try {
			file = new File(path);
			final File p = file.getParentFile();
			if (p != null && !p.exists()) {
				p.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			final FileInputStream fis = new FileInputStream(file);
			properties = new Properties();
			fis.close();
			final Enumeration enumeration = properties.keys();
			while (enumeration.hasMoreElements()) {
				final String name = (String) enumeration.nextElement();
				loadElement(properties, name);
			}
		} catch (final Exception e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not load colors");
		}
	}

	private static void loadElement(final Properties properties,
			final String name) {
		try {
			if (!name.startsWith("Color.")) {
				return;
			}
			addColor((String) properties.get(name));
		} catch (final Exception e) {
			FMLLog.log(ColoredWool.MOD_ID, Level.SEVERE, e,
					"Could not load element %s", name);
		}
	}

}