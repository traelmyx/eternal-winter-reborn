package ichttt.mods.eternalwinter.test.fabric;

//? if fabric {
/*import ichttt.mods.eternalwinter.test.WinterTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "eternalwinter_gametest:empty";

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void warmBiomesSnow(GameTestHelper helper) {
		WinterTests.warmBiomesSnow(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void blacklistedBiomesUntouched(GameTestHelper helper) {
		WinterTests.blacklistedBiomesUntouched(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void snowSettlesOnGrass(GameTestHelper helper) {
		WinterTests.snowSettlesOnGrass(helper);
	}
}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "eternalwinter_gametest:empty";

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void warmBiomesSnow(GameTestHelper helper) {
		WinterTests.warmBiomesSnow(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void blacklistedBiomesUntouched(GameTestHelper helper) {
		WinterTests.blacklistedBiomesUntouched(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void snowSettlesOnGrass(GameTestHelper helper) {
		WinterTests.snowSettlesOnGrass(helper);
	}
}
^///?}
*///?}
