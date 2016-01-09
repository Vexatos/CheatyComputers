package vexatos.cheatycomputers.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import li.cil.oc.client.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import vexatos.cheatycomputers.ScalaProxy;

@SideOnly(Side.CLIENT)
public class PackageItemRenderer implements IItemRenderer {

	private static final RenderItem renderItem = new RenderItem();
	private static final Minecraft mc = Minecraft.getMinecraft();

	private static ItemStack getContainedFromPackage(ItemStack stack) {
		if(stack == null) {
			return null;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null || !tag.hasKey("t")) {
			return null;
		}
		return ScalaProxy.getCase(tag.getInteger("t")).createItemStack(1);
	}

	private boolean recursion = false; // Prevent recursion

	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
		if(!this.recursion && type == ItemRenderType.INVENTORY && KeyBindings.showExtendedTooltips()) {
			if(getContainedFromPackage(stack) != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		recursion = true;

		final ItemStack contained = getContainedFromPackage(stack);

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
		RenderHelper.enableGUIStandardItemLighting();
		renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), contained, 0, 0);
		RenderHelper.disableStandardItemLighting();
		GL11.glPopAttrib();

		recursion = false;
	}
}
