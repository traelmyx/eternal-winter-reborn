package ichttt.mods.eternalwinter.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

import net.minecraft.world.level.biome.Biome;

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
                    Screen screen = mc.screen;
                    if (screen != null && mc.getOverlay() == null && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        mc.options.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
                        createWorld(mc);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && mc.screen == null) {
                        stage = 2;
                    }
                }
                case 2 -> {
                    ticks++;
                    if (ticks == 40) {
                        var server = mc.getSingleplayerServer();
                        server.execute(() -> {
                            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "time set 3000");
                            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "weather rain 6000");
                        });
                    }
                    if (ticks == 60) {
                        mc.options.hideGui = true;
                        mc.player.setXRot(10);
                    }
                    if (ticks == 400) {
                        Screenshot.grab(mc.gameDirectory, "snowfall.png", mc.getMainRenderTarget(), msg -> {});
                        mc.options.hideGui = false;
                    }
                    if (ticks >= 420) {
                        var pos = mc.player.blockPosition();
                        var precipitation = mc.level.getBiome(pos).value().getPrecipitationAt(pos);
                        String biome = mc.level.getBiome(pos).unwrapKey().map(k -> k.location().toString()).orElse("?");
                        System.out.println("[clientsmoke] client biome " + biome + " precipitation " + precipitation);
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

    private static void createWorld(Minecraft mc) {
        LevelSettings settings = new LevelSettings("smoke", GameType.SURVIVAL, false, Difficulty.PEACEFUL, true, new GameRules(), WorldDataConfiguration.DEFAULT);
        mc.createWorldOpenFlows().createFreshLevel("smoke", settings, new WorldOptions(20261016L, true, false),
            registries -> registries.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.NORMAL).value().createWorldDimensions(),
            mc.screen);
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
