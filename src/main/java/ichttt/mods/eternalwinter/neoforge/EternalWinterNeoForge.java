package ichttt.mods.eternalwinter.neoforge;

//? if neoforge {
import com.mojang.serialization.MapCodec;
import ichttt.mods.eternalwinter.EternalWinter;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@Mod(EternalWinter.MOD_ID)
public class EternalWinterNeoForge {
    private static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, EternalWinter.MOD_ID);
    public static final Supplier<MapCodec<BiomesModifier>> BIOME_MODIFIER_CODEC = BIOME_MODIFIER_SERIALIZERS.register("change_to_snow", () -> MapCodec.unit(BiomesModifier::new));

    public EternalWinterNeoForge(IEventBus modBus) {
        EternalWinter.LOGGER.info("Starting up eternal winter mod");
        EternalWinter.loadConfig(FMLPaths.CONFIGDIR.get());
        BIOME_MODIFIER_SERIALIZERS.register(modBus);
    }

    public static class BiomesModifier implements BiomeModifier {
        @Override
        //? if >=26 {
        /*public void modify(net.minecraft.core.RegistryAccess registries, Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        *///?} else {
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        //?}
            if (phase != Phase.MODIFY || biome.unwrapKey().isEmpty() || !EternalWinter.affects(biome.unwrapKey().get())) {
                return;
            }
            var climate = builder.getClimateSettings();
            if (EternalWinter.config.downfall != -1) {
                climate.setDownfall((float) EternalWinter.config.downfall);
            }
            climate.setTemperature(0F);
            climate.setTemperatureModifier(Biome.TemperatureModifier.NONE);
            climate.setHasPrecipitation(true);
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return BIOME_MODIFIER_CODEC.get();
        }
    }
}
//?}
