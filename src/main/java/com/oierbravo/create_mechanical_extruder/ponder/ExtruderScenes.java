package com.oierbravo.create_mechanical_extruder.ponder;

import com.oierbravo.create_mechanical_extruder.components.extruder.andesite.ExtruderBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ExtruderScenes {
    public static void extruderBasic(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("extruder", "Block generation");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);

        //Cogs
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 5), Direction.DOWN); // back cog
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 2, 5), Direction.DOWN); // back cog
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 2, 4), Direction.DOWN); //shaft
        scene.idle(5);

        scene.world().showSection(util.select().position(2, 1, 3), Direction.DOWN); //andesite casing
        scene.idle(5);

        BlockPos extruderPos = util.grid().at(2, 2, 3);
        Selection extruderS = util.select().position(2, 2, 3);

        scene.world().setKineticSpeed(extruderS, 0);
        scene.world().showSection(extruderS, Direction.DOWN);

        scene.idle(5);
        scene.effects().indicateSuccess(extruderPos);
        scene.idle(10);
        scene.overlay().showText(50)
                .text("The Extruder uses rotational force to generate blocks")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().topOf(extruderPos));
        scene.idle(60);

        scene.overlay().showOutline(PonderPalette.GREEN, new Object(), util.select().position(extruderPos.relative(Direction.WEST)), 60);
        scene.overlay().showOutline(PonderPalette.GREEN, new Object(), util.select().position(extruderPos.relative(Direction.EAST)), 60);
        scene.overlay().showOutline(PonderPalette.GREEN, new Object(), util.select().position(extruderPos.relative(Direction.DOWN)), 60);

        Vec3 extruderSide = util.vector().blockSurface(extruderPos, Direction.WEST);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Generation depends on side blocks.")
                .pointAt(extruderSide)
                .placeNearTarget();
        scene.idle(90);


        scene.world().showSection(util.select().position(3, 2, 4), Direction.DOWN);
        scene.world().showSection(util.select().position(4, 2, 3), Direction.DOWN);
        scene.world().showSection(util.select().position(3, 2, 2), Direction.DOWN);

        scene.world().showSection(util.select().position(1, 2, 4), Direction.DOWN);
        scene.world().showSection(util.select().position(0, 2, 3), Direction.DOWN);
        scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);

        scene.idle(5);
        scene.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 2, 3), Direction.DOWN);
        scene.idle(5);

        scene.world().setKineticSpeed(extruderS, 32);

        Class<ExtruderBlockEntity> type = ExtruderBlockEntity.class;
        scene.world().modifyBlockEntity(extruderPos, type, pte -> pte.getExtrudingBehaviour()
                .start());
        ItemStack output = new ItemStack(Blocks.COBBLESTONE);
        scene.world().modifyBlockEntity(extruderPos, ExtruderBlockEntity.class,
                ms -> ms.outputInventory.setStackInSlot(0, output));

        scene.overlay().showText(50)
                .text("When the process is done, the result can be obtained via Right-click")
                .pointAt(util.vector().blockSurface(extruderPos, Direction.WEST))
                .placeNearTarget();
        scene.idle(60);


        scene.overlay().showControls(util.vector().blockSurface(extruderPos, Direction.NORTH), Pointing.RIGHT, 40).rightClick();

        scene.idle(50);

        scene.addKeyframe();

        scene.idle(20);
        scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(2, 1, 2), Direction.NORTH);
        scene.world().showSection(util.select().position(2, 1, 1), Direction.NORTH);
        scene.world().showSection(util.select().position(2, 1, 0), Direction.NORTH);

        scene.world().modifyBlockEntity(extruderPos, type, pte -> pte.getExtrudingBehaviour()
                .start());
        scene.idle(35);
        scene.world().createItemOnBelt(util.grid().at(2, 1, 2), Direction.UP, output);

        scene.addKeyframe();
        scene.idle(35);
        scene.overlay().showText(50)
                .text("The outputs can also be extracted by automation")
                .pointAt(util.vector().blockSurface(extruderPos, Direction.WEST)
                        .add(-.5, .4, 0))
                .placeNearTarget();
        scene.idle(60);


        scene.idle(10);
        scene.idle(40);
        Vec3 filter = util.vector().of(2.5, 3.1, 3.1);
        scene.overlay().showFilterSlotInput(filter, Direction.UP, 80);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("When a conbination of ingredient has multiple possible outcomes, the filter slot can specify it")
                .pointAt(filter)
                .placeNearTarget();
        scene.idle(110);
    }
}
