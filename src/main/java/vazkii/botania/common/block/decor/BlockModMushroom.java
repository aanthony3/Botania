/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 23, 2015, 4:24:08 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;
import java.util.Random;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockModMushroom extends BlockMushroom implements IInfusionStabiliser, IHornHarvestable, ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.3, 0, 0.3, 0.8, 1, 0.8);
	private int originalLight;

	public BlockModMushroom() {
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.MUSHROOM));
		GameRegistry.register(new ItemBlockWithMetadataAndName(this), getRegistryName());
		setUnlocalizedName(LibBlockNames.MUSHROOM);
		setLightLevel(0.2F);
		setHardness(0F);
		setSoundType(SoundType.PLANT);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= EnumDyeColor.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public void updateTick(World p_149674_1_, BlockPos pos, IBlockState state, Random p_149674_5_) {
		// NO-OP, to prevent spreading
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		if(pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState state = world.getBlockState(pos.down());
			Block block = state.getBlock();
			return block == Blocks.mycelium || block == Blocks.dirt && state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL || block.canSustainPlant(state, world, pos.down(), EnumFacing.UP, this);
		}

		return false;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public Block setLightLevel(float p_149715_1_) {
		originalLight = (int) (p_149715_1_ * 15);
		return super.setLightLevel(p_149715_1_);
	}

	@Override
	@Optional.Method(modid = "easycoloredlights")
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);

		return ColoredLightHelper.getPackedColor(world.getBlockState(pos).getValue(BotaniaStateProps.COLOR), originalLight);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
		int hex = state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = (hex & 0xFF);

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency * 0.25F)
			Botania.proxy.sparkleFX(par1World, pos.getX() + 0.3 + par5Random.nextFloat() * 0.5, pos.getY() + 0.5 + par5Random.nextFloat() * 0.5, pos.getZ() + 0.3 + par5Random.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, par5Random.nextFloat(), 5);
	}

	@Override
	public boolean canStabaliseInfusion(World world, BlockPos pos) {
		return ConfigHandler.enableThaumcraftStablizers;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.mushrooms;
	}

	@Override
	public boolean canHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public boolean hasSpecialHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public void harvestByHorn(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		// NO-OP
	}

}
