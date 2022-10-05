package com.oierbravo.createmechanicalextruder;

import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.oierbravo.createmechanicalextruder.register.ModTiles;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("create_mechanical_extruder")
public class CreateMechanicalExtruder
{
    public static final String MODID = "create_mechanical_extruder";
    public static final String DISPLAY_NAME = "Create Mechanical Extruder";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static IEventBus modEventBus;

    public static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(MODID);

    public CreateMechanicalExtruder()
    {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ModBlocks.register();
        ModTiles.register();
        ModRecipes.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> ModPartials::load);
        generateLangEntries();
    }
    private void generateLangEntries(){

        registrate().addRawLang("create.recipe.extruding", "Extruding recipe");
    }
    public static CreateRegistrate registrate() {
        return registrate.get();
    }



    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
