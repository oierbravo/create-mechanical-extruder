package com.oierbravo.createmechanicalextruder.infrastructure.data;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.foundation.data.ExtrudingRecipeGen;
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
        event.getGenerator().addProvider(true, CreateMechanicalExtruder.registrate().setDataProvider(new RegistrateDataProvider(CreateMechanicalExtruder.registrate(), CreateMechanicalExtruder.MODID, event)));

    }
}