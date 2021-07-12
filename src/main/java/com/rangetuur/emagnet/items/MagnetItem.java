package com.rangetuur.emagnet.items;

import com.rangetuur.emagnet.EMagnetConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

import java.text.NumberFormat;
import java.util.List;

public class MagnetItem extends Item implements EnergyHolder {

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
            EMagnetConfig config = AutoConfig.getConfigHolder(EMagnetConfig.class).getConfig();
            if (s.getItem() instanceof MagnetItem || config.magnets.magnet_always_works) {
                attractItemsToPlayer(entity, s);
            }
        }
    }

    private void attractItemsToPlayer(Entity entity, ItemStack stack) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        List<ItemEntity> items = entity.getEntityWorld().getEntitiesByType(EntityType.ITEM, new Box(x-range,y-range,z-range,x+range,y+range,z+range), EntityPredicates.VALID_ENTITY);

        for (ItemEntity item : items) {
            int energyForItem = item.getStack().getCount();
            if(Energy.of(stack).getEnergy()>=energyForItem) {
                item.setPickupDelay(0);

                Vec3d itemVector = new Vec3d(item.getX(), item.getY(), item.getZ());
                Vec3d playerVector = new Vec3d(x, y+0.75, z);
                item.move(null, playerVector.subtract(itemVector).multiply(0.5));

                Energy.of(stack).set(Energy.of(stack).getEnergy()-energyForItem);
            }
        }
    }

    public int getRange() {
        return range;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xFF800600;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        EnergyHandler energy = Energy.of(stack);
        return (int) (energy.getEnergy() / energy.getMaxStored() * 13);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText(String.format("%s/%s EU", Energy.of(stack).getEnergy(), Energy.of(stack).getMaxStored())));
    }
}