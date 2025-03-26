package com.oierbravo.createmechanicalextruder;

import com.oierbravo.createmechanicalextruder.components.extruder.andesite.ExtruderBlockEntity;
import com.oierbravo.createmechanicalextruder.components.extruder.brass.BrassExtruderBlockEntity;
import com.oierbravo.createmechanicalextruder.infrastructure.config.ModConfigs;
import com.oierbravo.createmechanicalextruder.infrastructure.data.ModDataGen;
import com.oierbravo.createmechanicalextruder.ponder.ModPonderPlugin;
import com.oierbravo.createmechanicalextruder.register.ModBlockEntities;
import com.oierbravo.createmechanicalextruder.register.ModBlocks;
import com.oierbravo.createmechanicalextruder.register.ModPartials;
import com.oierbravo.createmechanicalextruder.register.ModRecipes;
import com.oierbravo.mechanicals.register.MechanicalCreativeModeTabs;
import com.oierbravo.mechanicals.utility.RegistrateLangBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.oierbravo.createmechanicalextruder.ModConstants.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("create_mechanical_extruder")
public class CreateMechanicalExtruder
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static IEventBus modEventBus;

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(MODID).defaultCreativeTab(MechanicalCreativeModeTabs.MAIN_TAB.getKey());

    static {
        REGISTRATE.setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }
    public CreateMechanicalExtruder(IEventBus modEventBus, ModContainer modContainer)
    {
        REGISTRATE.registerEventListeners(modEventBus);

        ModLoadingContext modLoadingContext = ModLoadingContext.get();


        ModBlocks.register();
        ModBlockEntities.register();
        //ModCreativeTabs.register(modEventBus);
        ModConfigs.register(modLoadingContext, modContainer);

        ModRecipes.register(modEventBus);
        modEventBus.addListener(ModDataGen::gatherData);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::doClientStuff);

        generateLangEntries();
    }
    @net.neoforged.bus.api.SubscribeEvent
    public void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        ExtruderBlockEntity.registerCapabilities(event);
        BrassExtruderBlockEntity.registerCapabilities(event);
    }
    private void doClientStuff(final FMLClientSetupEvent event) {
        ModPartials.init();

        PonderIndex.addPlugin(new ModPonderPlugin());
        RenderType cutout = RenderType.cutoutMipped();

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MECHANICAL_EXTRUDER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MECHANICAL_BRASS_EXTRUDER.get(), cutout);
    }
    private void generateLangEntries(){
        new RegistrateLangBuilder(MODID, registrate())

                .addRaw("itemGroup.create_mechanical_extruder:main", ModConstants.DISPLAY_NAME)
                .add("recipe.extruding", "Extruding recipe")

                .add("goggles.bonks", "%1$s bonks")
                .add("ponder.extruder.header", "Block generation")
                .add("ponder.extruder.text_1", "The Extruder uses rotational force to generate blocks")
                .add("ponder.extruder.text_2", "Generation depends on side & below blocks.")
                .add("ponder.extruder.text_3", "When the process is done, the result can be obtained via Right-click")
                .add("ponder.extruder.text_4", "The outputs can also be extracted by automation")
                .add("ponder.extruder.text_5", "When a conbination of ingredient has multiple possible outcomes, the filter slot can specify it")
                .add("ui.recipe_requirement.advanced.title", "Brass extruder");


    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
    public static Logger logger(){
        return LOGGER;
    }
}
