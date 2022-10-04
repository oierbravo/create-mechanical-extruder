package com.oierbravo.createmechanicalextruder.register;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderTileEntity;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtrudingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreateMechanicalExtruder.MODID);

    public static final RegistryObject<RecipeSerializer<ExtrudingRecipe>> EXTRUDING_SERIALIZER =
            SERIALIZERS.register("extruding", () -> ExtrudingRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
    public static Optional<ExtrudingRecipe> findExtruding(ExtruderTileEntity extruder, Level level){
        if(level.isClientSide())
            return Optional.empty();
        List<ExtrudingRecipe> allExtrudingRecipes = level.getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE);

        Stream<ExtrudingRecipe> allExtrudingRecipesFiltered = allExtrudingRecipes.stream().filter(extrudingRecipe -> ExtrudingRecipe.match(extruder,extrudingRecipe));
        Optional<ExtrudingRecipe> recipe = allExtrudingRecipesFiltered.findFirst();
        return recipe;
    }
    /*public static Optional<ExtrudingRecipe> findExtruding(NonNullList<Ingredient> itemIngredients , NonNullList<FluidIngredient> fluidIngredients, Item catalyst, Level level){
        List<ExtrudingRecipe> allExtrudingRecipes = level.getRecipeManager().getAllRecipesFor(ExtrudingRecipe.Type.INSTANCE);
        Stream<ExtrudingRecipe> allExtrudingRecipesFiltered = allExtrudingRecipes.stream().filter(extrudingRecipe -> ExtrudingRecipe.match(itemIngredients,fluidIngredients,catalyst,extrudingRecipe));
        Stream<ExtrudingRecipe> allExtrudingRecipesFiltered = allExtrudingRecipes.stream().filter(extrudingRecipe -> ExtrudingRecipe.match(itemIngredients,fluidIngredients,catalyst,extrudingRecipe));
        Optional<ExtrudingRecipe> recipe = allExtrudingRecipesFiltered.findFirst();
        return recipe;
    }*/
}
