package com.github.mrstop.stdemo.block;

import com.github.mrstop.stdemo.STDemo;
import com.github.mrstop.stdemo.core.util.InventoryHelper;
import com.github.mrstop.stdemo.creativetab.CreativeTabsLoader;
import com.github.mrstop.stdemo.tileentity.TileEntityMachineRedstoneFluxFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class BlockMachineRedstoneFluxFurnace extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon top;
    @SideOnly(Side.CLIENT)
    private IIcon front;

    private static boolean isBurning;
    private final boolean isBurningFlag;

    public BlockMachineRedstoneFluxFurnace(boolean isActive) {
        super(Material.iron);
        this.setUnlocalizedName("machineRedstoneFluxFurnace");
        this.setHardness(0.8F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 1);
        this.isBurningFlag = isActive;
        if (isActive) {
            this.setLightLevel(5.0F);
        }
        else {
            this.setCreativeTab(CreativeTabsLoader.tabSTDemo) ;
        }
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(BlockLoader.blockMachineRedstoneFluxFurnaceInactive);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        this.onRedstoneFluxFurnaceAdded(world, x, y, z);
    }
    //功能不明
    private void onRedstoneFluxFurnaceAdded(World world, int blockX, int blockY, int blockZ) {
        if (!world.isRemote) {
            //判断是否为客户端
            Block block = world.getBlock(blockX, blockY, blockZ - 1);                                      //获得金属熔炉北面的方块
            Block block1 = world.getBlock(blockX, blockY, blockZ + 1);                                     //获得金属熔炉南面的方块
            Block block2 = world.getBlock(blockX - 1, blockY, blockZ);                                     //获得金属熔炉西面的方块
            Block block3 = world.getBlock(blockX + 1, blockY, blockZ);                                     //获得金属熔炉东面的方块
            byte byte0 = 3;

            if (block.isFullBlock() && !block1.isFullBlock()) {
                byte0 = 3;
            }

            if (block1.isFullBlock() && !block.isFullBlock()) {
                byte0 = 2;
            }

            if (block2.isFullBlock() && !block3.isFullBlock()) {
                byte0 = 5;
            }
            if (block3.isFullBlock() && !block2.isFullBlock()) {
                byte0 = 4;
            }
            world.setBlockMetadataWithNotify(blockX, blockY, blockZ, byte0, 2);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if (worldIn.isRemote) {
            return true;
        }
        else {
            TileEntityMachineRedstoneFluxFurnace tileEntityMachineRedstoneFluxFurnace = (TileEntityMachineRedstoneFluxFurnace) worldIn.getTileEntity(x, y, z);
            if (tileEntityMachineRedstoneFluxFurnace != null) {
                player.openGui(STDemo.instance, STDemo.GUIIDMachineRedstoneFluxFurnace, worldIn, x, y, z);
            }
            return true;
        }
    }

    public static void updateRedstoneFluxFurnace(boolean isBurningFlag, World world, int blockX, int blockY, int blockZ) {
        int metadata = world.getBlockMetadata(blockX, blockY, blockZ);
        TileEntity tileEntity = world.getTileEntity(blockX, blockY, blockZ);
        //isBurning = true;
        if (isBurningFlag) {
            world.setBlock(blockX, blockY, blockZ, BlockLoader.blockMachineRedstoneFluxFurnaceActive);
        }
        else {
           world.setBlock(blockX, blockY, blockZ, BlockLoader.blockMachineRedstoneFluxFurnaceInactive);
       }
       //isBurning = false;
        world.setBlockMetadataWithNotify(blockX, blockY, blockZ, metadata, 2);
        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(blockX, blockY ,blockZ, tileEntity);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMachineRedstoneFluxFurnace();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        int sideFlag = MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (sideFlag == 0) {
            worldIn.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        if (sideFlag == 1) {
            worldIn.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        if (sideFlag == 2) {
            worldIn.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }
        if (sideFlag == 3) {
            worldIn.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
        if (itemStack.hasDisplayName()) {
            ((TileEntityMachineRedstoneFluxFurnace) worldIn.getTileEntity(x, y, z)).setCustomInventoryName(itemStack.getDisplayName());
        }
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        TileEntityMachineRedstoneFluxFurnace tileEntityMachineRedstoneFluxFurnace = (TileEntityMachineRedstoneFluxFurnace) worldIn.getTileEntity(x, y, z);
        if (tileEntityMachineRedstoneFluxFurnace == null){
            super.breakBlock(worldIn, x, y, z, blockBroken, meta);
            return;
        }
        InventoryHelper.dropInventoryItems(worldIn, tileEntityMachineRedstoneFluxFurnace, x, y, z);
//        worldIn.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
//        if (willHarvest){
//            return true;
//        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return super.getDrops(world, x, y, z, metadata, fortune);
//        TileEntity tileEntity = world.getTileEntity(x, y, z);
//        ArrayList<ItemStack> itemStacks = new ArrayList<>();
//        if (tileEntity instanceof TileEntityMachineRedstoneFluxFurnace){
//            TileEntityMachineRedstoneFluxFurnace tileEntityMachineRedstoneFluxFurnace = (TileEntityMachineRedstoneFluxFurnace)tileEntity;
//            NBTTagCompound compound = new NBTTagCompound();
//            tileEntityMachineRedstoneFluxFurnace.writeToNBT(compound);
//            ItemStack itemStack = new ItemStack(world.getBlock(x, y, z), 1, metadata);
//            itemStack.setTagCompound(compound);
//            itemStacks.add(itemStack);
//        }
//        return itemStacks;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(worldIn, player, x, y, z, meta);
        worldIn.setBlockToAir(x, y, z);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    //获得比较器输入
    @Override
    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side) {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1){
            return this.top;
        }
        else {
            if (meta != 0){
                if (side == meta){
                    return this.front;
                }
                else {
                    return this.blockIcon;
                }
            }
            else {
                if (side == 3){
                    return this.front;
                }
                if (side == 4){
                    return this.blockIcon;
                }
            }
            return this.blockIcon;
        }
        //如果meta为0且side为1(顶面)返回top
        //如果meta为0且side为3(南面)返回front
        //如果meta为0且side不等于3(南面)返回blockIcon
        //如果meta不等于0且side等于1(顶面)返回top
        //如果meta不等于0且side不等于meta返回blockIcon
        //如果meta不等于0且side等于meta返回front
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("stdemo:machine_redstoneflux_furnace_side");
        this.front = iconRegister.registerIcon(this.isBurningFlag ? "stdemo:machine_redstoneflux_furnace_active" : "stdemo:machine_redstoneflux_furnace_inactive");
        this.top = iconRegister.registerIcon("stdemo:machine_redstoneflux_furnace_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z) {
        return Item.getItemFromBlock(BlockLoader.blockMachineRedstoneFluxFurnaceInactive);
    }
}
