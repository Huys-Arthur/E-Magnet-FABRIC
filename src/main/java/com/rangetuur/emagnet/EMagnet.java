package com.rangetuur.emagnet;

import com.rangetuur.emagnet.registry.ModBlockEntityTypes;
import com.rangetuur.emagnet.registry.ModBlocks;
import com.rangetuur.emagnet.registry.ModItems;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class EMagnet implements ModInitializer {

    public static final String MOD_ID ="emagnet";

    @Override
    public void onInitialize() {
        AutoConfig.register(EMagnetConfig.class, JanksonConfigSerializer::new);

        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlockEntityTypes.registerBlockEntityTypes();
    }
}
