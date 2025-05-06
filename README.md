[CREATE]: https://www.curseforge.com/minecraft/mc-mods/create
# Create Mechanical Extruder

A mechanical extruder block. Can be used to generate any block or item from adjacent blocks/fluids.
This mod it's meant to be used in modpacks. Only contains very basic recipes.

Heavily inspired on Thermal Expansions Igneous Extruder.

## 1.21.1-2.x Version Requires Mechanicals Lib
- [Curseforge](https://www.curseforge.com/minecraft/mc-mods/mechanicals-lib "Curseforge")
- [Modrinth](https://modrinth.com/mod/mechanicals-lib "Modrinth")

## Version support & documentation
- 1.21.1: Supported. Documentation refers to this version.
- 1.20.1: Only critical issues
- 1.19.x: Unsupported: [Documentation](https://github.com/oierbravo/create-mechanical-extruder/tree/mc1.19/dev "Documentation")
- 1.18.x: Unsupported: [Documentation](https://github.com/oierbravo/create-mechanical-extruder/tree/mc1.18/dev "Documentation")

## Andesite Extruder
- Kinetic block.
- Filter functionality for selecting output when recipe collides.
- Shift+right click with empty hand to extract content.
- Extraction via automation.

## Brass Extruder
- Can consume source blocks.

## Extruding recipes
- JEI integration.
- Per recipe custom requirements.

## BlockState
### Block
```json
{
        "blocks": "minecraft:water"
},
```
### Block with State
```json
{
  "blocks": "minecraft:furnace",
  "state": {
    "lit": "true"
  }
}
```

### Input BlockStates
```json
"blockIngredients": {
    "first": {
        "blocks": "minecraft:water"
    },
    "second": {
        "blocks": "minecraft:lava"
    }
}
```
### Catalyst BlockStates
```json
"catalyst": {
  "blocks": "minecraft:obsidian"
}
```
### Required bonks (optional)
- `	"requiredBonks": 10`
- Defines how many times must hit.

### Advanced extruder (brass) per recipe (optional)
- `"advanced":"true"`
- Recipes requires brass extruder.

## Consume blocks (advanced recipe)
- Advanced extruder can consume blocks.
```json
"consumeBlocks": {
    "first": false,
    "second": true
},
```
### Recipe Requirements
- MinY/MaxY
```json
"requirements": [
    {
        "type": "mechanicals:min_y",
        "value": 0
    },
    {
        "type": "mechanicals:max_y",
        "value": 60
    }
],
```
- MinSpeed/MaxSpeed
```json
"requirements": [
    {
        "type": "mechanicals:max_speed",
        "value": 4.0
    }
],
```
- Biome
```json
"requirements": [
    {
        "type": "mechanicals:biome",
        "value": "minecraft:plains"
    }
],
```
- BiomeTag
```json
"requirements": [
    {
        "type": "mechanicals:biome_tag",
        "value": "minecraft:is_nether"
    }
],
```


### KubeJS
- Remove al Extruding recipes.
```js
ServerEvents.recipes(event => {
  event.remove({ type: 'create_mechanical_extruder:extruding' })
})
```
- Chanced output (binding)
```js
Output.of('minecraft:clay', 0.5)
Output.of('4xminecraft:clay', 0.5)
```
- BlockPredicate (binding)
```js
BlockPredicate.of('minecraft:sand')
```

- RecipeRequirement (binding)
```js
RecipeRequirement.minY(int) //ex: RecipeRequirement.minY(-10)
RecipeRequirement.maxY(int) //ex: RecipeRequirement.maxY(20)
RecipeRequirement.minSpeed(float) //ex: RecipeRequirement.minSpeed(2.0)
RecipeRequirement.maxSpeed(float) //ex: RecipeRequirement.maxSpeed(160)
RecipeRequirement.biome(string) //ex: RecipeRequirement.biome("minecraft:plains")
RecipeRequirement.maxSpeed(string)  //ex: RecipeRequirement.biome("minecraft:plains")
```

- Add recipes
```js
ServerEvents.recipes(event => {
  /** 
    create_mechanical_extruder.extruding(Output result, BlockPredicate[] inputs)
    .catalys(BlockPredicate catalyst) // optional, default: empty
    .advanced(true) //optional, default: false
    .consumeBlock(Couple consume) //optional, default: empty
  **/
  
    event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:dirt'),[BlockPredicate.of('minecraft:lava'),BlockPredicate.of('minecraft:stone')])
    	    .catalyst('minecraft:clay')})
```
- Some examples:
```js
//Minimal
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:redstone_block'),[BlockPredicate.of('minecraft:lava'),BlockPredicate.of('minecraft:stone')])

        //Catalyst
    	event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:dirt'),[BlockPredicate.of('minecraft:lava'),BlockPredicate.of('minecraft:stone')])
    	    .catalyst('minecraft:clay')

        //Bonks
    	event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:sand'),[BlockPredicate.of('minecraft:lava'),BlockPredicate.of('minecraft:stone')])
    	    .requiredBonks(10)

        //Chanced output
        event.recipes.create_mechanical_extruder.extruding(Output.of('minecraft:red_sand',0.5),[BlockPredicate.of('minecraft:lava'),BlockPredicate.of('minecraft:stone')])

        //Chanced output & bonks
        event.recipes.create_mechanical_extruder.extruding(Output.of('minecraft:birch_planks',0.5),[BlockPredicate.of('minecraft:lava'),BlockPredicate.of('minecraft:stone')])
            .requiredBonks(5)

        //Advanced extruder
    	event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:obsidian'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')])
    	    .catalyst(BlockPredicate.of("minecraft:dirt"))
    	    .advanced(true)

    	//Advanced extruder + consume blocks
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:birch_planks'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')])
            .catalyst(BlockPredicate.of("minecraft:dirt"))
            .consumeBlocks(true)
            .advanced(true)

        //Advanced extruder + consume different blocks
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:obsidian'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')])
            .catalyst(BlockPredicate.of("minecraft:dirt"))
            .consumeBlocks([true,false])
            .advanced(true)

        //Biome requirement
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:iron_block'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')]).catalyst(BlockPredicate.of("minecraft:dirt"))
            .requirements(
                [
                    RecipeRequirement.biome("minecraft:plains")
                ]
            );

        //Biome Tag requirement
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:gold_block'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')]).catalyst(BlockPredicate.of("minecraft:dirt"))
            .requirements(
                [
            	    RecipeRequirement.biomeTag("minecraft:is_nether")
                ]
            );

        //MinY & MaxY requirement
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:coal_block'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')]).catalyst(BlockPredicate.of("minecraft:dirt"))
            .requirements(
                [
                    RecipeRequirement.minY(-10),
                    RecipeRequirement.maxY(12),
                ]
    	    );
        //MinSpeed & MaxSpeed requirement
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:dirt'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')]).catalyst(BlockPredicate.of("minecraft:dirt"))
    	    .requirements(
                [
                    RecipeRequirement.minSpeed(1.0),
                    RecipeRequirement.maxSpeed(16.0),
                ]
            );

        //All requirements together
    	event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:emerald_block'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')]).catalyst(BlockPredicate.of("minecraft:dirt"))
    	.requirements(
    	    [
    	        RecipeRequirement.minSpeed(6.0),
    	        RecipeRequirement.maxSpeed(16.0),
    	        RecipeRequirement.minY(10),
    	        RecipeRequirement.maxY(12),
    	        RecipeRequirement.biomeTag("minecraft:is_nether")

    	    ]
    	);

        //Everything together
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:emerald_block'),[BlockPredicate.of('minecraft:sand'),BlockPredicate.of('minecraft:gravel')])
        .catalyst(BlockPredicate.of("minecraft:dirt"))
        .advanced(true)
        .consumeBlocks(true)
    	.requirements(
    	    [
    	        RecipeRequirement.minSpeed(16.0),
    	        RecipeRequirement.minY(10),
    	        RecipeRequirement.maxY(12),
    	        RecipeRequirement.biomeTag("minecraft:is_nether")

    	    ]
    	);

    	// Funny things
    	// Flower pots
        event.recipes.create_mechanical_extruder.extruding(Item.of('minecraft:glowstone'),[BlockPredicate.of('minecraft:potted_dandelion'),BlockPredicate.of('minecraft:potted_poppy')])

```



**Thanks to the Creators of Create.**

Code inspiration from the [Create](https://www.curseforge.com/minecraft/mc-mods/create "Create") mod itself.

## License
Create Mechanical Extruder is licensed under the LGPL license. See [LICENSE](LICENSE) for more information.

Certain sections of the code are from the Create mod, which is licensed under the MIT license. See [Create's license](https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/LICENSE) for more information.
