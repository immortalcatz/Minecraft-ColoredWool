package org.minecraftnauja.coloredwool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.minecraftnauja.coloredwool.block.BlockColoredWool;
import org.minecraftnauja.coloredwool.block.BlockModelFactory;
import org.minecraftnauja.coloredwool.block.BlockPictureFactory;
import org.minecraftnauja.coloredwool.block.FactoryState;
import org.minecraftnauja.coloredwool.item.ItemColoredBrush;
import org.minecraftnauja.coloredwool.item.ItemColoredDye;
import org.minecraftnauja.coloredwool.menu.GuiHandler;
import org.minecraftnauja.coloredwool.tileentity.TileEntityColoredWool;
import org.minecraftnauja.coloredwool.tileentity.TileEntityModelFactory;
import org.minecraftnauja.coloredwool.tileentity.TileEntityPictureFactory;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "ColoredWool", name = "ColoredWool", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "ColoredWool" }, packetHandler = PacketHandler.class)
public class ColoredWool implements ITickHandler {

	/**
	 * Mod identifier.
	 */
	public static final String MOD_ID = "ColoredWool";

	/**
	 * Mod instance.
	 */
	@Instance("ColoredWool")
	public static ColoredWool instance;

	/**
	 * Proxy instance.
	 */
	@SidedProxy(clientSide = "org.minecraftnauja.coloredwool.ClientProxy", serverSide = "org.minecraftnauja.coloredwool.CommonProxy")
	public static CommonProxy proxy;

	/**
	 * Mod configuration.
	 */
	public static Config config;

	/**
	 * Colored wool instance.
	 */
	public static Block coloredWool;

	/**
	 * Burning picture factory instance.
	 */
	public static Block pictureFactoryBurning;

	/**
	 * Active picture factory instance.
	 */
	public static Block pictureFactoryActive;

	/**
	 * Idle picture factory instance.
	 */
	public static Block pictureFactoryIdle;

	/**
	 * Burning model factory instance.
	 */
	public static Block modelFactoryBurning;

	/**
	 * Active model factory instance.
	 */
	public static Block modelFactoryActive;

	/**
	 * Idle model factory instance.
	 */
	public static Block modelFactoryIdle;

	/**
	 * Colored dye instance.
	 */
	public static Item coloredDye;

	/**
	 * Colored brush instance.
	 */
	public static Item coloredBrush;

	/**
	 * The side.
	 */
	public static Side side;

	/**
	 * List of image import.
	 */
	private static List<ImageImport> imageImport;

	/**
	 * {@inheritDoc}
	 */
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		side = event.getSide();
		config = Config.load(event);
		SavedColors.load(config.coloredWool.savedColors);
	}

	/**
	 * {@inheritDoc}
	 */
	@Init
	public void load(final FMLInitializationEvent event) throws IOException {
		// Colored wool.
		coloredWool = new BlockColoredWool(config.ids.coloredWoolId)
				.setHardness(Block.cloth.blockHardness)
				.setStepSound(Block.cloth.stepSound)
				.setUnlocalizedName("coloredWool");
		// Picture factory.
		pictureFactoryBurning = new BlockPictureFactory(
				config.ids.pictureFactoryBurningId, FactoryState.Burning)
				.setHardness(Block.furnaceBurning.blockHardness)
				.setStepSound(Block.furnaceBurning.stepSound)
				.setLightValue(0.875F).setUnlocalizedName("pictureFactory");
		pictureFactoryActive = new BlockPictureFactory(
				config.ids.pictureFactoryActiveId, FactoryState.Active)
				.setHardness(Block.furnaceIdle.blockHardness)
				.setStepSound(Block.furnaceIdle.stepSound)
				.setLightValue(0.4375F).setUnlocalizedName("pictureFactory");
		pictureFactoryIdle = new BlockPictureFactory(
				config.ids.pictureFactoryIdleId, FactoryState.Idle)
				.setHardness(pictureFactoryActive.blockHardness)
				.setStepSound(pictureFactoryActive.stepSound)
				.setUnlocalizedName("pictureFactory")
				.setCreativeTab(CreativeTabs.tabDecorations);
		// Model factory.
		modelFactoryBurning = new BlockModelFactory(
				config.ids.modelFactoryBurningId, FactoryState.Burning)
				.setHardness(pictureFactoryBurning.blockHardness)
				.setStepSound(pictureFactoryBurning.stepSound)
				.setLightValue(0.875F).setUnlocalizedName("modelFactory");
		modelFactoryActive = new BlockModelFactory(
				config.ids.modelFactoryActiveId, FactoryState.Active)
				.setHardness(pictureFactoryActive.blockHardness)
				.setStepSound(pictureFactoryActive.stepSound)
				.setLightValue(0.4375F).setUnlocalizedName("modelFactory");
		modelFactoryIdle = new BlockModelFactory(config.ids.modelFactoryIdleId,
				FactoryState.Idle)
				.setHardness(pictureFactoryIdle.blockHardness)
				.setStepSound(pictureFactoryIdle.stepSound)
				.setUnlocalizedName("modelFactory")
				.setCreativeTab(CreativeTabs.tabDecorations);
		// Items.
		coloredDye = new ItemColoredDye(config.ids.coloredDyeId)
				.setUnlocalizedName("coloredDye");
		coloredBrush = new ItemColoredBrush(config.ids.coloredBrushId)
				.setUnlocalizedName("coloredBrush");
		// Register.
		LanguageRegistry.addName(coloredWool, "Colored Wool");
		GameRegistry.registerBlock(coloredWool, "coloredWool");
		GameRegistry.registerTileEntity(TileEntityColoredWool.class,
				"coloredWool");
		LanguageRegistry.addName(pictureFactoryBurning, "Picture Factory");
		GameRegistry.registerBlock(pictureFactoryBurning,
				"pictureFactoryBurning");
		GameRegistry.registerTileEntity(TileEntityPictureFactory.class,
				"pictureFactoryBurning");
		LanguageRegistry.addName(pictureFactoryActive, "Picture Factory");
		GameRegistry
				.registerBlock(pictureFactoryActive, "pictureFactoryActive");
		GameRegistry.registerTileEntity(TileEntityPictureFactory.class,
				"pictureFactoryActive");
		LanguageRegistry.addName(pictureFactoryIdle, "Picture Factory");
		GameRegistry.registerBlock(pictureFactoryIdle, "pictureFactoryIdle");
		GameRegistry.registerTileEntity(TileEntityPictureFactory.class,
				"pictureFactoryIdle");
		LanguageRegistry.addName(modelFactoryBurning, "Model Factory");
		GameRegistry.registerBlock(modelFactoryBurning, "modelFactoryBurning");
		GameRegistry.registerTileEntity(TileEntityModelFactory.class,
				"modelFactoryBurning");
		LanguageRegistry.addName(modelFactoryActive, "Model Factory");
		GameRegistry.registerBlock(modelFactoryActive, "modelFactoryActive");
		GameRegistry.registerTileEntity(TileEntityModelFactory.class,
				"modelFactoryActive");
		LanguageRegistry.addName(modelFactoryIdle, "Model Factory");
		GameRegistry.registerBlock(modelFactoryIdle, "modelFactoryIdle");
		GameRegistry.registerTileEntity(TileEntityModelFactory.class,
				"modelFactoryIdle");
		LanguageRegistry.addName(coloredDye, "Colored Dye");
		LanguageRegistry.addName(coloredBrush, "Colored Brush");
		// Craft.
		ItemStack cds = new ItemStack(coloredDye);
		ItemStack pls = new ItemStack(Block.planks);
		ItemStack cls = new ItemStack(Block.cloth);
		ItemStack iis = new ItemStack(Item.ingotIron);
		ItemStack igs = new ItemStack(Item.ingotGold);
		ItemStack gls = new ItemStack(Block.glass);
		ItemStack rds = new ItemStack(Item.redstone);
		GameRegistry.addShapelessRecipe(new ItemStack(coloredWool), cds, cls);
		GameRegistry.addShapelessRecipe(cds,
				new ItemStack(Item.dyePowder, 1, 1), new ItemStack(
						Item.dyePowder, 1, 2), new ItemStack(Item.dyePowder, 1,
						4));
		GameRegistry.addRecipe(new ItemStack(coloredBrush), "xxx", "yyy",
				" z ", 'x', cls, 'y', pls, 'z', new ItemStack(Item.stick));
		GameRegistry.addRecipe(new ItemStack(pictureFactoryIdle), "xyx", "zuz",
				"xzx", 'x', iis, 'y', gls, 'z', pls, 'u', rds);
		GameRegistry.addRecipe(new ItemStack(modelFactoryIdle), "xyx", "zuz",
				"xzx", 'x', igs, 'y', gls, 'z', pls, 'u', rds);
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		// Tick.
		TickRegistry.registerTickHandler(this, event.getSide());
		imageImport = new CopyOnWriteArrayList<ImageImport>();
	}

	/**
	 * Gets a local image.
	 * 
	 * @param path
	 *            its path.
	 * @return the image.
	 */
	public static BufferedImage getLocalImage(String path) {
		try {
			File f = new File(config.coloredWool.folder);
			if (f.isDirectory() && !f.exists()) {
				f.mkdirs();
			}
			return ImageIO.read(new File(f, path));
		} catch (Exception e) {
			FMLLog.log(MOD_ID, Level.SEVERE, e, "Image not found");
			return null;
		}
	}

	/**
	 * Adds an image import.
	 * 
	 * @param imageImport
	 *            the image import to add.
	 */
	public static void addImageImport(ImageImport imageImport) {
		ColoredWool.imageImport.add(imageImport);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if ((side == Side.SERVER && type.equals(EnumSet.of(TickType.SERVER)))
				|| (side == Side.CLIENT && type.equals(EnumSet
						.of(TickType.CLIENT)))) {
			for (ImageImport ii : imageImport) {
				ii.imageTick();
				if (ii.importFinished) {
					imageImport.remove(ii);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnumSet<TickType> ticks() {
		switch (side) {
		case CLIENT:
			return EnumSet.of(TickType.CLIENT);
		case SERVER:
			return EnumSet.of(TickType.SERVER);
		default:
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return null;
	}

}