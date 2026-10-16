package ichttt.mods.eternalwinter.probe;

//? if neoforge {
import net.minecraft.client.Minecraft;

@net.neoforged.fml.common.Mod("eternalwinter_probe")
public final class MultiplayerProbe {
    public MultiplayerProbe(net.neoforged.api.distmarker.Dist dist) {
        if (dist.isClient() && ProbeLogic.start()) {
            ProbeLogic.modLoaded = net.neoforged.fml.ModList.get().isLoaded("eternalwinter");
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.client.event.ClientTickEvent.Post e) -> ProbeLogic.tick(Minecraft.getInstance()));
        }
    }
}
//?}
