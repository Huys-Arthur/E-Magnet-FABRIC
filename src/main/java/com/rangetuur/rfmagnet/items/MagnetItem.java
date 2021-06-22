package com.rangetuur.rfmagnet.items;

import com.rangetuur.rfmagnet.RFMagnetConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

import java.util.List;

public class MagnetItem extends Item implements EnergyHolder, CustomDurabilityItem {

    private final int range;
    private final int maxEnergy;
    private final EnergyTier tier;

    public MagnetItem(Settings settings, int range, int maxEnergy, EnergyTier tier) {
        super(settings);
        this.range = range;
        this.maxEnergy = maxEnergy;
        this.tier = tier;
    }

    @Override
    public double getMaxStoredPower() {
        return maxEnergy;
    }

    @Override
    public EnergyTier getTier() {
        return tier;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (Energy.of(stack).getEnergy()>maxEnergy){
            Energy.of(stack).set(maxEnergy);
        }

        for (ItemStack s: entity.getItemsHand()){
            RFMagnetConfig config = AutoConfig.getConfigHolder(RFMagnetConfig.class).getConfig();
            if (s.getItem() instanceof MagnetItem || config.magnets.magnet_always_works) {
                attractItemsToPlayer(entity, s);
            }
        }
    }

    private void attractItemsToPlayer(Entity entity, ItemStack stack) {
        double x = entity.getX();
        double y = entity.getY() + 0.75;
        double z = entity.getZ();

        List<ItemEntity> items = entity.getEntityWorld().getEntitiesByType(EntityType.ITEM, new Box(x-range,y-range,z-range,x+range,y+range,z+range), EntityPredicates.VALID_ENTITY);

        for (ItemEntity item : items) {
            int energyForItem = item.getStack().getCount();
            if(Energy.of(stack).getEnergy()>=energyForItem) {
                item.setPickupDelay(0);
                item.updatePosition(x, y, z);
                Energy.of(stack).set(Energy.of(stack).getEnergy()-energyForItem);
            }
        }
    }

    public int getRange() {
        return range;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public double getDurabilityBarProgress(ItemStack stack) {
        EnergyHandler energy = Energy.of(stack);
        double stored = energy.getMaxStored() - energy.getEnergy();
        return stored / energy.getMaxStored();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean hasDurabilityBar(ItemStack stack) {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getDurabilityBarColor(ItemStack stack) {
        return CustomDurabilityItem.super.getDurabilityBarColor(stack);
    }
}
