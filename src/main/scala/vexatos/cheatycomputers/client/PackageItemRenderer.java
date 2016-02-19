package vexatos.cheatycomputers.client;

import li.cil.oc.client.KeyBindings;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vexatos.cheatycomputers.util.PackageUtil;

@SideOnly(Side.CLIENT)
public class PackageItemRenderer implements ItemMeshDefinition /*, IPerspectiveAwareModel, IFlexibleBakedModel*/ {

	public final ModelResourceLocation resourceLocation;

	public PackageItemRenderer(ModelResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	// Old

	/*private boolean recursion = false; // Prevent recursion

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
	}*/

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		if(KeyBindings.showExtendedTooltips()) {
			ItemStack contained = PackageUtil.getContainedFromPackage(stack);
			if(contained != null) {
				NBTTagCompound tag = stack.getTagCompound();
				if(tag != null) {
					int tier = tag.hasKey("t") ? tag.getInteger("t") + 1 : 1;
					return new ModelResourceLocation("opencomputers:case" + tier, "inventory");
				}
			}
		}
		return resourceLocation;
	}
}
