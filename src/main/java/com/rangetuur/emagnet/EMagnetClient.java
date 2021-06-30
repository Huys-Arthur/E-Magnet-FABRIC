package com.rangetuur.emagnet;

import com.rangetuur.emagnet.blocks.blockentities.renderers.MagnetJarBlockEntityRenderer;
import com.rangetuur.emagnet.registry.ModBlockEntityTypes;
import com.rangetuur.emagnet.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class EMagnetClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MAGNET_JAR, RenderLayer.getTranslucent());

        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityTypes.MAGNET_JAR, MagnetJarBlockEntityRenderer::new);
    }
}
