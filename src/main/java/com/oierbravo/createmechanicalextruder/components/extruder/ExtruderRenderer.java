package com.oierbravo.createmechanicalextruder.components.extruder;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.behaviour.filtering.FilteringRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ExtruderRenderer extends KineticTileEntityRenderer {
    public ExtruderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public boolean shouldRenderOffScreen(KineticTileEntity te) {
        return true;
    }
    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if (Backend.canUseInstancing(te.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        FilteringRenderer.renderOnTileEntity(te, partialTicks, ms, buffer, light, overlay);



        BlockState blockState = te.getBlockState();

        SuperByteBuffer superBuffer = CachedBufferer.partial(AllBlockPartials.SHAFT_HALF, blockState);
        standardKineticRotationTransform(superBuffer, te, light).renderInto(ms, vb);

        ExtrudingBehaviour extrudingBehaviour = ((ExtruderTileEntity) te).getExtrudingBehaviour();
        float renderedHeadOffset =
                extrudingBehaviour.getRenderedPoleOffset(partialTicks);

        SuperByteBuffer poleRender = CachedBufferer.partialFacing(ModPartials.MECHANICAL_EXTRUDER_POLE, blockState,
                blockState.getValue(HORIZONTAL_FACING));
        poleRender.translate(0, -renderedHeadOffset + extrudingBehaviour.headOffset, 0)
                .light(light)
                .renderInto(ms, vb);
    }
    /*@Override
    protected BlockState getRenderedBlockState(KineticTileEntity te) {
        return shaft(getRotationAxisOf(te));
    }*/
}
