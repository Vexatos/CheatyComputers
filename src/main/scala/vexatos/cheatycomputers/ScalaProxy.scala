package vexatos.cheatycomputers

import li.cil.oc.Constants.{BlockName, ItemName}
import li.cil.oc.api
import li.cil.oc.api.detail.ItemInfo
import li.cil.oc.common.InventorySlots.InventorySlot
import li.cil.oc.common.tileentity.traits.Rotatable
import li.cil.oc.common.{InventorySlots, Slot, Tier}
import li.cil.oc.util.{BlockPosition, InventoryUtils}
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}
import net.minecraft.util.EnumFacing

/**
  * @author Vexatos
  */
object ScalaProxy {

  //private implicit def toItemStack(s: String): Option[ItemStack] = Option(api.Items.get(s)).map(_.createItemStack(1))
  private implicit def toItemStack(s: String): (Int => Option[String]) = index => Option(s)

  private implicit def to2(s: String): Option[String] = Option(s)

  //private implicit def toSimpleFunction(s: String): (Int => Option[ItemStack]) = index => s

  def createRecipes() = {
    addPackageRecipe(createPackage(Tier.One, {
      case Slot.CPU => ItemName.CPUTier1
      case Slot.HDD => ItemName.HDDTier1
      case Slot.EEPROM => ItemName.LuaBios
      case Slot.Memory => ItemName.RAMTier2
      case Slot.Card => {
        case 0 => ItemName.GraphicsCardTier1
        case 1 => ItemName.RedstoneCardTier1
        case 2 => ItemName.NetworkCard
        case _ => ""
      }
      case _ => ""
    }
    ), "stone")

    addPackageRecipe(createPackage(Tier.Two, {
      case Slot.CPU => ItemName.APUTier1
      case Slot.HDD => ItemName.HDDTier2
      case Slot.EEPROM => ItemName.LuaBios
      case Slot.Memory => ItemName.RAMTier4
      case Slot.Card => {
        case 0 => ItemName.RedstoneCardTier2
        case 1 => ItemName.NetworkCard
        case _ => ""
      }
      case _ => ""
    }
    ), "ingotIron")

    addPackageRecipe(createPackage(Tier.Three, {
      case Slot.CPU => ItemName.CPUTier3
      case Slot.HDD => ItemName.HDDTier3
      case Slot.EEPROM => ItemName.LuaBios
      case Slot.Memory => ItemName.RAMTier6
      case Slot.Floppy => ItemName.OpenOS
      case Slot.Card => {
        case 0 => ItemName.GraphicsCardTier3
        case 1 => ItemName.RedstoneCardTier2
        case 2 => ItemName.NetworkCard
        case _ => ""
      }
      case _ => ""
    }
    ), "ingotGold")
  }


  def addPackageRecipe(result: ItemStack, other: AnyRef) = {
    CheatyComputers.addPackageRecipe(result, other)
  }

  def countSlots(s: Array[InventorySlot], slot: String): Int = s.count(otherslot => otherslot.slot.equals(slot))

  def getCase(tier: Int): ItemInfo = api.Items.get(BlockName.Case(tier))

  def setFacing(rotatable: Rotatable, entity: Entity) = {
    rotatable.setFromEntityPitchAndYaw(entity)
    if (!rotatable.validFacings.contains(rotatable.pitch)) {
      rotatable.pitch = rotatable.validFacings.headOption.getOrElse(EnumFacing.NORTH)
    }
    rotatable.invertRotation()
  }

  def insertIntoInventoryAt(stack: ItemStack, pos: BlockPosition) = InventoryUtils.insertIntoInventoryAt(stack, pos)

  //def registerRecipes() = recipes.foreach(GameRegistry.addRecipe(_))

  def createPackage(tier: Int, f: (String => (Int => Option[String]))): ItemStack = {
    val stack = new ItemStack(CheatyComputers.item, 1, 0)
    val tag = new NBTTagCompound
    tag.setInteger("t", tier)
    val slots = InventorySlots.computer(tier)
    slots.groupBy(_.slot).keys.foreach {
      slot => f(slot) match {
        case g if g != null =>
          val list = new NBTTagList()
          for (index <- 0 until countSlots(slots, slot)) {
            g(index) match {
              case Some(content) =>
                //list.appendTag(content.writeToNBT(new NBTTagCompound))
                list.appendTag(new NBTTagString(content))
              case _ => // NO-OP
            }
          }
          tag.setTag(slot, list)
        case _ => // NO-OP
      }
    }

    stack.setTagCompound(tag)
    stack
  }

}
