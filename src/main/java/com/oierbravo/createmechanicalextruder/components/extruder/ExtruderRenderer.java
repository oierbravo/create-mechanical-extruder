package com.oierbravo.createmechanicalextruder.components.extruder;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ExtruderRenderer extends KineticBlockEntityRenderer<ExtruderBlockEntity> {
    public ExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public boolean shouldRenderOffScreen(ExtruderBlockEntity be) {
        return true;
    }
    @Override
    protected void renderSafe(ExtruderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        //super.renderSafe(be, partialTicks, ms, buffer, light, overlay);


        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);

        if (Backend.canUseInstancing(be.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState blockState = be.getBlockState();



        ExtrudingBehaviour extrudingBehaviour = be.getExtrudingBehaviour();
        float renderedHeadOffset =
                extrudingBehaviour.getRenderedPoleOffset(partialTicks);

        SuperByteBuffer poleRender = CachedBufferer.partialFacing(ModPartials.MECHANICAL_EXTRUDER_POLE, blockState,
                blockState.getValue(HORIZONTAL_FACING));
        poleRender.translate(0, -renderedHeadOffset + extrudingBehaviour.headOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        SuperByteBuffer superBuffer = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, blockState, blockState.getValue(HORIZONTAL_FACING).getOpposite());
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
        //kineticRotationTransform(superBuffer, be, blockState.getValue(HORIZONTAL_FACING).getAxis(), 90,light).renderInto(ms, vb);

        //KineticBlockEntityRenderer.renderRotatingBuffer(be, getRotatedModel(be, be.getBlockState()), ms,
        //        buffer.getBuffer(RenderType.solid()), light);
        //CachedBufferer.partial(AllPartialModels.SHAFT_HALF, blockState);
        //standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
        //KineticBlockEntityRenderer.renderRotatingBuffer( be, superBuffer, ms, buffer,light)
        //super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        //KineticBlockEntityRenderer.
    }
    /*protected SuperByteBuffer getRotatedModel(ExtruderBlockEntity be) {
        BlockState state = be.getBlockState();
        if (state.getValue(HORIZONTAL_FACING)
                .getAxis()
                .isHorizontal())
            return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF,
                    state.rotate(be.getLevel(), be.getBlockPos(), Rotation.CLOCKWISE_180));
        return CachedBufferer.block(KineticBlockEntityRenderer.KINETIC_BLOCK,
                getRenderedBlockState(be));
    }*/
   /* @Override
    protected BlockState getRenderedBlockState(ExtruderBlockEntity be) {
        return AllBlocks.HALF.getDefaultState()
                .setValue(BlockStateProperties.AXIS, getRotationAxisOf(be));
    }*/
   @Override
   protected SuperByteBuffer getRotatedModel(ExtruderBlockEntity be, BlockState state) {
       return CachedBufferer.partial(AllPartialModels.SHAFT_HALF, state);
   }
}
