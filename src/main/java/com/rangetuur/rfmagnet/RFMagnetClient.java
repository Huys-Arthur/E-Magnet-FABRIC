package com.rangetuur.rfmagnet;

import com.rangetuur.rfmagnet.blocks.blockentities.renderers.MagnetJarBlockEntityRenderer;
import com.rangetuur.rfmagnet.registry.ModBlockEntityTypes;
import com.rangetuur.rfmagnet.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class RFMagnetClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MAGNET_JAR, RenderLayer.getTranslucent());

        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.MAGNET_JAR, MagnetJarBlockEntityRenderer::new);
    }
}
