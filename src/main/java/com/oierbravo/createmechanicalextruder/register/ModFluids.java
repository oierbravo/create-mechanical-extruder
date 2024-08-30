package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ModFluids {
    public static final CreateRegistrate REGISTRATE = CreateMechanicalExtruder.registrate();
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_ANDESITE_ALLOY = createMoltenFluid( "andesite_alloy");


    private static FluidEntry<ForgeFlowingFluid.Flowing> createMoltenFluid(String target){

        return REGISTRATE.standardFluid("molten_" + target)
                .lang("Molten " + target)
                .properties(b -> b.viscosity(2000)
                        .density(1400))
                .fluidProperties(p -> p.levelDecreasePerBlock(2)
                        .tickRate(25)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .tag(AllTags.forgeFluidTag("molten_fluid"))
                .source(ForgeFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                .bucket()
                .tag(AllTags.forgeItemTag("buckets/molten"))
                .build()
                .register();
    }

    public static void register() {}
}