package basmat.testmod.tileentities.blockcounter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCounter extends TileEntity{
	
	private int count;
	
	@Override
	
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		compound.setInteger("count", count);
		return super.writeToNBT(compound);
	}
	
	public void readFromNBT(NBTTagCompound compound) {
		count = compound.getInteger("count");
		super.readFromNBT(compound);
		
	}
	public int getCount() {
		return count;
	}
	public void incrementCount() {
		count++;
		markDirty();
	}
	public void decrementCount() {
		count--;
		markDirty();
	}

}
