package com.oierbravo.createmechanicalextruder.components.extruder;

import com.mojang.math.Axis;
import com.oierbravo.mechanicals.MechanicalPartials;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

import java.util.function.Consumer;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public abstract class AbstractExtruderVisual<EX extends AbstractExtruderBlockEntity> extends KineticBlockEntityVisual<EX> implements SimpleDynamicVisual {
    private final OrientedInstance extruderPole;
    protected final RotatingInstance shaft;
    final Direction direction;
    private final Direction opposite;

    protected abstract PartialModel getPoleModel();
    public AbstractExtruderVisual(VisualizationContext context, EX blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        direction = blockState.getValue(HORIZONTAL_FACING);

        opposite = direction.getOpposite();

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(MechanicalPartials.SHAFT_QUARTER))
                .createInstance();

        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();

        extruderPole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPoleModel()))
                .createInstance();

        Quaternionf q = Axis.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(HORIZONTAL_FACING)));

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
        return blockEntity.getRenderedPoleOffset(pt)
                * blockEntity.headOffset;

    }

    @Override
    public void beginFrame(Context ctx) {
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