/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 23, 2015, 11:44:35 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockGaiaHead extends BlockSkull {

	public BlockGaiaHead() {
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.GAIA_HEAD));
		setUnlocalizedName(LibBlockNames.GAIA_HEAD);
		setHardness(1.0F);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ModItems.gaiaHead);
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess p_149749_1_, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        
        if(!state.getValue(NODROP)) {
            ItemStack itemstack = new ItemStack(ModItems.gaiaHead, 1);
            TileEntitySkull tileentityskull = (TileEntitySkull)p_149749_1_.getTileEntity(pos);

            if(tileentityskull == null) 
            	return ret;

            ret.add(itemstack);
        }
        return ret;
    }
	
	@Override
    public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return ModItems.gaiaHead;
    }

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileGaiaHead();
	}
}
