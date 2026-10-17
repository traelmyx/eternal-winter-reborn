package ichttt.mods.eternalwinter.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

import java.nio.file.Files;

// Run with -Dsmoketest=true: creates a flat world, takes a screenshot and quits. Exit code 1 on failure.
public final class ClientSmoke {

    public static final boolean ENABLED = Boolean.getBoolean("smoketest");
    private static final long TIMEOUT_MS = 5 * 60 * 1000;

    private static int stage;
    private static int ticks;

    private ClientSmoke() {
    }

    public static boolean start() {
        if (!ENABLED) {
            return false;
        }
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MS);
                fail("timed out");
            } catch (InterruptedException ignored) {
            }
        }, "smoketest-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        return true;
    }

    public static void tick(Minecraft mc) {
        try {
            switch (stage) {
                case 0 -> {
                    Screen screen = SmokeCompat.screen(mc);
                    if (screen != null && SmokeCompat.overlayHidden(mc) && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        mc.options.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
                        SmokeCompat.createWorld(mc, GameType.SURVIVAL, WorldPresets.NORMAL, 20261016L, true);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && SmokeCompat.screen(mc) == null) {
                        stage = 2;
                    }
                }
                case 2 -> {
                    ticks++;
                    if (ticks == 40) {
                        // go somewhere that never sees snow in vanilla: a desert, or plains if there is none nearby
                        var server = mc.getSingleplayerServer();
                        server.execute(() -> {
                            var player = server.getPlayerList().getPlayers().get(0);
                            var level = player.serverLevel();
                            var found = level.findClosestBiome3d(b -> b.is(Biomes.DESERT), player.blockPosition(), 4000, 32, 64);
                            if (found == null) found = level.findClosestBiome3d(b -> b.is(Biomes.PLAINS), player.blockPosition(), 4000, 32, 64);
                            if (found != null) {
                                var at = found.getFirst();
                                level.getChunk(at);
                                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, at.getX(), at.getZ());
                                server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), String.format(java.util.Locale.ROOT, "tp %s %.1f %d %.1f 0 10", player.getStringUUID(), at.getX() + 0.5, y + 1, at.getZ() + 0.5));
                            }
                            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "time set 3000");
                            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "weather rain 6000");
                        });
                    }
                    if (ticks == 60) {
                        mc.options.hideGui = true;
                    }
                    if (ticks == 400) {
                        SmokeCompat.screenshot(mc, "snowfall.png");
                        mc.options.hideGui = false;
                    }
                    if (ticks >= 420) {
                        var pos = mc.player.blockPosition();
                        var precipitation = mc.level.getBiome(pos).value().getPrecipitationAt(pos);
                        String biome = mc.level.getBiome(pos).unwrapKey().map(k -> k.location().toString()).orElse("?");
                        System.out.println("[clientsmoke] client biome " + biome + " precipitation " + precipitation);
                        var desert = WinterTests.biome(mc.level.registryAccess(), Biomes.DESERT);
                        if (desert.getPrecipitationAt(pos) != Biome.Precipitation.SNOW) {
                            fail("client's desert has " + desert.getPrecipitationAt(pos));
                        }
                        if (precipitation != Biome.Precipitation.SNOW && !biome.contains("river") && !biome.contains("ocean")) {
                            fail("client sees " + precipitation + " in " + biome);
                        }
                        Files.writeString(mc.gameDirectory.toPath().resolve("smoke-ok"), "ok\n");
                        stage = 3;
                        mc.stop();
                    }
                }
                default -> {
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
