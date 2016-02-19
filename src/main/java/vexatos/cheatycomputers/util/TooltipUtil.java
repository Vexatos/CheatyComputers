package vexatos.cheatycomputers.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Vexatos
 */
public class TooltipUtil {

	private static final int maxWidth = 220;

	// Thanks, Sangar.
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public static void addShiftTooltip(ItemStack stack, List tooltip) {
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		final String key = stack.getUnlocalizedName() + ".tip";
		if(StringUtil.canTranslate(key)) {
			String tip = StringUtil.localize(key);
			if(!tip.equals(key)) {
				String[] lines = tip.split("\n");
				for(String line : lines) {
					List list = font.listFormattedStringToWidth(line, maxWidth);
					tooltip.addAll(list);
				}
			}
		}
	}
}
