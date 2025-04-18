package com.oierbravo.create_mechanical_extruder.components.extruder.brass;

import com.oierbravo.create_mechanical_extruder.components.extruder.AbstractExtruderBlockEntity;
import com.oierbravo.create_mechanical_extruder.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BrassExtruderBlockEntity extends AbstractExtruderBlockEntity {
    @Override
    public boolean isAdvancedMachine() {
        return true;
    }

    public BrassExtruderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MECHANICAL_BRASS_EXTRUDER.get(),
                (be, context) -> be.getItemHandler()
        );

    }
}
