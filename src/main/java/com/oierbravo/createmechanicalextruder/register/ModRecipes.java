package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderBlockEntity;
import com.oierbravo.createmechanicalextruder.components.extruder.recipe.ExtrudingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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


    /*public static final Supplier<RecipeType<ExtrudingRecipe>> EXTRUDING_TYPE =
            RECIPE_TYPES.register("extruding",() -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ExtrudingRecipe.Type.ID.toString();
                }
            });*/
    public static final Supplier<ExtrudingRecipe.Serializer> EXTRUDING_SERIALIZER =
            SERIALIZERS.register("extruding", () -> ExtrudingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {

        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);

    }
    public static Optional<RecipeHolder<ExtrudingRecipe>> find(SingleRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getRecipeFor(ModRecipes.EXTRUDING_TYPE.get() ,pInput,pLevel);
    }
    public static Optional<RecipeHolder<ExtrudingRecipe>> findExtruding(ExtruderBlockEntity extruder, Level level){
        if(level.isClientSide())
            return Optional.empty();
        List<RecipeHolder<ExtrudingRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE);

        //return Optional.empty();
        return level.getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE)
                .stream()
                    .filter(extruder::matchIngredients)
                    .sorted(Comparator.comparing(ExtrudingRecipe::hasCatalyst,Comparator.reverseOrder()))
                    .findFirst();

    }
}
