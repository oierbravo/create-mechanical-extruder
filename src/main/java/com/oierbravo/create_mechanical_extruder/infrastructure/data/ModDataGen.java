package com.oierbravo.create_mechanical_extruder.infrastructure.data;

import com.oierbravo.create_mechanical_extruder.CreateMechanicalExtruder;
import com.oierbravo.create_mechanical_extruder.ModConstants;
import com.tterrag.registrate.providers.RegistrateDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ModDataGen {
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


        if (event.includeServer()) {
            generator.addProvider(true, new ExtrudingRecipeGen(output, lookupProvider));
        }
        event.getGenerator().addProvider(true, CreateMechanicalExtruder.registrate().setDataProvider(new RegistrateDataProvider(CreateMechanicalExtruder.registrate(), ModConstants.MODID, event)));

    }
}