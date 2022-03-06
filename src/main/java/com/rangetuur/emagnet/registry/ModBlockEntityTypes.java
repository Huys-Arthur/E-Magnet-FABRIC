package com.rangetuur.emagnet.registry;

import com.rangetuur.emagnet.EMagnet;
import com.rangetuur.emagnet.blocks.blockentities.MagnetJarBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntityTypes {

    public static BlockEntityType<MagnetJarBlockEntity> MAGNET_JAR = FabricBlockEntityTypeBuilder.create(MagnetJarBlockEntity::new, ModBlocks.MAGNET_JAR).build(null);

    public static void registerBlockEntityTypes() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(EMagnet.MOD_ID, "magnet_jar"), MAGNET_JAR);
    }
}
