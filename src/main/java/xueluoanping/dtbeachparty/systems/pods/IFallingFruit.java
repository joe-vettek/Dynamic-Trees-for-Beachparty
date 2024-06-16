package xueluoanping.dtbeachparty.systems.pods;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import xueluoanping.dtbeachparty.DTBeachpartyRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

// Thanks supermassimo
public interface IFallingFruit {

    int fallDamageMax = 40;
    float fallDamageAmount = 2.0F;

    DamageSource getDamageSource(Level level);

    default boolean checkToFall(BlockState state, Level world, BlockPos pos, RandomSource random){
        if (getAge(state) < getMaxAge()) return false;
        return random.nextFloat() <= getRandomFruitFallChance();
    }

    int getRootY (BlockState state, Level world, BlockPos pos);

    default boolean doFall(BlockState state, Level world, BlockPos pos){
        if (getPlayerDistanceToFall() <= 0) return false; //if distance is 0 block falling is disabled.
        int rootY = getRootY(state, world, pos);
        //if a player is not nearby don't bother, this is a gimmick to damage the player >:)
        if (!world.hasNearbyAlivePlayer(pos.getX(), rootY, pos.getZ(), getPlayerDistanceToFall())){
            return false;
        }
        if (pos.getY() >= world.getMinBuildHeight() && FallingBlock.isFree(world.getBlockState(pos.below()))) {
            if (world.isLoaded(pos)) {
                if (!world.isClientSide()) {
                    FallingBlockEntity fallingBlockEntity = getFallingEntity(world, pos, state);
                    world.addFreshEntity(fallingBlockEntity);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    return true;
                }
            }
        }
        return false;
    }

    default FallingBlockEntity getFallingEntity (Level world, BlockPos pos, BlockState state){
        return new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state){

            @Override
            public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
                int i = (int)Math.ceil(pFallDistance - 1.0F);
                if (i > 0) {
                    List<Entity> list = Lists.newArrayList(level().getEntities(this, this.getBoundingBox()));
                    for(Entity entity : list) {
                        if (entity instanceof LivingEntity){
                            entity.hurt(getDamageSource(level()),
                                    (float)Math.min(Math.floor((float)i * IFallingFruit.fallDamageAmount), IFallingFruit.fallDamageMax) * pMultiplier);
                            level().playSound(null, pos,
                                    DTBeachpartyRegistries.FRUIT_BONK.get(), SoundSource.BLOCKS,
                                    1.0F, 1.0F);
                        }
                    }
                }
                return false;
            }

            @Nullable
            @Override
            public ItemEntity spawnAtLocation(@Nonnull ItemStack pStack, float pOffsetY) {
                return super.spawnAtLocation(getDropOnFallItems( this), pOffsetY);
            }
        };
    }

    ItemStack getDropOnFallItems(FallingBlockEntity entity);
    float getRandomFruitFallChance ();
    float getPlayerDistanceToFall();
    int getAge(BlockState state);
    int getMaxAge();
}