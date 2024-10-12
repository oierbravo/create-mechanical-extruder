[CREATE]: https://www.curseforge.com/minecraft/mc-mods/create
# Create Mechanical Extruder

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
- Per recipe biome, minY, maxY, speed requirements.

## Extruding recipes
- Left and right blocks/fluids are `ingredients` in any order.
- `result` is an Item or Block
- Required bonks can be specified with `requiredBonks` (int)
- Required catalyst can be specified with `catalyst` (block)
- Required biome can be specified with `biome` (resource)
- Required minimum Y level can be specified with `min_height` (int)
- Required maximum Y level can be specified with `max_height` (int)
- Required speed can be specified with `min_speed` (float)

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
    "requiredBonks":10
}
```

Required biome (Tag)
```
{
  "type": "create_mechanical_extruder:extruding",
  "biome": "minecraft:is_end",
  "ingredients": [
    {
      "item": "minecraft:blue_ice"
    },
    {
      "amount": 1000,
      "fluid": "minecraft:lava",
      "nbt": {}
    }
  ],
  "result": {
    "item": "minecraft:end_stone"
  }
}
```

Required biome (Id)
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
    "result": {
      "item": "minecraft:netherrack"
    },
    "biome":"minecraft:plains"
}
```

Required biome (Tag)
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
    "result": {
      "item": "minecraft:netherrack"
    },
    "biome":"is_nether"
}
```

Required min/max height (Y level)
```
{
  "type": "create_mechanical_extruder:extruding",
  "ingredients": [
    {
      "amount": 1000,
      "fluid": "minecraft:lava",
      "nbt": {}
    },
    {
      "amount": 1000,
      "fluid": "minecraft:water",
      "nbt": {}
    }
  ],
  "max_height": "-10",
  "min_height": "-50",
  "result": {
    "item": "minecraft:andesite"
  }
}
```


Required minimum speed.
```
{
  "type": "create_mechanical_extruder:extruding",
  "ingredients": [
    {
      "amount": 1000,
      "fluid": "minecraft:lava",
      "nbt": {}
    },
    {
      "amount": 1000,
      "fluid": "minecraft:water",
      "nbt": {}
    }
  ],
  "max_height": "64",
  "min_speed": "128.0",
  "result": {
    "item": "minecraft:deepslate"
  }
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

