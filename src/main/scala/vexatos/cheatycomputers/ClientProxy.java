package vexatos.cheatycomputers;

import net.minecraftforge.client.MinecraftForgeClient;
import vexatos.cheatycomputers.client.PackageItemRenderer;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		MinecraftForgeClient.registerItemRenderer(CheatyComputers.item, new PackageItemRenderer());
	}
}
