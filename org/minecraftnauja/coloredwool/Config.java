package org.minecraftnauja.coloredwool;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Mod configuration.
 */
public final class Config {

	/**
	 * Loads the configuration.
	 * 
	 * @param event
	 *            initialization event.
	 * @return the configuration.
	 */
	public static Config load(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();
		Config c = new Config(event, config);
		config.save();
		return c;
	}

	/**
	 * Ids configuration.
	 */
	public final Ids ids;

	/**
	 * Colored wool configuration.
	 */
	public final ColoredWool coloredWool;

	/**
	 * Picture factory configuration.
	 */
	public final PictureFactory pictureFactory;

	/**
	 * Model factory configuration.
	 */
	public final ModelFactory modelFactory;

	/**
	 * Loads the configuration.
	 * 
	 * @param event
	 *            initialization event.
	 * @param config
	 *            the configuration.
	 */
	private Config(FMLPreInitializationEvent event, Configuration config) {
		ids = new Ids(config);
		coloredWool = new ColoredWool(event, config);
		pictureFactory = new PictureFactory(config);
		modelFactory = new ModelFactory(config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Config [ids=" + ids + ", coloredWool=" + coloredWool
				+ ", pictureFactory=" + pictureFactory + ", modelFactory="
				+ modelFactory + "]";
	}

	/**
	 * Ids configuration.
	 */
	public static final class Ids {

		/**
		 * Colored wool identifier.
		 */
		public int coloredWoolId = 501;

		/**
		 * Burning picture factory identifier.
		 */
		public int pictureFactoryBurningId = coloredWoolId + 1;

		/**
		 * Active picture factory identifier.
		 */
		public int pictureFactoryActiveId = pictureFactoryBurningId + 1;

		/**
		 * Idle picture factory identifier.
		 */
		public int pictureFactoryIdleId = pictureFactoryActiveId + 1;

		/**
		 * Burning model factory identifier.
		 */
		public int modelFactoryBurningId = pictureFactoryIdleId + 1;

		/**
		 * Active model factory identifier.
		 */
		public int modelFactoryActiveId = modelFactoryBurningId + 1;

		/**
		 * Idle model factory identifier.
		 */
		public int modelFactoryIdleId = modelFactoryActiveId + 1;

		/**
		 * Colored dye identifier.
		 */
		public int coloredDyeId = 5001;

		/**
		 * Colored brush identifier.
		 */
		public int coloredBrushId = coloredDyeId + 1;

		/**
		 * Loads the configuration.
		 * 
		 * @param config
		 *            the configuration.
		 */
		private Ids(Configuration config) {
			// Blocks.
			coloredWoolId = config.getBlock("ColoredWool", coloredWoolId)
					.getInt();
			pictureFactoryBurningId = config.getBlock("PictureFactoryBurning",
					pictureFactoryBurningId).getInt();
			pictureFactoryActiveId = config.getBlock("PictureFactoryActive",
					pictureFactoryActiveId).getInt();
			pictureFactoryIdleId = config.getBlock("PictureFactoryIdle",
					pictureFactoryIdleId).getInt();
			modelFactoryBurningId = config.getBlock("ModelFactoryBurning",
					modelFactoryBurningId).getInt();
			modelFactoryActiveId = config.getBlock("ModelFactoryActive",
					modelFactoryActiveId).getInt();
			modelFactoryIdleId = config.getBlock("ModelFactoryIdle",
					modelFactoryIdleId).getInt();
			// Items.
			coloredDyeId = config.getItem("ColoredDye", coloredDyeId).getInt();
			coloredBrushId = config.getItem("ColoredBrush", coloredBrushId)
					.getInt();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "Ids [coloredWoolId=" + coloredWoolId
					+ ", pictureFactoryBurningId=" + pictureFactoryBurningId
					+ ", pictureFactoryActiveId=" + pictureFactoryActiveId
					+ ", pictureFactoryIdleId=" + pictureFactoryIdleId
					+ ", modelFactoryBurningId=" + modelFactoryBurningId
					+ ", modelFactoryActiveId=" + modelFactoryActiveId
					+ ", modelFactoryIdleId=" + modelFactoryIdleId
					+ ", coloredDyeId=" + coloredDyeId + ", coloredBrushId="
					+ coloredBrushId + "]";
		}

	}

	/**
	 * Colored wool configuration.
	 */
	public static final class ColoredWool {

		/**
		 * Configuration category.
		 */
		private static final String CATEGORY = "coloredwool";

		/**
		 * Folder for images.
		 */
		public String folder = "";

		/**
		 * File for saved colors.
		 */
		public String savedColors = "";

		/**
		 * Initial value for red component.
		 */
		public int initColorRed = 56;

		/**
		 * Initial value for green component.
		 */
		public int initColorGreen = 56;

		/**
		 * Initial value for blue component.
		 */
		public int initColorBlue = 56;

		/**
		 * Initial color.
		 */
		public int initColor = 0;

		/**
		 * Initial value for number of steps required to reach 255.
		 */
		public int initColorStep = 5;

		/**
		 * Maximal number of steps.
		 */
		public int maxColorStep = 10;

		/**
		 * How the color is chosen.
		 */
		public ColorSelection colorSelection = ColorSelection.Menu;

		/**
		 * Loads the configuration.
		 * 
		 * @param event
		 *            initialization event.
		 * @param config
		 *            the configuration.
		 */
		private ColoredWool(FMLPreInitializationEvent event,
				Configuration config) {
			File f = new File(event.getModConfigurationDirectory(), "images");
			folder = config.get(CATEGORY, "Folder", f.getAbsolutePath())
					.getString();
			f = new File(event.getModConfigurationDirectory(),
					"savedColors.properties");
			savedColors = config.get(CATEGORY, "SavedColors",
					f.getAbsolutePath()).getString();
			initColorRed = get(config, "InitColorRed", initColorRed, 0, 255);
			initColorGreen = get(config, "InitColorGreen", initColorGreen, 0,
					255);
			initColorBlue = get(config, "InitColorBlue", initColorBlue, 0, 255);
			initColor = ((initColorRed & 0xFF) << 16)
					+ ((initColorGreen & 0xFF) << 8) + (initColorBlue & 0xFF);
			maxColorStep = get(config, "MaxColorStep", maxColorStep, 1,
					Integer.MAX_VALUE);
			initColorStep = get(config, "InitColorStep", initColorStep, 1,
					Integer.MAX_VALUE);
			colorSelection = ColorSelection.parse(config.get(CATEGORY,
					"ColorSelection", colorSelection.toString()).getString());
		}

		/**
		 * Gets a value from configuration.
		 * 
		 * @param config
		 *            configuration.
		 * @param key
		 *            the key.
		 * @param value
		 *            default value.
		 * @param min
		 *            minimal value.
		 * @param max
		 *            maximal value.
		 * @return the value.
		 */
		private int get(Configuration config, String key, int value, int min,
				int max) {
			return Math.max(Math.min(
					config.get(CATEGORY, key, value).getInt(value), max), min);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "ColoredWool [folder=" + folder + ", savedColors="
					+ savedColors + ", initColorRed=" + initColorRed
					+ ", initColorGreen=" + initColorGreen + ", initColorBlue="
					+ initColorBlue + ", initColorStep=" + initColorStep
					+ ", maxColorStep=" + maxColorStep + ", colorSelection="
					+ colorSelection + "]";
		}

	}

	/**
	 * Factory configuration.
	 */
	public static abstract class Factory {

		/**
		 * Don't erase existing blocks.
		 */
		public boolean dontEraseAnything = false;

		/**
		 * Don't erase blocks with given ids.
		 */
		public String dontEraseTheseIds = "";

		/**
		 * Items are not required to use the factory.
		 */
		public boolean dontRequireItems = true;

		/**
		 * Fuel is not required to use the factory.
		 */
		public boolean dontRequireFuel = true;

		/**
		 * No waiting time.
		 */
		public boolean instantCook = true;

		/**
		 * Loads the configuration.
		 * 
		 * @param config
		 *            the configuration.
		 * @param category
		 *            configuration category.
		 */
		private Factory(Configuration config, String category) {
			dontEraseAnything = config.get(category, "DontEraseAnything",
					dontEraseAnything).getBoolean(dontEraseAnything);
			dontEraseTheseIds = config.get(category, "DontEraseTheseIds",
					dontEraseTheseIds).getString();
			dontRequireItems = config.get(category, "DontRequireItems",
					dontRequireItems).getBoolean(dontRequireItems);
			dontRequireFuel = config.get(category, "DontRequireFuel",
					dontRequireFuel).getBoolean(dontRequireFuel);
			instantCook = config.get(category, "InstantCook", instantCook)
					.getBoolean(instantCook);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "Factory [dontEraseAnything=" + dontEraseAnything
					+ ", dontEraseTheseIds=" + dontEraseTheseIds
					+ ", dontRequireItems=" + dontRequireItems
					+ ", dontRequireFuel=" + dontRequireFuel + ", instantCook="
					+ instantCook + "]";
		}

	}

	/**
	 * Picture factory configuration.
	 */
	public static final class PictureFactory extends Factory {

		/**
		 * Configuration category.
		 */
		private static final String CATEGORY = "picturefactory";

		/**
		 * Loads the configuration.
		 * 
		 * @param config
		 *            the configuration.
		 */
		private PictureFactory(Configuration config) {
			super(config, CATEGORY);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "PictureFactory [dontEraseAnything=" + dontEraseAnything
					+ ", dontEraseTheseIds=" + dontEraseTheseIds
					+ ", dontRequireItems=" + dontRequireItems
					+ ", dontRequireFuel=" + dontRequireFuel + ", instantCook="
					+ instantCook + "]";
		}

	}

	/**
	 * Model factory configuration.
	 */
	public static final class ModelFactory extends Factory {

		/**
		 * Configuration category.
		 */
		private static final String CATEGORY = "modelfactory";

		/**
		 * Loads the configuration.
		 * 
		 * @param config
		 *            the configuration.
		 */
		private ModelFactory(Configuration config) {
			super(config, CATEGORY);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "ModelFactory [dontEraseAnything=" + dontEraseAnything
					+ ", dontEraseTheseIds=" + dontEraseTheseIds
					+ ", dontRequireItems=" + dontRequireItems
					+ ", dontRequireFuel=" + dontRequireFuel + ", instantCook="
					+ instantCook + "]";
		}

	}

	/**
	 * Enums for color selection methods.
	 */
	public static enum ColorSelection {

		/**
		 * Click on the block to choose a color.
		 */
		Manual {

			public String toString() {
				return "manual";
			}

		},
		/**
		 * Open a menu to choose a color.
		 */
		Menu {

			public String toString() {
				return "menu";
			}

		},
		/**
		 * Can't choose a color.
		 */
		None {

			public String toString() {
				return "none";
			}

		};

		/**
		 * Parses given string and returns corresponding color selection mode.
		 * 
		 * @param s
		 *            string to parse.
		 * @return corresponding color selection mode.
		 */
		public static ColorSelection parse(String s) {
			if (s.equals("menu")) {
				return ColorSelection.Menu;
			} else if (s.equals("manual")) {
				return ColorSelection.Manual;
			} else {
				return ColorSelection.None;
			}
		}

	}

}
