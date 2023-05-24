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
    protected void renderSafe(ExtruderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);
        if (Backend.canUseInstancing(be.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        //FilteringRenderer.renderOnTileEntity(te, partialTicks, ms, buffer, light, overlay);



        BlockState blockState = be.getBlockState();

        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.SHAFT_HALF, blockState);
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);

        ExtrudingBehaviour extrudingBehaviour = ((ExtruderBlockEntity) be).getExtrudingBehaviour();
        float renderedHeadOffset =
                extrudingBehaviour.getRenderedPoleOffset(partialTicks);

        SuperByteBuffer poleRender = CachedBufferer.partialFacing(ModPartials.MECHANICAL_EXTRUDER_POLE, blockState,
                blockState.getValue(HORIZONTAL_FACING));
        poleRender.translate(0, -renderedHeadOffset + extrudingBehaviour.headOffset, 0)
                .light(light)
                .renderInto(ms, vb);
    }

}
