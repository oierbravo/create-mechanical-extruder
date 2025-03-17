package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.AbstractExtruderBlockEntity;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateMechanicalExtruder.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CreateMechanicalExtruder.MODID);


    public static final Supplier<RecipeType<ExtrudingRecipe>> EXTRUDING_TYPE =
            RECIPE_TYPES.register(
                    "extruding_type",
                    // We need the qualifying generic here due to generics being generics.
                    () -> RecipeType.<ExtrudingRecipe>simple(CreateMechanicalExtruder.asResource("extruding_type"))

        );

    public static final Supplier<ExtrudingRecipe.Serializer> EXTRUDING_SERIALIZER =
            SERIALIZERS.register("extruding", () -> ExtrudingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {

        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);

    }
    /*public static Optional<RecipeHolder<ExtrudingRecipe>> find(SingleRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getRecipeFor(ModRecipes.EXTRUDING_TYPE.get() ,pInput,pLevel);
    }*/
    /*ublic static <EXB extends AbstractExtruderBlockEntity> List<RecipeHolder<ExtrudingRecipe>> findMatchingIngredientExtruding(EXB extruder){
        Level level = extruder.getLevel();
        assert level != null;
        if(level.isClientSide())
            return List.of();
        return level.getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE)
                .stream()
                .filter(extruder::matchIngredients).toList();
    }*//*ublic static <EXB extends AbstractExtruderBlockEntity> List<RecipeHolder<ExtrudingRecipe>> findMatchingIngredientExtruding(EXB extruder){
        Level level = extruder.getLevel();
        assert level != null;
        if(level.isClientSide())
            return List.of();
        return level.getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE)
                .stream()
                .filter(extruder::matchIngredients).toList();
    }*/


    public static <EXB extends AbstractExtruderBlockEntity> List<ExtrudingRecipe> findRecipesWithMatchingIngredients(EXB extruder){
        if(Objects.requireNonNull(extruder.getLevel()).isClientSide())
            return List.of();

        return extruder.getLevel().getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE)
                .stream()
                    .filter(extruder::matchesIngredients)
                    .map(RecipeHolder::value)
                    //.sorted(Comparator.comparing(ExtrudingRecipe::hasCatalyst,Comparator.reverseOrder()))
                    .toList();

    }

    public static List<RecipeHolder<ExtrudingRecipe>> getAllHolders() {
        return Objects.requireNonNull(Minecraft.getInstance().getConnection())
                .getRecipeManager()
                .getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE);
    }
}
