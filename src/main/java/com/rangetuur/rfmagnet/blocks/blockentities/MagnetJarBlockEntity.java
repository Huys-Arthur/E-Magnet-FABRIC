package com.rangetuur.rfmagnet.blocks.blockentities;

import com.rangetuur.rfmagnet.ImplementedInventory;
import com.rangetuur.rfmagnet.RFMagnetConfig;
import com.rangetuur.rfmagnet.items.MagnetItem;
import com.rangetuur.rfmagnet.registry.ModBlockEntityTypes;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.*;

import java.util.List;

public class MagnetJarBlockEntity extends BlockEntity implements ImplementedInventory, SidedInventory, BlockEntityClientSerializable, EnergyStorage {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public MagnetJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.MAGNET_JAR, pos, state);
    }


    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag,items);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        Inventories.writeNbt(tag,items);
        return super.writeNbt(tag);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        Inventories.readNbt(tag,items);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        Inventories.writeNbt(tag,items);
        return tag;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return stack.getItem() instanceof EnergyHolder;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot==1;
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        RFMagnetConfig config = AutoConfig.getConfigHolder(RFMagnetConfig.class).getConfig();
        MagnetJarBlockEntity e = (MagnetJarBlockEntity) world.getBlockEntity(pos);

        if (e!=null) {
            if(e.getStack(0)!=ItemStack.EMPTY && Energy.of(e.getStack(0)).getEnergy()!=Energy.of(e.getStack(0)).getMaxStored()){
                BlockEntity entityUp = world.getBlockEntity(e.getPos().up());
                if(!world.getEntitiesByType(EntityType.LIGHTNING_BOLT,new Box(pos.getX(), pos.getY()+1, pos.getZ(), pos.getX()+1, pos.getY()+3, pos.getZ()+1) , EntityPredicates.VALID_ENTITY).isEmpty()){
                    Energy.of(e.getStack(0)).set(Energy.of(e.getStack(0)).getMaxStored());
                    e.markDirty();
                }
                else if (entityUp!=null){
                    Energy.of(entityUp).side(EnergySide.DOWN).into(Energy.of(e.getStack(0))).move();
                    e.markDirty();
                }
                if(e.getStack(0).getItem() instanceof MagnetItem){
                    if(config.blocks.disable_magnet_jar_with_redstone){
                        if(!world.isReceivingRedstonePower(pos)){
                            e.attractItemsAroundBlock(pos, e.getStack(0));
                        }
                    }
                    else{
                        e.attractItemsAroundBlock(pos, e.getStack(0));
                    }
                }
            }
            if (e.getStack(1).isEmpty()) {
                e.putItemAroundBlockInInventory();
            }
        }
    }

    private void attractItemsAroundBlock(BlockPos pos, ItemStack stack) {
        int range = ((MagnetItem) stack.getItem()).getRange();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if(world!=null){
            List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, new Box(x-range,y-range,z-range,x+1+range,y+1+range,z+1+range), EntityPredicates.VALID_ENTITY);
            List<ItemEntity> itemsInBlock = world.getEntitiesByType(EntityType.ITEM, new Box(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+1,pos.getZ()+1), EntityPredicates.VALID_ENTITY);

            for (ItemEntity item : items) {
                if (!itemsInBlock.contains(item)){
                    int energyForItem = item.getStack().getCount();
                    if(Energy.of(stack).getEnergy()>=energyForItem) {

                        Vec3d itemVector = new Vec3d(item.getX(), item.getY(), item.getZ());
                        Vec3d blockVector = new Vec3d(x+0.5, y+0.5, z+0.5);
                        item.move(null, blockVector.subtract(itemVector).multiply(0.5));

                        Energy.of(stack).extract(energyForItem);
                        markDirty();
                    }
                }
            }
        }
    }

    private void putItemAroundBlockInInventory() {
        if (world!=null){
            List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, new Box(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+1,pos.getZ()+1), EntityPredicates.VALID_ENTITY);
            if (!items.isEmpty()){
                ItemEntity item = items.get(0);

                setStack(1, item.getStack().copy());
                item.remove(Entity.RemovalReason.DISCARDED);
                markDirty();
            }
        }
    }

    @Override
    public double getStored(EnergySide face) {
        return 0;
    }

    @Override
    public void setStored(double amount) {

    }

    @Override
    public double getMaxStoredPower() {
        return 0;
    }


    @Override
    public EnergyTier getTier() {
        return EnergyTier.MICRO;
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return 0;
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return 0;
    }


}
