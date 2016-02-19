package vexatos.cheatycomputers.reference;

import net.minecraftforge.fml.common.Loader;

/**
 * @author Vexatos
 */
public class Mods {

	//The mod itself
	public static final String
		CC = "CheatyComputers",
		CC_NAME = "Cheaty Computers";

	public static final String
		OC = "OpenComputers";

	public static boolean isLoaded(String name) {
		return Loader.isModLoaded(name);
	}
}
