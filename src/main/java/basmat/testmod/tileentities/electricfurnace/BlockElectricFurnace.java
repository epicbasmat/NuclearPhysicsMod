package basmat.testmod.tileentities.electricfurnace;

import java.util.Random;

import basmat.testmod.Main;
import basmat.testmod.blocks.BlockBase;
import basmat.testmod.init.ModBlocks;
import basmat.testmod.tileentities.blockcounter.TileEntityCounter;
import basmat.testmod.tileentities.generator.TETestEnergyGenerator;
import basmat.testmod.util.Reference;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockElectricFurnace extends BlockBase{

		public static final PropertyDirection FACING = BlockHorizontal.FACING;
		public static final PropertyBool BURNING = PropertyBool.create("burning");
		
		public BlockElectricFurnace(String name) 
		{
			super(name, Material.IRON);
			this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING, false));
		}
		
		@Override
		public Item getItemDropped(IBlockState state, Random rand, int fortune) 
		{
			return Item.getItemFromBlock(ModBlocks.electricfurnace);
		}
		
		@Override
		public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
		{
			return new ItemStack(ModBlocks.electricfurnace);
		}
		
		@Override
		public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
		{
			if(!worldIn.isRemote)
			{
				playerIn.openGui(Main.instance, Reference.GUI_ELECTRICFURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			
			return true;
		}
		
		@Override
		public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
		{
			if (!worldIn.isRemote) 
	        {
	            IBlockState north = worldIn.getBlockState(pos.north());
	            IBlockState south = worldIn.getBlockState(pos.south());
	            IBlockState west = worldIn.getBlockState(pos.west());
	            IBlockState east = worldIn.getBlockState(pos.east());
	            EnumFacing face = (EnumFacing)state.getValue(FACING);

	            if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) face = EnumFacing.SOUTH;
	            else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) face = EnumFacing.NORTH;
	            else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) face = EnumFacing.EAST;
	            else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) face = EnumFacing.WEST;
	            worldIn.setBlockState(pos, state.withProperty(FACING, face), 2);
	        }
		}
		
		public static void setState(boolean active, World worldIn, BlockPos pos) 
		{
			IBlockState state = worldIn.getBlockState(pos);
			TileEntity tileentity = worldIn.getTileEntity(pos);
			
			if(active) worldIn.setBlockState(pos, ModBlocks.electricfurnace.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(BURNING, true), 3);
			else worldIn.setBlockState(pos, ModBlocks.electricfurnace.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(BURNING, false), 3);
			
			if(tileentity != null) 
			{
				tileentity.validate();
				worldIn.setTileEntity(pos, tileentity);
			}
		}
		
		@Override
		public boolean hasTileEntity() 
		{
			return true;
		}
		
		@Override
		public boolean hasTileEntity(IBlockState state)
		{
			return true;
		}
		
		@Override
		public TileEntity createTileEntity(World world, IBlockState state)
		{
			return new TileEntityElectricFurnace();
		}
		
		@Override
		public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) 
		{
			return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		}
		
		@Override
		public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
		{
			worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		}
		
		@Override
		public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
		{
			TileEntityElectricFurnace tileentity = (TileEntityElectricFurnace)worldIn.getTileEntity(pos);
			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.handler.getStackInSlot(0)));
			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.handler.getStackInSlot(1)));
			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.handler.getStackInSlot(2)));
			super.breakBlock(worldIn, pos, state);
		}
		
		@Override
		public EnumBlockRenderType getRenderType(IBlockState state) 
		{
			return EnumBlockRenderType.MODEL;
		}
		
		@Override
		public IBlockState withRotation(IBlockState state, Rotation rot)
		{
			return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
		}
		
		@Override
		public IBlockState withMirror(IBlockState state, Mirror mirrorIn) 
		{
			return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
		}
		
		@Override
		protected BlockStateContainer createBlockState() 
		{
			return new BlockStateContainer(this, new IProperty[] {BURNING,FACING});
		}
		
		@Override
		public IBlockState getStateFromMeta(int meta) 
		{
			EnumFacing facing = EnumFacing.getFront(meta);
			if(facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
			return this.getDefaultState().withProperty(FACING, facing);
		}
		
		@Override
		public int getMetaFromState(IBlockState state) 
		{
			return ((EnumFacing)state.getValue(FACING)).getIndex();
		}
		
}