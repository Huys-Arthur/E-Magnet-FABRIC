package com.rangetuur.rfmagnet;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = RFMagnet.MOD_ID)
public class RFMagnetConfig implements ConfigData{

    @ConfigEntry.Category("magnets")
    @ConfigEntry.Gui.TransitiveObject
    @Comment("Magnets configuration options.")
    public Magnets magnets = new Magnets();

    public static class Magnets {
        @Comment("If false the magnet only work when holding in your hands else if true the magnets works when in inventory [DEFAULT: false]")
        public boolean magnet_always_works = false;
        @Comment("This value determines how big the capacity of the basic magnet is [DEFAULT: 10000]")
        public int capacity_basic_magnet = 10000;
        @Comment("This value determines how big the capacity of the advanced magnet is [DEFAULT: 30000]")
        public int capacity_advanced_magnet = 30000;
        @Comment("This value determines how big the range of the basic magnet is [DEFAULT: 6]")
        public int range_basic_magnet = 6;
        @Comment("This value determines how big the range of the advanced magnet is [DEFAULT: 8]")
        public int range_advanced_magnet = 8;
    }

    @ConfigEntry.Category("magnets")
    @ConfigEntry.Gui.TransitiveObject
    @Comment("Magnets configuration options.")
    public Blocks blocks = new Blocks();

    public static class Blocks {
        @Comment("If false the magnet jar is not disabled else if true the magnet jar is disabled [DEFAULT: false]")
        public boolean disable_magnet_jar = false;

        //Todo
        //@Comment("If false the magnet jar can not be disabled with redstone else if true the magnet jar can be disabled with redstone (when the magnet jar is disabled it can not attract items anymore but it can still charge)  [DEFAULT: true]")
        //public boolean disable_with_redstone_magnet_jar = true;

        @Comment("If false the magnet jar can not generate energy else if true the magnet jar can generate energy from lava: place lava on top of magnet jar and it will generate 8 EU/t when magnet is inside (this can be useful when you do not want to use another mod to generate energy)  [DEFAULT: false]")
        public boolean generate_energy_magnet_jar = false;
        @Comment("This value determines how match energy the magnet jar generates when there is lava above  [DEFAULT: 8]")
        public int generate_amount_magnet_jar = 8;
    }
}
