package ichttt.mods.eternalwinter.fabric;

//? if fabric {
/*import ichttt.mods.eternalwinter.EternalWinter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.loader.api.FabricLoader;
//? if >=1.21.11 {
/^import net.minecraft.resources.Identifier;
^///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.level.biome.Biome;

public class EternalWinterFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EternalWinter.LOGGER.info("Starting up eternal winter mod");
        EternalWinter.loadConfig(FabricLoader.getInstance().getConfigDir());
        //? if >=1.21.11 {
        /^BiomeModifications.create(Identifier.fromNamespaceAndPath(EternalWinter.MOD_ID, "change_to_snow"))
        ^///?} else {
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(EternalWinter.MOD_ID, "change_to_snow"))
        //?}
            .add(ModificationPhase.POST_PROCESSING, BiomeSelectors.all(), (selection, context) -> {
                if (!EternalWinter.affects(selection.getBiomeKey())) {
                    return;
                }
                var weather = context.getWeather();
                if (EternalWinter.config.downfall != -1) {
                    weather.setDownfall((float) EternalWinter.config.downfall);
                }
                weather.setTemperature(0F);
                weather.setTemperatureModifier(Biome.TemperatureModifier.NONE);
                weather.setPrecipitation(true);
            });
    }
}
*///?}
