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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleBatteryItem;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagnetItem extends Item implements SimpleBatteryItem {

    private final int range;
    private final long cap;
    private final long max_io;

    public MagnetItem(Settings settings, int range, int cap, int max_io) {
        super(settings);
        this.range = range;
        this.cap = cap;
        this.max_io = max_io;
    }

    @Override
    public long getEnergyCapacity() {
        return cap;
    }

    @Override
    public long getEnergyMaxInput() {
        return max_io;
    }

    @Override
    public long getEnergyMaxOutput() {
        return max_io;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (getStoredEnergy(stack) > getEnergyCapacity()) {
            setStoredEnergy(stack, getEnergyCapacity());
        }

        for (ItemStack s : entity.getItemsHand()) {
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

        List<ItemEntity> items = entity.getEntityWorld().getEntitiesByType(EntityType.ITEM,
                new Box(x - range, y - range, z - range, x + range, y + range, z + range),
                EntityPredicates.VALID_ENTITY);

        for (ItemEntity item : items) {
            int ForItem = item.getStack().getCount();
            if (getStoredEnergy(stack) >= ForItem) {
                item.setPickupDelay(0);

                Vec3d itemVector = new Vec3d(item.getX(), item.getY(), item.getZ());
                Vec3d playerVector = new Vec3d(x, y + 0.75, z);
                item.move(null, playerVector.subtract(itemVector).multiply(0.5));

                setStoredEnergy(stack, getStoredEnergy(stack) - ForItem);
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
        return (int) ((float) getStoredEnergy(stack) / getEnergyCapacity() * 13);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText(String.format("%s/%s EU", getStoredEnergy(stack), getEnergyCapacity())));
    }
}
