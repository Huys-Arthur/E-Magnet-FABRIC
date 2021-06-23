package com.rangetuur.rfmagnet;

import com.rangetuur.rfmagnet.registry.ModBlockEntityTypes;
import com.rangetuur.rfmagnet.registry.ModBlocks;
import com.rangetuur.rfmagnet.registry.ModItems;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class RFMagnet implements ModInitializer {

    public static final String MOD_ID ="rfmagnet";

    @Override
    public void onInitialize() {
        AutoConfig.register(RFMagnetConfig.class, JanksonConfigSerializer::new);

        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlockEntityTypes.registerBlockEntityTypes();
    }
}
