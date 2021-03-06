package com.rangetuur.emagnet.registry;

import com.rangetuur.emagnet.EMagnet;
import com.rangetuur.emagnet.EMagnetConfig;
import com.rangetuur.emagnet.blocks.MagnetJarBlock;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModBlocks {
    private static EMagnetConfig config = AutoConfig.getConfigHolder(EMagnetConfig.class).getConfig();

    public static final Block MAGNET_JAR = new MagnetJarBlock(FabricBlockSettings.of(Material.GLASS).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().strength(50.0f, 500.0f).sounds(BlockSoundGroup.GLASS).luminance(10).nonOpaque());

    public static void registerBlocks() {
        if (!config.blocks.disable_magnet_jar){
            Registry.register(Registry.BLOCK, new Identifier(EMagnet.MOD_ID, "magnet_jar"), MAGNET_JAR);
        }
    }
}
