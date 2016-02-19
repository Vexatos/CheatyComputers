package vexatos.cheatycomputers.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vexatos.cheatycomputers.ScalaProxy;

/**
 * @author Vexatos
 */
public class PackageUtil {

	public static ItemStack getContainedFromPackage(ItemStack stack) {
		if(stack == null) {
			return null;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null || !tag.hasKey("t")) {
			return null;
		}
		return ScalaProxy.getCase(tag.getInteger("t")).createItemStack(1);
	}
}
