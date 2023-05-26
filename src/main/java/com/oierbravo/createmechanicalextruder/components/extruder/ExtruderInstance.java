package com.oierbravo.createmechanicalextruder.components.extruder;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ExtruderInstance extends ShaftInstance<ExtruderBlockEntity> implements DynamicInstance {
    private final OrientedData extruderPole;
    private final ExtruderBlockEntity extruder;

    public ExtruderInstance(MaterialManager dispatcher, ExtruderBlockEntity tile) {
        super(dispatcher, tile);
        extruder = tile;

        extruderPole = dispatcher.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(ModPartials.MECHANICAL_EXTRUDER_POLE, blockState)
                .createInstance();

        Quaternion q = Vector3f.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(ExtruderBlock.HORIZONTAL_FACING)));
        extruderPole.setRotation(q);

        transformModels();


    }


    @Override
    public void beginFrame() {
        transformModels();
    }

    private void transformModels() {
        float renderedHeadOffset = getRenderedHeadOffset(extruder);

        extruderPole.setPosition(getInstancePosition())
                .nudge(0, -renderedHeadOffset + extruder.getExtrudingBehaviour().headOffset, 0);
    }

    private float getRenderedHeadOffset(ExtruderBlockEntity press) {
        ExtrudingBehaviour pressingBehaviour = press.getExtrudingBehaviour();
        return pressingBehaviour.getRenderedPoleOffset(AnimationTickHolder.getPartialTicks());
    }

    @Override
    public void updateLight() {
        super.updateLight();

        relight(pos, extruderPole);
    }

    @Override
    public void remove() {
        super.remove();
        extruderPole.delete();
    }
    @Override
    protected Instancer<RotatingData> getModel() {


            BlockState referenceState = blockState.rotate(blockEntity.getLevel(), blockEntity.getBlockPos(), Rotation.CLOCKWISE_180);
            Direction facing = referenceState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, referenceState, facing);
    }
}
