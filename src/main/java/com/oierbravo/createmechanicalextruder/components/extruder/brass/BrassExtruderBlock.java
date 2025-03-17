package com.oierbravo.createmechanicalextruder.components.extruder.brass;

import com.oierbravo.createmechanicalextruder.components.extruder.AbstractExtruderBlock;
import com.oierbravo.createmechanicalextruder.register.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class BrassExtruderBlock extends AbstractExtruderBlock<BrassExtruderBlockEntity> {
    public BrassExtruderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory(BrassExtruderBlockEntity extruder) {
        return extruder.outputInventory;
    }

    @Override
    protected void sendDataInternal(BrassExtruderBlockEntity extruder) {
        extruder.sendData();
    }

    @Override
    public Class<BrassExtruderBlockEntity> getBlockEntityClass() {
        return BrassExtruderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BrassExtruderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_BRASS_EXTRUDER.get();
    }
}
