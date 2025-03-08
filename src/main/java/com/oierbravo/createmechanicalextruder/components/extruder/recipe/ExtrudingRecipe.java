package com.oierbravo.createmechanicalextruder.components.extruder.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.oierbravo.createmechanicalextruder.components.extruder.ExtruderBlockEntity;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.*;
import com.oierbravo.mechanical_lemon_lib.foundation.recipe.requirements.SpeedRequirement;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ExtrudingRecipe extends BaseRecipe<RecipeInput, ExtrudingRecipe.ExtrudingRecipeParams> {

    public static Comparator<? super ExtrudingRecipe> hasCatalyst;
    private ResourceLocation id;
    private NonNullList<Ingredient> itemIngredients;
    private NonNullList<FluidIngredient> fluidIngredients;
    private ItemStack catalyst;


    private ProcessingOutput result;

    private int requiredBonks;

    private final Map<RecipeRequirementType<?>, RecipeRequirement> recipeRequirements = new HashMap<>();


    public static final List<RecipeRequirementType<?>> enabledRecipeRequirements = List.of(
         //   BiomeRequirement.TYPE,
         //   MinHeightRequirement.TYPE,
         //   MaxHeightRequirement.TYPE,
            SpeedRequirement.TYPE
    );

    public ExtrudingRecipe(ExtrudingRecipeParams params) {
        super(params);
        this.id = params.id;
        this.result = params.result;
        this.itemIngredients = params.itemIngredients;
        this.fluidIngredients = params.fluidIngredients;
        this.catalyst = params.catalyst;
        this.requiredBonks = params.requiredBonks;

        params.recipeRequirements.forEach(
                recipeRequirement -> recipeRequirements.put(recipeRequirement.getType(), recipeRequirement)
        );
    }



    public static boolean match(ExtruderBlockEntity extruderBlockEntity, ExtrudingRecipe recipe){

        FilteringBehaviour filter = extruderBlockEntity.getFilter();
        if (filter == null)
            return false;
        boolean filterTest = filter.test(recipe.getResultItem(extruderBlockEntity.getLevel().registryAccess()));
        if(!getAllIngredientsStringList(recipe).equals(extruderBlockEntity.getAllIngredientsStringList()))
            return false;

        if(!recipe.catalyst.isEmpty() && !recipe.catalyst.is(extruderBlockEntity.getCatalystItem()))
            return false;

        if (!filterTest)
            return false;
        return true;
    }

    public boolean hasCatalyst() {
        return !this.getCatalyst().isEmpty();
    }
    public List<Ingredient> getItemIngredients() {
        return itemIngredients;
    }

    public List<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    public static List<String> getAllIngredientsStringList(ExtrudingRecipe recipe) {
        List<String> list = new ArrayList<>();

        recipe.getItemIngredients().forEach(ingredient -> list.add(ingredient.getItems()[0].getItem().toString()));
        recipe.getFluidIngredients().forEach(ingredient -> list.add(ingredient.getMatchingFluidStacks().get(0).getFluid().getFluidType().getDescriptionId()));
        Collections.sort(list);
        return list;
    }


    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return result.rollOutput();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.rollOutput();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;

    }


    public ItemStack getResultItem() {
        return result.rollOutput();
    }
    public ProcessingOutput getResult(){
        return result;
    }

    public ItemStack getCatalyst() {
        return catalyst;
    }

    public int getRequiredBonks() {
        return requiredBonks;
    }


    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public @NotNull Map<RecipeRequirementType<?>, RecipeRequirement> getRecipeRequirements() {
        return recipeRequirements;
    }

    @Override
    public boolean checkRequirements(Level level, BlockEntity blockEntity) {
        return false;
    }

    public static <T> boolean hasCatalyst(RecipeHolder<ExtrudingRecipe> extrudingRecipeRecipeHolder) {
        return !extrudingRecipeRecipeHolder.value().catalyst.isEmpty();
    }

    public static class Type implements RecipeType<ExtrudingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final RecipeType<ExtrudingRecipe> RECIPE_TYPE = new Type();
        public static final String ID = "extruding";
    }

    public static class ExtrudingRecipeParams extends BaseRecipeParams{
        protected NonNullList<Ingredient> itemIngredients;
        protected ProcessingOutput result;
        protected NonNullList<FluidIngredient> fluidIngredients;
        protected ItemStack catalyst;

        protected int requiredBonks;

        //protected BiomeRequirement biome;

        public ArrayList<RecipeRequirement> recipeRequirements;

        protected ExtrudingRecipeParams(ResourceLocation id) {
            super(id);
            itemIngredients = NonNullList.create();
            result = ProcessingOutput.EMPTY;
            fluidIngredients = NonNullList.create();
            catalyst = ItemStack.EMPTY;
            requiredBonks = 1;
            recipeRequirements = new ArrayList<>();
        }

    }
    public static class Serializer extends BaseRecipeSerializer<ExtrudingRecipe, ExtrudingRecipeBuilder> implements RecipeSerializer<ExtrudingRecipe> {
        public static final Serializer INSTANCE = new Serializer(ExtrudingRecipe.enabledRecipeRequirements);

        public final StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

        private ExtrudingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ResourceLocation recipeId = ResourceLocation.STREAM_CODEC.decode(buffer);

            NonNullList<Ingredient> ingredients = CatnipStreamCodecBuilders.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).decode(buffer);
            NonNullList<FluidIngredient> fluidIngredients = CatnipStreamCodecBuilders.nonNullList(FluidIngredient.STREAM_CODEC).decode(buffer);
            ProcessingOutput result = ProcessingOutput.STREAM_CODEC.decode(buffer);
            int requiredBonks = ByteBufCodecs.INT.decode(buffer);
            //float requiredSpeed = ByteBufCodecs.FLOAT.decode(buffer);

            return new ExtrudingRecipeBuilder(recipeId).withItemIngredients(ingredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .requiredBonks(requiredBonks)
                    //.withRequirement(SpeedRequirement.of(requiredSpeed))
                    .build();
        }

        private void toNetwork(RegistryFriendlyByteBuf buffer, ExtrudingRecipe extrudingRecipe) {
            ResourceLocation.STREAM_CODEC.encode(buffer, extrudingRecipe.id);

            CatnipStreamCodecBuilders.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).encode(buffer, extrudingRecipe.itemIngredients);
            CatnipStreamCodecBuilders.nonNullList(FluidIngredient.STREAM_CODEC).encode(buffer, extrudingRecipe.fluidIngredients);
            ProcessingOutput.STREAM_CODEC.encode(buffer, extrudingRecipe.getResult());
            ByteBufCodecs.INT.encode(buffer,extrudingRecipe.getRequiredBonks());
            //ByteBufCodecs.FLOAT.encode(buffer,extrudingRecipe.getSpeedRequirement())
            ;
        }
        //public static final MapCodec<ExtrudingRecipe> CODEC = AllRecipeTypes.CODEC.dispatchMap(ExtrudingRecipe::getId, ExtrudingCodec);

        public static final MapCodec<ExtrudingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance
                        .group(
                                Codec.either(Ingredient.CODEC, FluidIngredient.CODEC).listOf().fieldOf("ingredients").forGetter(i -> {
                                    List<Either<Ingredient, FluidIngredient>> list = new ArrayList<>();
                                    i.getIngredients().forEach(o -> list.add(Either.left(o)));
                                    i.getFluidIngredients().forEach(o -> list.add(Either.right(o)));
                                    return list;
                                }),

                                ProcessingOutput.CODEC.fieldOf("result").forGetter(ExtrudingRecipe::getResult),
                                ItemStack.CODEC.optionalFieldOf("catalyst", ItemStack.EMPTY).forGetter(ExtrudingRecipe::getCatalyst),
                                Codec.INT.optionalFieldOf("requiredBonks",1).forGetter(ExtrudingRecipe::getRequiredBonks),
                                //Codec.FLOAT.optionalFieldOf(SpeedRequirement.TYPE.getId(),1f).forGetter(ExtrudingRecipe::getSpeedRequirement),
                                ICondition.LIST_CODEC.optionalFieldOf(ConditionalOps.DEFAULT_CONDITIONS_KEY, List.of()).forGetter(ExtrudingRecipe::getConditions)
                        ).apply(instance, (ingredients, processingOutput, catalyst, requiredBonks/*, requiredSpeed,*/, iConditions) -> {
                            String recipeId = instance.toString();
                            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(ResourceLocation.parse("create_mechanical_extruder:extruding"));

                            NonNullList<Ingredient> ingredientList = NonNullList.create();
                            NonNullList<FluidIngredient> fluidIngredientList = NonNullList.create();

                            for (Either<Ingredient, FluidIngredient> either : ingredients) {
                                either.left().ifPresent(ingredientList::add);
                                either.right().ifPresent(fluidIngredientList::add);
                            }

                            builder
                                    .withItemIngredients(ingredientList)
                                    .withFluidIngredients(fluidIngredientList)
                                    .withSingleItemOutput(processingOutput)
                                    .requiredBonks(requiredBonks)
                                    .withCatalyst(catalyst);
                                 //   .withRequirement(SpeedRequirement.of(requiredSpeed));

                            return builder.build();


                        })
        );

        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(CreateMechanicalExtruder.MODID,"extruding");

        public Serializer(List<RecipeRequirementType<?>> pEnabledRecipeRequirements) {
            super(pEnabledRecipeRequirements);
        }

        @Override
        protected ExtrudingRecipeBuilder readFromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            return null;
        }

        @Override
        protected ExtrudingRecipeBuilder readFromBuffer(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            return null;
        }

        @Override
        protected void writeToJson(JsonObject jsonObject, ExtrudingRecipe extrudingRecipe) {

        }

        @Override
        protected void writeToBuffer(FriendlyByteBuf friendlyByteBuf, ExtrudingRecipe extrudingRecipe) {

        }

        @Override
        public @NotNull MapCodec<ExtrudingRecipe> codec() {
            return CODEC;
        }

        /*@Override
                public @NotNull MapCodec<ExtrudingRecipe> codec() {
                    return CODEC;
                }
        */
        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ExtrudingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
        /*@Override
        public ExtrudingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();

            ProcessingOutput result = ProcessingOutput.EMPTY;
            ItemStack catalyst = ItemStack.EMPTY;
            int requiredBonks = 1;

            ArrayList<RecipeRequirement> recipeRequirements = new ArrayList<>();

            for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
                if (FluidIngredient.isFluidIngredient(je))
                    fluidIngredients.add(FluidIngredient.deserialize(je));
                else
                    itemIngredients.add(Ingredient.fromJson(je));
            }
            result = ProcessingOutput.deserialize(GsonHelper.getAsJsonObject(json, "result"));

            if(GsonHelper.isValidNode(json,"catalyst")){
                catalyst = ShapedRecipe.itemStackFromJson( GsonHelper.getAsJsonObject(json, "catalyst"));
            }

            if(GsonHelper.isValidNode(json,"requiredBonks")){
                requiredBonks = GsonHelper.getAsInt(json,"requiredBonks");
            }
            ExtrudingRecipe.enabledRecipeRequirements.forEach(recipeRequirementType -> {
                if (GsonHelper.isValidNode(json, recipeRequirementType.getId())) {
                    recipeRequirements.add(recipeRequirementType.fromJson(json));
                }
            });


            return builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks)
                    .withRequirements(recipeRequirements)
                    .build();
        }
        public JsonObject toJson(JsonObject pJson, ExtrudingRecipe pRecipe) {
            JsonArray jsonIngredients = new JsonArray();

            pRecipe.itemIngredients.forEach(i -> jsonIngredients.add(i.toJson()));
            pRecipe.fluidIngredients.forEach(i -> jsonIngredients.add(i.serialize()));

            pJson.add("result", pRecipe.result.serialize());

            pJson.add("ingredients", jsonIngredients);

            if(pRecipe.hasCatalyst())
                pJson.add("catalyst", new ProcessingOutput(pRecipe.getCatalyst(),1).serialize());

            if (pRecipe.getRequiredBonks() > 1)
                pJson.addProperty("requiredBonks", pRecipe.getRequiredBonks());

            for (Map.Entry<RecipeRequirementType<?>, RecipeRequirement> entry : pRecipe.recipeRequirements.entrySet()) {
                pJson = entry.getKey().toJson(pJson, entry.getValue());
            }


            return pJson;
        }

        @Override
        public ExtrudingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            ExtrudingRecipeBuilder builder = new ExtrudingRecipeBuilder(id);
            NonNullList<Ingredient> itemIngredients = NonNullList.create();
            NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
            ProcessingOutput result = ProcessingOutput.EMPTY;
            ItemStack catalyst = ItemStack.EMPTY;
            int requiredBonks = 1;

            ArrayList<RecipeRequirement> recipeRequirements = new ArrayList<>();


            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                itemIngredients.add(Ingredient.fromNetwork(buffer));

            size = buffer.readVarInt();
            for (int i = 0; i < size; i++)
                fluidIngredients.add(FluidIngredient.read(buffer));

            result = ProcessingOutput.read(buffer);
            catalyst = buffer.readItem();
            requiredBonks = buffer.readInt();

            ExtrudingRecipe.enabledRecipeRequirements.forEach(recipeRequirementType -> {
                recipeRequirements.add(recipeRequirementType.fromNetwork(buffer));
            });

            return builder.withItemIngredients(itemIngredients)
                    .withSingleItemOutput(result)
                    .withFluidIngredients(fluidIngredients)
                    .withCatalyst(catalyst)
                    .requiredBonks(requiredBonks)
                    .withRequirements(recipeRequirements)
                    .build();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtrudingRecipe pRecipe) {
            NonNullList<Ingredient> itemIngredients = pRecipe.itemIngredients;
            NonNullList<FluidIngredient> fluidIngredients = pRecipe.fluidIngredients;
            ProcessingOutput result = pRecipe.result;
            ItemStack catalyst = pRecipe.catalyst;
            int requiredBonks = pRecipe.requiredBonks;

            buffer.writeVarInt(itemIngredients.size());
            itemIngredients.forEach(i -> i.toNetwork(buffer));
            buffer.writeVarInt(fluidIngredients.size());
            fluidIngredients.forEach(i -> i.write(buffer));
            result.write(buffer);
            buffer.writeItemStack(catalyst, false);
            buffer.writeInt(requiredBonks);


            ExtrudingRecipe.enabledRecipeRequirements.forEach(recipeRequirementType -> {
                recipeRequirementType.toNetwork(buffer,pRecipe.getRequirement(recipeRequirementType));
            });

        }*/


    }

    private ResourceLocation getId() {
        return id;
    }

    private Float getSpeedRequirement() {
        @NotNull Map<RecipeRequirementType<?>, RecipeRequirement> reqs =  this.getRecipeRequirements();
        SpeedRequirement sp = (SpeedRequirement) this.getRequirement(SpeedRequirement.TYPE);
        //if(this.getRecipeRequirements().get(SpeedRequirement.TYPE).isPresent())
        //    return (Float) this.getRecipeRequirements().get(SpeedRequirement.TYPE).getValue();
        return SpeedRequirement.EMPTY.getValue();
    }

   /* private List<RecipeRequirement> getRecipeRequirementsList() {
        List<RecipeRequirement> recipeRequirements = List.of();
        ExtrudingRecipe.enabledRecipeRequirements.forEach(recipeRequirementType -> {
            recipeRequirements.add(getRequirement(recipeRequirementType));
        });
        return recipeRequirements;
    }*/

}
