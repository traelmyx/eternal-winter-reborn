package ichttt.mods.eternalwinter.test;

//? if neoforge {

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Run with -Dprobe=<name> and --quickPlayMultiplayer: after joining a dedicated server, writes what the client thinks the
// weather in a few warm biomes is (the client renders precipitation from its own copy of the biome) to probe-<name>.txt.
public final class MultiplayerProbe {
    private static final String NAME = System.getProperty("probe", "");
    private static int ticks;
    private static boolean done;

    private MultiplayerProbe() {
    }

    public static boolean start() {
        if (NAME.isEmpty()) return false;
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(5 * 60 * 1000);
                System.out.println("[probe] FAILED: timed out");
                Runtime.getRuntime().halt(1);
            } catch (InterruptedException ignored) {
            }
        }, "probe-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        return true;
    }

    public static void tick(Minecraft mc) {
        if (done || mc.level == null || mc.player == null || mc.hasSingleplayerServer()) return;
        if (++ticks == 40) {
            mc.player.setXRot(-20);
            mc.options.hideGui = true;
        }
        if (ticks < 200) return;
        done = true;
        try {
            Screenshot.grab(mc.gameDirectory, "probe-" + NAME + ".png", mc.getMainRenderTarget(), msg -> {});
            var biomes = mc.level.registryAccess().registryOrThrow(Registries.BIOME);
            BlockPos pos = mc.player.blockPosition();
            List<String> lines = new ArrayList<>();
            lines.add("probe=" + NAME + " eternalwinterLoaded=" + net.neoforged.fml.ModList.get().isLoaded("eternalwinter"));
            lines.add("standing in " + mc.level.getBiome(pos).unwrapKey().map(k -> k.location().toString()).orElse("?")
                + " precipitation " + mc.level.getBiome(pos).value().getPrecipitationAt(pos) + " raining " + mc.level.isRaining());
            for (ResourceKey<Biome> key : List.of(Biomes.DESERT, Biomes.PLAINS, Biomes.JUNGLE)) {
                Biome biome = biomes.getOrThrow(key);
                lines.add(key.location() + " precipitation " + biome.getPrecipitationAt(new BlockPos(0, 64, 0)) + " temperature " + biome.getBaseTemperature());
            }
            lines.forEach(l -> System.out.println("[probe] " + l));
            Files.write(mc.gameDirectory.toPath().resolve("probe-" + NAME + ".txt"), lines);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        mc.stop();
    }
}
//?}
