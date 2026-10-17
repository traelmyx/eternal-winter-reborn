package ichttt.mods.eternalwinter.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public final class WinterTests {
    private WinterTests() {
    }

    private static Biome biome(GameTestHelper helper, ResourceKey<Biome> key) {
        return biome(helper.getLevel().registryAccess(), key);
    }

    public static Biome.Precipitation precipitation(Biome biome, net.minecraft.core.BlockPos pos) {
        //? if >=1.21.2 {
        /*return biome.getPrecipitationAt(pos, 63);
        *///?} else {
        return biome.getPrecipitationAt(pos);
        //?}
    }

    public static Biome biome(net.minecraft.core.RegistryAccess registries, ResourceKey<Biome> key) {
        //? if >=1.21.11 {
        /*return registries.lookupOrThrow(Registries.BIOME).getValueOrThrow(key);
        *///?} else {
        return registries.registryOrThrow(Registries.BIOME).getOrThrow(key);
        //?}
    }

    public static void warmBiomesSnow(GameTestHelper helper) {
        BlockPos pos = helper.absolutePos(new BlockPos(1, 2, 1));
        for (ResourceKey<Biome> key : List.of(Biomes.PLAINS, Biomes.DESERT, Biomes.JUNGLE, Biomes.SAVANNA, Biomes.BADLANDS, Biomes.FOREST)) {
            Biome biome = biome(helper, key);
            helper.assertTrue(precipitation(biome, pos) == Biome.Precipitation.SNOW, ichttt.mods.eternalwinter.EternalWinter.id(key) + " precipitation is " + precipitation(biome, pos));
        }
        helper.succeed();
    }

    public static void blacklistedBiomesUntouched(GameTestHelper helper) {
        BlockPos pos = helper.absolutePos(new BlockPos(1, 2, 1));
        for (ResourceKey<Biome> key : List.of(Biomes.RIVER, Biomes.WARM_OCEAN, Biomes.OCEAN)) {
            Biome biome = biome(helper, key);
            helper.assertTrue(precipitation(biome, pos) == Biome.Precipitation.RAIN, ichttt.mods.eternalwinter.EternalWinter.id(key) + " precipitation is " + precipitation(biome, pos));
        }
        helper.succeed();
    }

    public static void snowSettlesOnGrass(GameTestHelper helper) {
        helper.setBlock(new BlockPos(1, 1, 1), Blocks.GRASS_BLOCK);
        BlockPos above = helper.absolutePos(new BlockPos(1, 2, 1));
        helper.assertTrue(biome(helper, Biomes.PLAINS).shouldSnow(helper.getLevel(), above), "snow would not settle on grass in plains");
        helper.setBlock(new BlockPos(3, 1, 1), Blocks.WATER);
        helper.assertTrue(biome(helper, Biomes.DESERT).shouldFreeze(helper.getLevel(), helper.absolutePos(new BlockPos(3, 1, 1)), false), "desert water would not freeze");
        helper.succeed();
    }
}
