package cofh.thermal.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static cofh.lib.util.constants.Constants.FULL_CUBE_COLLISION;

public class LumiumBlock extends Block {

    protected static int duration = 40;

    public LumiumBlock(Properties properties) {

        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return FULL_CUBE_COLLISION;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {

        if (entityIn instanceof LivingEntity mob) {
            if (mob.getMobType() == MobType.UNDEAD) {
                mob.setSecondsOnFire(duration);
            }
        }
    }

}
