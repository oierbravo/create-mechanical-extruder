package com.oierbravo.createmechanicalextruder.components.extruder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class AbstractExtruderRenderer<EXB extends AbstractExtruderBlockEntity> extends KineticBlockEntityRenderer<EXB> {
    public AbstractExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    protected abstract PartialModel getPoleModel();

    @Override
    public boolean shouldRenderOffScreen(EXB be) {
        return true;
    }
    @Override
    protected void renderSafe(EXB be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);

        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState blockState = be.getBlockState();

        float renderedHeadOffset =
                be.getRenderedPoleOffset(partialTicks);

        SuperByteBuffer poleRender = CachedBuffers.partialFacing(ModPartials.MECHANICAL_EXTRUDER_POLE, blockState,
                blockState.getValue(HORIZONTAL_FACING));
        poleRender.translate(0, -renderedHeadOffset + be.headOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        SuperByteBuffer superBuffer = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, blockState, blockState.getValue(HORIZONTAL_FACING).getOpposite());
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
    }
   @Override
   protected SuperByteBuffer getRotatedModel(EXB be, BlockState state) {
       return CachedBuffers.partial(AllPartialModels.SHAFT_HALF, state);
   }
}
