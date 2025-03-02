package com.oierbravo.createmechanicalextruder.components.extruder;

import com.mojang.math.Axis;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ExtruderVisual extends KineticBlockEntityVisual<ExtruderBlockEntity> implements SimpleDynamicVisual {
    private final OrientedInstance extruderPole;
    protected final RotatingInstance shaft;
    final Direction direction;
    private final Direction opposite;


    public ExtruderVisual(VisualizationContext context, ExtruderBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        direction = blockState.getValue(HORIZONTAL_FACING);

        opposite = direction.getOpposite();

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();

        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();

        extruderPole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartials.MECHANICAL_EXTRUDER_POLE))
                .createInstance();

        Quaternionf q = Axis.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(MechanicalPressBlock.HORIZONTAL_FACING)));

        extruderPole.rotation(q);

        transformModels(partialTick);
    }


    private void transformModels(float pt) {
        shaft.setup(blockEntity)
                .setChanged();
        float renderedHeadOffset = getRenderedHeadOffset(pt);

        extruderPole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0)
                .setChanged();
    }

    private float getRenderedHeadOffset(float pt) {
        ExtrudingBehaviour extrudingBehaviour = blockEntity.getExtrudingBehaviour();
        return extrudingBehaviour.getRenderedPoleOffset(pt)
                * extrudingBehaviour.headOffset;

    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        transformModels(ctx.partialTick());
    }

    @Override
    public void updateLight(float partialTick) {
        relight(extruderPole);
        relight(shaft);
    }

    @Override
    protected void _delete() {
        extruderPole.delete();
        shaft.delete();
    }
    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(extruderPole);
        consumer.accept(shaft);
    }

}
