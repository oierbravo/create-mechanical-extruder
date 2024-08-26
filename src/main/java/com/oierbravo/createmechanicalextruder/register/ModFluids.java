package com.oierbravo.createmechanicalextruder.register;

public class ModFluids {
    public static String PREFIX = "spawn_fluid";
    public static final CreateRegistrate REGISTRATE = CreateMechanicalSpawner.registrate();
    public static final FluidEntry<ForgeFlowingFluid.Flowing> RANDOM = createSpawnFluid( "random");

    /* Hostile Mobs */
    public static final FluidEntry<ForgeFlowingFluid.Flowing> BLAZE = createSpawnFluid("blaze");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> CREEPER = createSpawnFluid("creeper");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> DROWNED = createSpawnFluid("drowned");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> ENDERMAN = createSpawnFluid("enderman");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> EVOKER = createSpawnFluid("evoker");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> GHAST = createSpawnFluid("ghast");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MAGMA_CUBE = createSpawnFluid("magma_cube");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> PIGLING = createSpawnFluid("pigling");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> SKELETON = createSpawnFluid("skeleton");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> SLIME = createSpawnFluid("slime");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> SPIDER = createSpawnFluid("spider");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> WITCH = createSpawnFluid("witch");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> WITHER_SKELETON = createSpawnFluid("wither_skeleton");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> ZOMBIE = createSpawnFluid("zombie");

    /* Friendly Mobs */
    public static final FluidEntry<ForgeFlowingFluid.Flowing> BAT = createSpawnFluid("bat");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> BEE = createSpawnFluid("bee");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> COW = createSpawnFluid("cow");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> CHICKEN = createSpawnFluid("chicken");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> FOX = createSpawnFluid("fox");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HORSE = createSpawnFluid("horse");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> PANDA = createSpawnFluid("panda");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> PIG = createSpawnFluid("pig");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> RABBIT = createSpawnFluid("rabbit");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> VILLAGER = createSpawnFluid("villager");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> WOLF = createSpawnFluid("wolf");


    private static FluidEntry<ForgeFlowingFluid.Flowing> createSpawnFluid(String target){
        ResourceLocation flow = new ResourceLocation(CreateMechanicalSpawner.MODID,"fluid/spawn_fluid_" + target + "_flow");
        ResourceLocation still = new ResourceLocation(CreateMechanicalSpawner.MODID,"fluid/spawn_fluid_" + target + "_still");

        //return REGISTRATE.standardFluid(PREFIX + "_" + target,still, flow)
        return REGISTRATE.standardFluid(PREFIX + "_" + target)
                .lang("Spawn fluid " + target)
                .properties(b -> b.viscosity(2000)
                        .density(1400))
                .fluidProperties(p -> p.levelDecreasePerBlock(2)
                        .tickRate(25)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .tag(AllTags.forgeFluidTag("spawn_fluid"))
                .source(ForgeFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                .bucket()
                .tag(AllTags.forgeItemTag("buckets/spawn_fluid"))
                .build()
                .register();
    }

    public static void register() {}
}