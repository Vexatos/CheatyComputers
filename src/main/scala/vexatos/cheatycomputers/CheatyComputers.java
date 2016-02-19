package vexatos.cheatycomputers;

import li.cil.oc.api.CreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;
import vexatos.cheatycomputers.item.ItemPackage;
import vexatos.cheatycomputers.reference.Mods;

import java.util.ArrayList;

@Mod(modid = Mods.CC, name = Mods.CC_NAME, version = "@VERSION@", dependencies = "required-after:" + Mods.OC + "@[1.5.22,)")
public class CheatyComputers {

	@Instance
	public static CheatyComputers INSTANCE;

	@SidedProxy(modId = Mods.CC, serverSide = "vexatos.cheatycomputers.CommonProxy", clientSide = "vexatos.cheatycomputers.ClientProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tab;

	public static Configuration config;
	public static Logger log;

	public static ItemPackage item;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		log = e.getModLog();
		//MinecraftForge.EVENT_BUS.register(this);
		//FMLCommonHandler.instance().bus().register(this);
		config = new Configuration(e.getSuggestedConfigurationFile());
		//config.load();
		tab = CreativeTab.instance;
		item = new ItemPackage();
		GameRegistry.registerItem(item, "package");
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.registerRenderers();
	}

	public static ArrayList<ShapedOreRecipe> recipes = new ArrayList<ShapedOreRecipe>();
	public static ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();

	public static void addPackageRecipe(ItemStack result, Object other) {
		result.setItemDamage(itemList.size());
		recipes.add(new ShapedOreRecipe(result,
			"sss", "sds", "prp",
			's', other,
			'd', "gemDiamond",
			'p', Items.paper,
			'r', "dustRedstone"
		));

		itemList.add(result);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		ScalaProxy.createRecipes();
		for(ShapedOreRecipe recipe : recipes) {
			GameRegistry.addRecipe(recipe);
		}
		//config.save();
	}
}
