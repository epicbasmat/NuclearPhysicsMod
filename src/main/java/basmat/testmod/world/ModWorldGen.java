package basmat.testmod.world;

import java.util.Random;

import basmat.testmod.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ModWorldGen implements IWorldGenerator{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		if (world.provider.getDimension() == 0) {
			generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}
	private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		generateOre(ModBlocks.copperore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, random.nextInt(7) + 4, 12);
		generateOre(ModBlocks.lithiumore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, random.nextInt(5) + 3, 12);
		generateOre(ModBlocks.uraniumore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, random.nextInt(5) + 3, 6);
		generateOre(ModBlocks.nickelore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, random.nextInt(3) + 2, 12);
		generateOre(ModBlocks.leadore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, random.nextInt(5) + 2, 12);
	}
	
	private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size, int chances) 
	{
		int deltaY = maxY - minY;
		
		for (int i = 0; i < chances; i++) 
		{
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));
			
			WorldGenMinable generator = new WorldGenMinable(ore, size);
			generator.generate(world, random, pos);
					
		}
	}
}