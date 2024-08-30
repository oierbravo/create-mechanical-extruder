package com.oierbravo.createmechanicalextruder.components.extruder;

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

public class RecipeConditionsBehaviour<R extends IRecipeWithConditions> extends BlockEntityBehaviour {
    public static final BehaviourType<RecipeConditionsBehaviour<?>> TYPE = new BehaviourType<>();

    public RecipeConditionsBehaviour.RecipeConditionsSpecifics<R> specifics;

    private boolean meetsConditions;
    private boolean meetsIngredients;
    private boolean hasEnoughOutput;
    private ArrayList<String> missingConditions;

    public <T extends SmartBlockEntity & RecipeConditionsBehaviour.RecipeConditionsSpecifics<R>> RecipeConditionsBehaviour(T te) {
        super(te);
        this.specifics = te;
        this.meetsIngredients = false;
        this.missingConditions = new ArrayList<>();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public boolean meetsConditions(){
        return meetsConditions;
    }

    public <T> boolean checkConditions(Optional<R> pRecipe, Level pLevel, T pBlockEntity) {
        missingConditions = new ArrayList<>();
        boolean result = true;

        if(pRecipe.isEmpty() || !specifics.matchIngredients(pRecipe.get())){
            meetsIngredients = false;
            missingConditions.add("ingredients");
            blockEntity.sendData();
            return false;
        }
        meetsIngredients = true;


        hasEnoughOutput = specifics.hasEnoughOutputSpace();
        if(!hasEnoughOutput){
            missingConditions.add("output");
            result = false;
        }

        meetsConditions = checkConditions(pRecipe.get(), pLevel, specifics);
        if(!meetsConditions){
            result = false;
        }

        blockEntity.sendData();
        return result;
    }
    private boolean checkConditions(R pRecipe, Level pLevel, RecipeConditionsSpecifics<R> pSpecifics){
        boolean result = true;
        for (Map.Entry<RecipeConditionType<?>,RecipeCondition> entry : pRecipe.getRecipeConditions().entrySet()) {
            if(!checkCondition(entry.getValue(), pLevel, (BlockEntity) pSpecifics)){
                missingConditions.add(entry.getKey().getId());
                result = false;
            }

        }
        return result;
    }
    private boolean checkCondition(RecipeCondition value, Level pLevel, BlockEntity pSpecifics){
        if(value.test(pLevel, pSpecifics))
            return true;
        return false;
    }
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, boolean added) {
        if(missingConditions.isEmpty())
            return false;

        for(String conditionId : missingConditions){
            ModLang.translate("ui.recipe_condition." + conditionId).style(ChatFormatting.RED).forGoggles(tooltip,1);
            added = true;
        }
        return added;
    }



        @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        ListTag missingConditionsTag = new ListTag();
        for(String missingId : missingConditions){
            CompoundTag tag = new CompoundTag();
            tag.putString("t",missingId);
            missingConditionsTag.add(tag);
        }
        compound.put("MissingConditions", missingConditionsTag);
        super.write(compound, clientPacket);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket){

        missingConditions = new ArrayList<>();
        ListTag missingConditionsTag = compound.getList("MissingConditions", Tag.TAG_COMPOUND);
        for( Tag tag : missingConditionsTag ){
            CompoundTag conditionTag = (CompoundTag) tag;
            missingConditions.add(conditionTag.getString("t"));
        }
        super.read(compound, clientPacket);
    }

    public interface RecipeConditionsSpecifics<R extends IRecipeWithConditions> {
        boolean hasEnoughOutputSpace();
        boolean matchIngredients(R recipe);
    }
}
