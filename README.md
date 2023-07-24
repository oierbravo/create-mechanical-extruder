
[CREATE]: https://www.curseforge.com/minecraft/mc-mods/create
[DOWNLOAD]: https://www.curseforge.com/minecraft/mc-mods/create-mechanical-extruder/files
[CURSEFORGE]: https://www.curseforge.com/minecraft/mc-mods/create-mechanical-extruder
[MODRINTH]: https://modrinth.com/mod/create-mechanical-extruder
[ISSUES]: https://github.com/oierbravo/create-mechanical-extruder/issues

<!-- modrinth_exclude.start -->
# Create Mechanical Extruder
[![Release](https://img.shields.io/github/v/release/oierbravo/create-mechanical-extruder?label=Version&sort=semver)][DOWNLOAD]
[![Downloads](http://cf.way2muchnoise.eu/full_686100_downloads.svg)][CURSEFORGE]
[![Version](http://cf.way2muchnoise.eu/versions/686100.svg)][DOWNLOAD]
[![Issues](https://img.shields.io/github/issues/oierbravo/create-mechanical-extruder?label=Issues)][ISSUES]
[![Modrinth](https://modrinth-utils.vercel.app/api/badge/downloads?id=hGAlcCDJ&logo=true)][MODRINTH]
<!-- modrinth_exclude.end -->

[![](https://img.shields.io/badge/REQUIRES%20CREATE%20v0.5.1c%20for%201.18.2%2F1.19.2-gold?logo=curseforge&labelColor=gray&style=for-the-badge)][CREATE]

A mechanical extruder block. Can be used to generate any block or item from adjacent blocks/fluids.
This mod it's meant to be used in modpacks. Only contains very basic recipes.

Heavily inspired on Thermal Expansions Igneous Extruder.

## Features
- Andesite based kinetic block.
- Filter functionality for selecting output when recipe collides.
- Ponder scene.
- Shift+right click with empty hand to extract content.
- Extraction via automation.
- JEI integration.

## Extruding recipes
- Left and right blocks/fluids are `ingredients` in any order.
- `results` is an Item or Block
- Required bonks can be specified with `requiredBonks`
CobbleGen example (already in the mod)
```
{
  "type": "create_mechanical_extruder:extruding",
  "ingredients": [

    {
      "fluid": "minecraft:water",
      "amount": 1000
    },
    {
      "fluid": "minecraft:lava",
      "amount": 1000
    }
  ],
  "result": {
    "item": "minecraft:cobblestone"
  }
}
```
BasaltGen example (already in the mod)
```
{
  "type": "create_mechanical_extruder:extruding",
  "ingredients": [
    {
      "fluid": "minecraft:lava",
      "amount": 1000
    },
    {
      "item": "minecraft:blue_ice"
    }
  ],
  "catalyst": {
    "item": "minecraft:soul_sand"
  },
  "result": {
    "item": "minecraft:basalt"
  }
}
```
Required Bonks example
```
{
  "type": "create_mechanical_extruder:extruding",
  "ingredients": [
    {
      "fluid": "minecraft:lava",
      "amount": 1000
    },
    {
      "item": "minecraft:blue_ice"
    }
  ],
  "catalyst": {
    "item": "minecraft:soul_sand"
  },
  "result": {
    "item": "minecraft:basalt"
  },
  requiredBonks:10
}
```

### KubeJS integration:

```
//event.recipes.createsifterSifting(output, input[])

//EXAMPLE
event.recipes.createMechanicalExtruderExtruding(Item.of('minecraft:sand'),[Item.of('minecraft:cobblestone'),Item.of('minecraft:stone')])
//With catalyst
event.recipes.createMechanicalExtruderExtruding(Item.of('minecraft:dirt'),[Item.of('minecraft:sand'),Item.of('minecraft:stone')]).withCatalyst('minecraft:clay')
// With bonks 
event.recipes.createMechanicalExtruderExtruding(Item.of('minecraft:dirt'),[Fluid.of('minecraft:lava'),Item.of('minecraft:stone')]).withCatalyst('minecraft:clay').requiredBonks(10)
```

**Thanks to the Creators of Create.**

Code inspiration from the [Create](https://www.curseforge.com/minecraft/mc-mods/create "Create") mod itself.

