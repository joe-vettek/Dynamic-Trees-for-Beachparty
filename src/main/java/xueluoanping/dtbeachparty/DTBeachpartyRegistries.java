package xueluoanping.dtbeachparty;

import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xueluoanping.dtbeachparty.systems.ModCellKit;
import xueluoanping.dtbeachparty.systems.ModGrowthLogicKits;
import xueluoanping.dtbeachparty.systems.PalmFruitGenFeature;
import xueluoanping.dtbeachparty.systems.pods.FallingPalmPod;
import xueluoanping.dtbeachparty.trees.DiagonalPalmFamily;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTBeachpartyRegistries {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DTBeachparty.MOD_ID);

    public static final RegistryObject<SoundEvent> FRUIT_BONK = registerSound("falling_fruit.bonk");

    public static RegistryObject<SoundEvent> registerSound (String name){
        return SOUNDS.register(name, ()-> SoundEvent.createVariableRangeEvent(DTBeachparty.rl(name)));
    }

    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        event.registerType(DTBeachparty.rl("crooked_palm"), DiagonalPalmFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes(final TypeRegistryEvent<LeavesProperties> event) {
        DTBeachparty.LOGGER.debug("registerLeavesPropertiesTypes");
    }

    @SubscribeEvent
    public static void registerFruitTypes(final TypeRegistryEvent<Fruit> event) {
        DTBeachparty.LOGGER.debug("registerFruitTypes");
    }

    @SubscribeEvent
    public static void registerPodTypes(final TypeRegistryEvent<Pod> event) {
        // DTNaturesSpirit.LOGGER.debug("registerFruitTypes");
        event.registerType(DTBeachparty.rl( "falling_palm"), FallingPalmPod.TYPE);
        // event.registerType(new ResourceLocation(DTNaturesSpirit.MOD_ID, "named_fruit"), NamedFruitTypes.TYPE);
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        event.getRegistry().register(new PalmFruitGenFeature(DTBeachparty.rl("palm_fruit")));
    }


    @SubscribeEvent
    public static void onGrowthLogicKitsRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        ModGrowthLogicKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onCellKitsRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<CellKit> event) {
        ModCellKit.register(event.getRegistry());
    }
}
