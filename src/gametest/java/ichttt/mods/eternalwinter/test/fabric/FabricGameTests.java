package ichttt.mods.eternalwinter.test.fabric;

//? if fabric {
/*import ichttt.mods.eternalwinter.test.WinterTests;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class FabricGameTests implements FabricGameTest {
    private static final String EMPTY = "eternalwinter_gametest:empty";

    @GameTest(template = EMPTY)
    public void warmBiomesSnow(GameTestHelper helper) {
        WinterTests.warmBiomesSnow(helper);
    }

    @GameTest(template = EMPTY)
    public void blacklistedBiomesUntouched(GameTestHelper helper) {
        WinterTests.blacklistedBiomesUntouched(helper);
    }

    @GameTest(template = EMPTY)
    public void snowSettlesOnGrass(GameTestHelper helper) {
        WinterTests.snowSettlesOnGrass(helper);
    }
}
*///?}
