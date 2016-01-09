package vexatos.cheatycomputers.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import li.cil.oc.api.Items;
import li.cil.oc.api.Network;
import li.cil.oc.api.detail.ItemInfo;
import li.cil.oc.common.InventorySlots;
import li.cil.oc.common.tileentity.traits.Rotatable;
import li.cil.oc.util.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;
import scala.Option;
import vexatos.cheatycomputers.CheatyComputers;
import vexatos.cheatycomputers.ScalaProxy;
import vexatos.cheatycomputers.util.TooltipUtil;

import java.util.List;

/**
 * @author Vexatos
 */
public class ItemPackage extends Item {

	public ItemPackage() {
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CheatyComputers.tab);
		this.setUnlocalizedName("cheatycomputers.package");
		this.setTextureName("cheatycomputers:package");
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(stack != null && stack.getItem() instanceof ItemPackage && player.isSneaking() && !world.isRemote) {
			NBTTagCompound tag = stack.getTagCompound();
			if(tag == null || !tag.hasKey("t")) {
				player.addChatMessage(new ChatComponentTranslation("chat.cheatycomputers.invalid"));
				return true;
			}

			int tier = tag.getInteger("t");

			final int caseX, caseY, caseZ;

			if(world.getBlock(x, y, z).isReplaceable(world, x, y, z)) {
				caseX = x;
				caseY = y;
				caseZ = z;
			} else {
				ForgeDirection dir = ForgeDirection.getOrientation(side);
				caseX = x + dir.offsetX;
				caseY = y + dir.offsetY;
				caseZ = z + dir.offsetZ;
			}

			if(!world.getBlock(caseX, caseY, caseZ).isReplaceable(world, caseX, caseY, caseZ)) {
				player.addChatMessage(new ChatComponentTranslation("chat.cheatycomputers.obstructed"));
				return true;
			}
			Block block = ScalaProxy.getCase(tier).block();
			world.setBlock(caseX, caseY, caseZ, block);
			world.playSoundEffect((double) ((float) caseX + 0.5F), (double) ((float) caseY + 0.5F), (double) ((float) caseZ + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

			TileEntity tile = world.getTileEntity(caseX, caseY, caseZ);
			if(tile instanceof Rotatable) {
				ScalaProxy.setFacing(((Rotatable) tile), player);
			}

			Network.joinOrCreateNetwork(world.getTileEntity(caseX, caseY, caseZ));

			InventorySlots.InventorySlot[] slots = InventorySlots.computer()[tier];
			for(InventorySlots.InventorySlot slot : slots) {
				//NBTTagList list = tag.getTagList(slot.slot(), NBT.TAG_COMPOUND);
				NBTTagList list = tag.getTagList(slot.slot(), NBT.TAG_STRING);
				int count = ScalaProxy.countSlots(slots, slot.slot());
				for(int i = 0; i < count; i++) {
					//NBTTagCompound data = list.getCompoundTagAt(i);
					//ItemStack toAdd = ItemStack.loadItemStackFromNBT(data);
					ItemInfo info = Items.get(list.getStringTagAt(i));
					if(info != null) {
						ItemStack toAdd = info.createItemStack(1);
						if(toAdd != null) {
							ScalaProxy.insertIntoInventoryAt(toAdd.copy(), new BlockPosition(caseX, caseY, caseZ, Option.apply(world)));
							//InventoryUtils.insertIntoInventoryAt(toAdd.copy(),
							//	new BlockPosition(x, y, z, Option.apply(world)), Option.<ForgeDirection>empty(), 64, false);
						}
					}
				}
			}
			if(!player.capabilities.isCreativeMode) {
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}
			return true;
		}

		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(ItemStack stack : CheatyComputers.itemList) {
			list.add(stack.copy());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		TooltipUtil.addShiftTooltip(stack, list);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(!stack.hasTagCompound()) {
			return super.getItemStackDisplayName(stack);
		}
		NBTTagCompound tag = stack.getTagCompound();
		int tier = tag.getInteger("t");
		return StatCollector.translateToLocalFormatted("item.cheatycomputers.package.set", StatCollector.translateToLocal("set.cheatycomputers.case" + (tier + 1)));
	}
}
