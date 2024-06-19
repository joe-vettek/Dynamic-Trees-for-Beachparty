package xueluoanping.dtbeachparty.models;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.BakingCompleted;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xueluoanping.dtbeachparty.DTBeachparty;

@Mod.EventBusSubscriber(modid = DTBeachparty.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelBakeEventHandler {

    @SubscribeEvent
    public static void onModelRegistryEvent(RegisterGeometryLoaders event) {
        event.register("large_palm_fronds", new PalmLeavesModelLoader());
    }

    @SubscribeEvent
    public static void onModelBake(BakingCompleted event) {
        // Setup fronds models
        LargePalmLeavesBakedModel.INSTANCES.forEach(LargePalmLeavesBakedModel::setupModels);
    }

}