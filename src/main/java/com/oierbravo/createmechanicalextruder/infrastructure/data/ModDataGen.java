package com.oierbravo.createmechanicalextruder.infrastructure.data;

import com.oierbravo.createmechanicalextruder.foundation.data.ExtrudingRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModDataGen {
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        if (event.includeServer()) {
            generator.addProvider(true, new ExtrudingRecipeGen(output));
        }
    }
}