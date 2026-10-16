package ichttt.mods.eternalwinter.probe;

//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricMultiplayerProbe implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (ProbeLogic.start()) {
            ProbeLogic.modLoaded = FabricLoader.getInstance().isModLoaded("eternalwinter");
            ClientTickEvents.END_CLIENT_TICK.register(ProbeLogic::tick);
        }
    }
}
*///?}
