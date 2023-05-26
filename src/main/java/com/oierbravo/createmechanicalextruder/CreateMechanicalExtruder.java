package com.oierbravo.createmechanicalextruder;

import com.oierbravo.createmechanicalextruder.register.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public CreateMechanicalExtruder()
    {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        new ModGroup("main");

        ModBlocks.register();
        ModBlockEntities.register();
        ModRecipes.register(modEventBus);
        modEventBus.addListener(this::doClientStuff);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> ModPartials::load);
        generateLangEntries();
    }
    private void generateLangEntries(){
        registrate().addRawLang("create.recipe.extruding", "Extruding recipe");

        registrate().addRawLang("create_mechanical_extruder.ponder.extruder.header", "Block generation");
        registrate().addRawLang("create_mechanical_extruder.ponder.extruder.text_1", "The Extruder uses rotational force to generate blocks");
        registrate().addRawLang("create_mechanical_extruder.ponder.extruder.text_2", "Generation depends on side & below blocks.");
        registrate().addRawLang("create_mechanical_extruder.ponder.extruder.text_3", "When the process is done, the result can be obtained via Right-click");
        registrate().addRawLang("create_mechanical_extruder.ponder.extruder.text_4", "The outputs can also be extracted by automation");

    }
    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(ModPonders::register);
    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }



    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
