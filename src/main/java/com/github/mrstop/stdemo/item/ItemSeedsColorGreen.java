package com.github.mrstop.stdemo.item;

import com.github.mrstop.stdemo.STDemo;
import com.github.mrstop.stdemo.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemSeedsColorGreen extends ItemSeedsColor {
    public ItemSeedsColorGreen() {
        super(BlockLoader.blockColorFlowerGreen, "seedsColorGreen");
    }

    @Override
    public void registerIcons(IIconRegister iIconRegister) {
        this.itemIcon = iIconRegister.registerIcon(STDemo.MOD_DOMAIN + "seeds_color_green");
    }
}
