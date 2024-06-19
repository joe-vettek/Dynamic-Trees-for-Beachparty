package xueluoanping.dtbeachparty.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.cell.Cell;
import com.ferreusveritas.dynamictrees.api.cell.CellNull;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.BlockStates;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DiagonalPalmFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(DiagonalPalmFamily::new);

    public DiagonalPalmFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        final BasicBranchBlock branch = new BasicBranchBlock(name, this.getProperties()){
            @Override
            public Cell getHydrationCell(BlockGetter level, BlockPos pos, BlockState state, Direction dir, LeavesProperties leavesProperties) {
                if (getRadius(state) != getFamily().getPrimaryThickness()) return CellNull.NULL_CELL;
                return super.getHydrationCell(level, pos, state, dir, leavesProperties);
            }

            @Override
            public GrowSignal growIntoAir(Level world, BlockPos pos, GrowSignal signal, int fromRadius) {
                final Species species = signal.getSpecies();

                final DynamicLeavesBlock leaves = species.getLeavesBlock().orElse(null);
                if (leaves != null) {
                    if (fromRadius == getFamily().getPrimaryThickness()) {// If we came from a twig (and we're not a stripped branch) then just make some leaves
                        if (isNextToBranch(world, pos, signal.dir.getOpposite())){
                            signal.success = false;
                            return signal;
                        }
                        signal.success = leaves.growLeavesIfLocationIsSuitable(world, species.getLeavesProperties(), pos.above(), 0);
                        if (signal.success)
                            return leaves.branchOut(world, pos, signal);
                    } else {// Otherwise make a proper branch
                        return leaves.branchOut(world, pos, signal);
                    }
                } else {
                    //If the leaves block is null, the branch grows directly without checking for leaves requirements
                    if (isNextToBranch(world, pos, signal.dir.getOpposite())){
                        signal.success = false;
                        return signal;
                    }
                    setRadius(world, pos, getFamily().getPrimaryThickness(), null);
                    signal.radius = getFamily().getSecondaryThickness();
                    signal.success = true;
                }
                return signal;
            }
        };
        if (this.isFireProof())
            branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }
}