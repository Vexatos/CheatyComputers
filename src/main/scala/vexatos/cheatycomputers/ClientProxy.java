package vexatos.cheatycomputers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import vexatos.cheatycomputers.client.PackageItemRenderer;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		CheatyComputers.item.renderer = new PackageItemRenderer(new ModelResourceLocation("cheatycomputers:package", "inventory"));
		ModelLoader.setCustomModelResourceLocation(CheatyComputers.item, 0, CheatyComputers.item.renderer.resourceLocation);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(CheatyComputers.item, CheatyComputers.item.renderer);
	}
}
