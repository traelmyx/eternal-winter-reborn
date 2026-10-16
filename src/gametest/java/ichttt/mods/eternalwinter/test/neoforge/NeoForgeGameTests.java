package ichttt.mods.eternalwinter.test.neoforge;

//? if neoforge {
import ichttt.mods.eternalwinter.test.ClientSmoke;
import ichttt.mods.eternalwinter.test.WinterTests;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
    public static final String MOD_ID = "eternalwinter_gametest";

    public NeoForgeGameTests(Dist dist) {
        if (dist.isClient() && ClientSmoke.start()) {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(net.minecraft.client.Minecraft.getInstance()));
        }
        if (dist.isClient() && ichttt.mods.eternalwinter.test.MultiplayerProbe.start()) {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ichttt.mods.eternalwinter.test.MultiplayerProbe.tick(net.minecraft.client.Minecraft.getInstance()));
        }
    }

    @GameTest(template = "empty")
    public static void warmBiomesSnow(GameTestHelper helper) {
        WinterTests.warmBiomesSnow(helper);
    }

    @GameTest(template = "empty")
    public static void blacklistedBiomesUntouched(GameTestHelper helper) {
        WinterTests.blacklistedBiomesUntouched(helper);
    }

    @GameTest(template = "empty")
    public static void snowSettlesOnGrass(GameTestHelper helper) {
        WinterTests.snowSettlesOnGrass(helper);
    }
}
//?}
