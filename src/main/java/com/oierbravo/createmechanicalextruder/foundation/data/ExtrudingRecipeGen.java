package com.oierbravo.createmechanicalextruder.foundation.data;

import com.oierbravo.create_mechanical_spawner.CreateMechanicalSpawner;
import com.oierbravo.create_mechanical_spawner.content.components.SpawnerRecipeBuilder;
import com.oierbravo.create_mechanical_spawner.content.components.SpawnerRecipeOutput;
import com.oierbravo.create_mechanical_spawner.registrate.ModFluids;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class ExtrudingRecipeGen extends RecipeProvider {
    public ExtrudingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        /* Random Spawner */
        create("random")
                .withFluid(ModFluids.RANDOM.get(),100)
                .withProcessingTime(1500)
                .save(pWriter);

        /* Hostile Spawner */
        create("blaze")
                .withMob(SpawnerRecipeOutput.of("minecraft:blaze"))
                .withFluid(ModFluids.BLAZE.get(),100)
                .withProcessingTime(5000)
                .save(pWriter);

        create("creeper")
                .withMob(SpawnerRecipeOutput.of("minecraft:creeper"))
                .withFluid(ModFluids.CREEPER.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("drowned")
                .withMob(SpawnerRecipeOutput.of("minecraft:drowned"))
                .withFluid(ModFluids.DROWNED.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("enderman")
                .withMob(SpawnerRecipeOutput.of("minecraft:enderman"))
                .withFluid(ModFluids.ENDERMAN.get(),100)
                .withProcessingTime(5000)
                .save(pWriter);

        create("evoker")
                .withMob(SpawnerRecipeOutput.of("minecraft:evoker"))
                .withFluid(ModFluids.EVOKER.get(),500)
                .withProcessingTime(5000)
                .save(pWriter);

        create("ghast")
                .withMob(SpawnerRecipeOutput.of("minecraft:ghast"))
                .withFluid(ModFluids.ENDERMAN.get(),100)
                .withProcessingTime(5000)
                .save(pWriter);

        create("magma_cube")
                .withMob(SpawnerRecipeOutput.of("minecraft:magma_cube"))
                .withFluid(ModFluids.MAGMA_CUBE.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("pigling")
                .withMob(SpawnerRecipeOutput.of("minecraft:pigling"))
                .withFluid(ModFluids.PIGLING.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("skeleton")
                .withMob(SpawnerRecipeOutput.of("minecraft:skeleton"))
                .withFluid(ModFluids.SKELETON.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("slime")
                .withMob(SpawnerRecipeOutput.of("minecraft:slime"))
                .withFluid(ModFluids.SLIME.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("spider")
                .withMob(SpawnerRecipeOutput.of("minecraft:spider"))
                .withFluid(ModFluids.SPIDER.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("witch")
                .withMob(SpawnerRecipeOutput.of("minecraft:witch"))
                .withFluid(ModFluids.WITCH.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("wither_skeleton")
                .withMob(SpawnerRecipeOutput.of("minecraft:wither_skeleton"))
                .withFluid(ModFluids.WITHER_SKELETON.get(),200)
                .withProcessingTime(5000)
                .save(pWriter);

        create("zombie")
                .withMob(SpawnerRecipeOutput.of("minecraft:zombie"))
                .withFluid(ModFluids.ZOMBIE.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        /* Friendly Spawner */
        create("bat")
                .withMob(SpawnerRecipeOutput.of("minecraft:bat"))
                .withFluid(ModFluids.BAT.get(),100)
                .withProcessingTime(1000)
                .save(pWriter);

        create("bee")
                .withMob(SpawnerRecipeOutput.of("minecraft:bee"))
                .withFluid(ModFluids.BEE.get(),100)
                .withProcessingTime(2000)
                .save(pWriter);

        create("chicken")
                .withMob(SpawnerRecipeOutput.of("minecraft:chicken"))
                .withFluid(ModFluids.CHICKEN.get(),100)
                .withProcessingTime(1000)
                .save(pWriter);

        create("cow")
                .withMob(SpawnerRecipeOutput.of("minecraft:cow"))
                .withFluid(ModFluids.COW.get(),100)
                .withProcessingTime(2500)
                .save(pWriter);

        create("fox")
                .withMob(SpawnerRecipeOutput.of("minecraft:fox"))
                .withFluid(ModFluids.FOX.get(),100)
                .withProcessingTime(3000)
                .save(pWriter);

        create("horse")
                .withMob(SpawnerRecipeOutput.of("minecraft:horse"))
                .withFluid(ModFluids.HORSE.get(),100)
                .withProcessingTime(2000)
                .save(pWriter);

        create("panda")
                .withMob(SpawnerRecipeOutput.of("minecraft:panda"))
                .withFluid(ModFluids.PANDA.get(),100)
                .withProcessingTime(4000)
                .save(pWriter);

        create("pig")
                .withMob(SpawnerRecipeOutput.of("minecraft:pig"))
                .withFluid(ModFluids.PIG.get(),100)
                .withProcessingTime(1500)
                .save(pWriter);

        create("rabbit")
                .withMob(SpawnerRecipeOutput.of("minecraft:rabbit"))
                .withFluid(ModFluids.RABBIT.get(),100)
                .withProcessingTime(1000)
                .save(pWriter);

        create("villager")
                .withMob(SpawnerRecipeOutput.of("minecraft:villager"))
                .withFluid(ModFluids.VILLAGER.get(),100)
                .withProcessingTime(5000)
                .save(pWriter);

        create("wolf")
                .withMob(SpawnerRecipeOutput.of("minecraft:wolf"))
                .withFluid(ModFluids.WOLF.get(),100)
                .withProcessingTime(1500)
                .save(pWriter);
    }

    private SpawnerRecipeBuilder create(String id){
        return new SpawnerRecipeBuilder(CreateMechanicalSpawner.asResource("spawner/" + id));
    }

    @Override
    public final String getName() {
        return "Mechanical Spawner's spawner recipes.";
    }

}
