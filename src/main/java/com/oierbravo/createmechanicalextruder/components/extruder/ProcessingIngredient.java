package com.oierbravo.createmechanicalextruder.components.extruder;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.crafting.Ingredient;

public class ProcessingIngredient {
   private Ingredient itemIngredient;
   private FluidIngredient fluidIngredient;
   public Type TYPE;

   public boolean test(ProcessingIngredient ingredient){
       return true;
       //if(ingredient.TYPE == Type.FLUID)
       //    return fluidIngredient.test(ingredient.get)
   }


    public enum Type {
        ITEM, FLUID
    }
}
