package basmat.testmod.recipes;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Map.Entry;

import basmat.testmod.init.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ElectricFurnaceRecipes {
	private static final ElectricFurnaceRecipes INSTANCE = new ElectricFurnaceRecipes();
	private final Table<ItemStack, ItemStack, ItemStack> smeltingList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
	private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();
	
	public static ElectricFurnaceRecipes getInstance() {
		return INSTANCE;
	}
	
	private ElectricFurnaceRecipes() {
		addElectricFurnaceRecipes(new ItemStack(Blocks.ACACIA_FENCE), new ItemStack(Blocks.ACACIA_FENCE_GATE), new ItemStack(Blocks.ANVIL), 5.0F);
		addElectricFurnaceRecipes(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Blocks.NETHERRACK), new ItemStack(Blocks.DIAMOND_BLOCK), 5.0F);
	}

	public void addElectricFurnaceRecipes(ItemStack input1, ItemStack input2, ItemStack result, float experience) {
		if(getElectricFurnaceResult(input1, input2) != ItemStack.EMPTY) return;
		this.smeltingList.put(input1, input2, result);
		this.experienceList.put(result, Float.valueOf(experience));
		
	}
	
	public ItemStack getElectricFurnaceResult(ItemStack input1, ItemStack input2) 
	{
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.smeltingList.columnMap().entrySet()) 
		{
			if(this.compareItemStacks(input1, (ItemStack)entry.getKey()))
			{
				for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) 
				{
					if(this.compareItemStacks(input2, (ItemStack)ent.getKey())) 
					{
						return (ItemStack)ent.getValue();
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	
	public Table<ItemStack, ItemStack, ItemStack> getDualSmeltingList() {
		return this.smeltingList;
	}
	
	public float getElectricFurnaceExperience(ItemStack stack) {
		for (Entry<ItemStack, Float> entry : this.experienceList.entrySet()) {
			if (this.compareItemStacks(stack,  (ItemStack)entry.getKey())) {
				return ((Float)entry.getValue()).floatValue();
			}
		}
		return 0.0F;
	}
}
