package com.github.mrstop.stdemo.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLoader {

    public static Block grassBlock = new BlockGrassBlock();//实例化方块
    public static Block chromiteBlock = new BlockChromiteBlock();
    public static Block fluidMercuryBlock = new BlockFluidMercury();
    public static Block colorBlock = new BlockColorCrops();
    public static Block metalFurnaceActive = new BlockMetalFurnace(true);
    public static Block metalFurnaceInactive = new BlockMetalFurnace(false);
    public static Block redstoneFluxFurnaceActive = new BlockRedstoneFluxFurnace(true);
    public static Block getRedstoneFluxFurnaceInactive = new BlockRedstoneFluxFurnace(false);
    public static Block windmillBlock = new BlockWindmill();
    public static Block windmillGroundBlock = new BlockWindmillGround();
    public static Block quartzFurnaceActive = new BlockQuartzFurnace(true);
    public static Block quartzFurnaceInactive = new BlockQuartzFurnace(false);
    public static Block electrolyticMachine = new BlockElectrolyticMachine();

    public  BlockLoader(FMLPreInitializationEvent event) {
        //构造方法
        register(grassBlock,"grass_block");//注册草块
        register(chromiteBlock, "chromite_block");
        register(fluidMercuryBlock, "fluidMercury_block");
        register(colorBlock, "color_block");
        register(metalFurnaceActive, "metal_furnace_active");
        register(metalFurnaceInactive, "metal_furnace_inactive");
        register(redstoneFluxFurnaceActive, "redstoneflux_furnace_active");
        register(getRedstoneFluxFurnaceInactive, "redstoneflux_furnace_inactive");
        register(windmillBlock, "windmill_block");
        register(windmillGroundBlock, "windmill_ground_block");
        register(quartzFurnaceActive, "quartz_furnace_active");
        register(quartzFurnaceInactive, "quartz_furnace_inactive");
        register(electrolyticMachine, "electrolytic_machine");
    }

    private static void register(Block block,String name) {
        //注册方块的方法
        GameRegistry.registerBlock(block,name);
    }
}
