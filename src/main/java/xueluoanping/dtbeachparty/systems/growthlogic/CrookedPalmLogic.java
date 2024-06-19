package xueluoanping.dtbeachparty.systems.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.PalmGrowthLogic;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Random;

// Thanks to maxhyper
// signal_energy controls the maximum trunk length.
// The value of each position in the six directions of probMap affects the initial branch size,
// but in the end, only values greater than 0 will always grow unless the fertility is exhausted.
// Because each growth pulse is sent throughout the tree along the trunk.
public class CrookedPalmLogic extends PalmGrowthLogic {

    public static final ConfigurationProperty<Integer> OFFSET_ODDS = ConfigurationProperty.integer("chance_to_offset");

    public CrookedPalmLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(OFFSET_ODDS, 4); // can only split under the bottom half
    }

    @Override
    protected void registerProperties() {
        this.register(OFFSET_ODDS);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {

        final Species species = context.species();
        final int[] probMap = new int[6];

        boolean doesOffset = CoordUtils.coordHashCode(context.pos(), 3) % configuration.get(OFFSET_ODDS) == 0;

        if (doesOffset) {
            int offsetDir = CoordUtils.coordHashCode(context.pos(), 2) % 4;
            probMap[2 + offsetDir] = 1;
        } else {
            probMap[Direction.UP.ordinal()] = species.getUpProbability();
        }

        return probMap;
    }

}
