package basmat.testmod.tileentities.energy;

import basmat.testmod.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TETestEnergyGenerator extends TileEntity implements ITickable
{
	public ItemStackHandler handler = new ItemStackHandler(1);
	private EnergyStorage storage = new EnergyStorage(100000);
	public int energy = storage.getEnergyStored();
	public int capacity = storage.getMaxEnergyStored();
	public int maxExtract = 10000;
	public int maxRecieve = 10000;
	private String customName;
	public int cookTime;
	public boolean cooking;
	
	
	public void update() {
		if (!handler.getStackInSlot(0).isEmpty() && isItemFuel(handler.getStackInSlot(0)) && capacity > energy) {
			energy += getFuelValue(handler.getStackInSlot(0));
			cookTime++;
			if (cookTime == 25) {
				handler.getStackInSlot(0).shrink(1);
				cookTime = 0;
			}
		}
		
		if (capacity < energy) {
			energy = 100000;
		}
		
	}
	
	private boolean isItemFuel(ItemStack stack) {
		return getFuelValue(stack) > 0;
	}
	
	private int getFuelValue(ItemStack stack) {
		if(stack.getItem() == ModItems.testItem) return 320; //return rf/tick - reduce later 
		else return 0;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) return (T)this.storage;
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.handler;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) return true;
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
		
	}
	
	@Override	
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Inventory", this.handler.serializeNBT());
		compound.setInteger("CookTime", cookTime);
		compound.setInteger("GuiEnergy", this.energy);
		compound.setString("Name", getDisplayName().toString());
		compound.setInteger("Energy", this.energy);
		compound.setInteger("Capacity", this.capacity);
		compound.setInteger("MaxRecieve", this.maxRecieve);
		compound.setInteger("MaxExtract", this.maxExtract);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.handler.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.cookTime = compound.getInteger("CookTime");
		this.energy = compound.getInteger("GuiEnergy");
		this.customName = compound.getString("Name");
		this.energy = compound.getInteger("Energy");
		this.capacity = compound.getInteger("Capacity");
		this.maxRecieve = compound.getInteger("MaxRecieve");
		this.maxExtract = compound.getInteger("MaxExtract");
	}
	
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("container.test_generator");
	}
	
	public int getEnergyStored() {
		return this.energy;

	}
	
	public int getMaxEnergyStored() {
		return this.storage.getMaxEnergyStored();
	}
	
	public int getField(int id) {
		switch(id) {
		case 0:
			return this.energy;
		case 1:
			return this.cookTime;
		default:
			return 0;
		}
	}
	
	public void setField(int id, int value) {
		switch(id) {
		case 0:
			this.energy = value;
		case 1:
			this.cookTime = value;
		}
	}
	
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	
	
}
