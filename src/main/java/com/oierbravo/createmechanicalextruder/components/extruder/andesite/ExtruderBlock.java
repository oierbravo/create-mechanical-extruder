package com.oierbravo.createmechanicalextruder.components.extruder.andesite;

import com.oierbravo.createmechanicalextruder.components.extruder.AbstractExtruderBlock;
import com.oierbravo.createmechanicalextruder.register.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class ExtruderBlock extends AbstractExtruderBlock<ExtruderBlockEntity> {
    public ExtruderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory(ExtruderBlockEntity extruder) {
        return extruder.outputInventory;
    }

    @Override
    protected void sendDataInternal(ExtruderBlockEntity extruder) {
        extruder.sendData();
    }

    @Override
    public Class<ExtruderBlockEntity> getBlockEntityClass() {
        return ExtruderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ExtruderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_EXTRUDER.get();
    }
}
