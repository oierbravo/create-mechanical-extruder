package com.oierbravo.createmechanicalextruder.foundation.recipe;

import com.oierbravo.createmechanicalextruder.foundation.utility.ModLang;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RecipeRequirementsBehaviour<R extends IRecipeWithRequirements> extends BlockEntityBehaviour {
    public static final BehaviourType<RecipeRequirementsBehaviour<?>> TYPE = new BehaviourType<>();

    public RecipeRequirementsSpecifics<R> specifics;

    private boolean meetsRequirements;
    private boolean meetsIngredients;
    private boolean hasEnoughOutput;
    private ArrayList<String> missingRequirements;

    public <T extends SmartBlockEntity & RecipeRequirementsSpecifics<R>> RecipeRequirementsBehaviour(T te) {
        super(te);
        this.specifics = te;
        this.meetsIngredients = false;
        this.missingRequirements = new ArrayList<>();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public boolean meetsRequirements(){
        return meetsRequirements;
    }

    public <T> boolean checkRequirements(Optional<R> pRecipe, Level pLevel, T pBlockEntity) {
        missingRequirements = new ArrayList<>();
        boolean result = true;

        if(pRecipe.isEmpty() || !specifics.matchIngredients(pRecipe.get())){
            meetsIngredients = false;
            missingRequirements.add("ingredients");
            blockEntity.sendData();
            return false;
        }
        meetsIngredients = true;


        hasEnoughOutput = specifics.hasEnoughOutputSpace();
        if(!hasEnoughOutput){
            missingRequirements.add("output");
            result = false;
        }

        meetsRequirements = checkRequirements(pRecipe.get(), pLevel, specifics);
        if(!meetsRequirements){
            result = false;
        }

        blockEntity.sendData();
        return result;
    }
    private boolean checkRequirements(R pRecipe, Level pLevel, RecipeRequirementsSpecifics<R> pSpecifics){
        boolean result = true;
        for (Map.Entry<RecipeRequirementType<?>, RecipeRequirement> entry : pRecipe.getRecipeRequirements().entrySet()) {
            if(!checkRequirement(entry.getValue(), pLevel, (BlockEntity) pSpecifics)){
                missingRequirements.add(entry.getKey().getId());
                result = false;
            }

        }
        return result;
    }
    private boolean checkRequirement(RecipeRequirement value, Level pLevel, BlockEntity pSpecifics){
        if(value.test(pLevel, pSpecifics))
            return true;
        return false;
    }
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, boolean added) {
        if(missingRequirements.isEmpty())
            return false;

        for(String requirementId : missingRequirements){
            ModLang.translate("goggles.recipe_requirement." + requirementId).style(ChatFormatting.RED).forGoggles(tooltip,1);
            added = true;
        }
        return added;
    }



        @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        ListTag missingRequirementsTag = new ListTag();
        for(String missingId : missingRequirements){
            CompoundTag tag = new CompoundTag();
            tag.putString("t",missingId);
            missingRequirementsTag.add(tag);
        }
        compound.put("MissingRequirements", missingRequirementsTag);
        super.write(compound, clientPacket);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket){

        missingRequirements = new ArrayList<>();
        ListTag missingRequirementsTag = compound.getList("MissingRequirements", Tag.TAG_COMPOUND);
        for( Tag tag : missingRequirementsTag ){
            CompoundTag requirementsTag = (CompoundTag) tag;
            missingRequirements.add(requirementsTag.getString("t"));
        }
        super.read(compound, clientPacket);
    }

    public interface RecipeRequirementsSpecifics<R extends IRecipeWithRequirements> {
        boolean hasEnoughOutputSpace();
        boolean matchIngredients(R recipe);
    }
}
