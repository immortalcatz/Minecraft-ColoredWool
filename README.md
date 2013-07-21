# Colored Wool

This mod adds a new block and allows you to give it a custom color. Default texture of the block is a
normal wool so it doesn't affect colors applied to it.

## Screenshots

Images made by peronix:  
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/20110505103100.png "Screenshot")  
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/20110505150508.png "Screenshot")

Test map:  
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/citrouilleb.png "Screenshot")
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/diamondo.png "Screenshot")  
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/linkn.png "Screenshot")
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/railt.png "Screenshot")  
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/diamond2.png "Screenshot")
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/redstone.png "Screenshot")

Possible shades with 10 color step:  
![Screenshot](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/sampleu.png "Screenshot")

## Videos

Colored wool:  
[![Colored wool](http://img.youtube.com/vi/7rSGPBRcZhk/0.jpg)](http://www.youtube.com/watch?v=7rSGPBRcZhk)

Picture factory:  
[![Picture factory](http://img.youtube.com/vi/sWsCL5Y-aWc/0.jpg)](http://www.youtube.com/watch?v=sWsCL5Y-aWc)

Model factory:  
[![Model factory](http://img.youtube.com/vi/ZUK8um7YJMk/0.jpg)](http://www.youtube.com/watch?v=ZUK8um7YJMk)

## How to craft it

Colored dye:  
![Crafting colored dye powder](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/crafting-coloreddyepowder.png "Crafting colored dye powder")

Colored wool:  
![Crafting colored wool](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/crafting-coloredwool.png "Crafting colored wool")

Brush:  
![Crafting brush](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/crafting-brush.png "Crafting brush")

Picture factory:  
![Crafting picture factory](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/crafting-picturefactory.png "Crafting picture factory")

Model factory:  
![Crafting model factory](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/crafting-modelfactory.png "Crafting model factory")

## How to use it

### Click to change the color

This method only work if you set `ColorSelection` to `manual` in the configuration.

![Manual](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/explications.png "Manual")

The block has four modes: Color step (S), Red (R), Green (G) and Blue (B). When you right-click on it, it switch from the current mode to the next and come back to S if you are in B. In R, G or B mode, a left-click increases the amount of selected color by `255 / colorStep` and,
if you reach 255, restarts from 0. In S, a left-click modifies the value of `colorStep`. You can go from 1 to `MaxColorStep`. You can change `MaxColorStep` in the configuration.

When you put this block on the floor, it has a default `colorStep` value of `InitColorStep` and R is selected. If you switch to another mode, disconnect and reconnect, it will by R again with
default `colorStep` value so if you forget what was the current mode it is not important.

### Hexadecimal value

If you set `ColorSelection` to `menu` in the configuration, right-clicking on the block will open a menu.

![Menu](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/hexa1.PNG "Menu")

You have the following options:

* Hexa: enter an hexadecimal value to color the wool.
* Saved colors: see below.
* Last color: assign the last color used to the wool.
* Import Image: import an image.

Clicking on `Hexa` opens another menu.

![Menu](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/hexa2.PNG "Menu")

Then click on `Done` twice and the color of the block will be updated.

![Menu](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/hexa3.PNG "Menu")

### Saved colors

From the menu, if you click on `Saved colors`, you will have another menu with a palette of colors and many options below. You colors are saved in the file `savedColors.properties`. You can modify the path in the configuration file.

![Saved colors](https://github.com/Nauja/Minecraft/raw/master/ColoredWool/resources/savedcolors.png "Saved colors")

With this menu you can add current selected color (the color of the block used to open the menu), delete existing or all of them and select the color to use. Here is a descriptions of all buttons:

* Add: add block's color or the one selected with hexadecimal input to the list.
* Delete: delete selected color from the list.
* Clear: remove all colors.
* Done: apply selected color to block and save the list of colors to `savedColors.properties`.

If you have a large number of colors, two buttons will be enabled (previous and next), allowing you to switch between one page to another.
 
* Previous: go to previous page.
* Next: go to next page.

## How to install it

### Client

1. Make sure to have [Forge](http://www.minecraftforge.net/wiki/Installation/Universal) installed.
2. Downloads the latest `ColoredWool-x.x.x-x.x.x.zip` and put it into `.minecraft/mods`.
3. It's done.

### Server

1. Make sure to have [Forge](http://www.minecraftforge.net/wiki/Installation/Universal) installed.
2. Downloads the latest `ColoredWool-x.x.x-x.x.x.zip` and put it into `yourserver/mods`.
3. It's done.

## Download

[Releases](https://github.com/Nauja/Minecraft-ColoredWool/releases)

## Configuration

The configuration file `ColoredWool.cfg` can be found in the `.minecraft/config` folder for the client and in the `yourserver/config` folder for the server. By default, it contains the following:

```
# Configuration file

####################
# block
####################

block {
    I:ColoredWool=501
    I:ModelFactoryActive=506
    I:ModelFactoryBurning=505
    I:ModelFactoryIdle=507
    I:PictureFactoryActive=503
    I:PictureFactoryBurning=502
    I:PictureFactoryIdle=504
}


####################
# coloredwool
####################

coloredwool {
    S:ColorSelection=menu
    S:Folder=/path/to/config/folder/images
    I:InitColorBlue=56
    I:InitColorGreen=56
    I:InitColorRed=56
    I:InitColorStep=5
    I:MaxColorStep=10
    S:SavedColors=/path/to/config/folder/savedColors.properties
}


####################
# item
####################

item {
    I:ColoredBrush=5002
    I:ColoredDye=5001
}


####################
# modelfactory
####################

modelfactory {
    B:DontEraseAnything=false
    S:DontEraseTheseIds=
    B:DontRequireFuel=true
    B:DontRequireItems=true
    B:InstantCook=true
}


####################
# picturefactory
####################

picturefactory {
    B:DontEraseAnything=false
    S:DontEraseTheseIds=
    B:DontRequireFuel=true
    B:DontRequireItems=true
    B:InstantCook=true
}
```

* block
    * ColoredWool: colored wool identifier.
    * ModelFactoryActive: model factory identifier when active.
    * ModelFactoryBurning: model factory identifier when burning.
    * ModelFactoryIdle: model factory identifier when idle.
    * PictureFactoryActive: picture factory identifier when active.
    * PictureFactoryBurning: picture factory identifier when burning.
    * PictureFactoryIdle: picture factory identifier when idle.
* coloredwool
    * ColorSelection: how the color is modified.
        * manual: left-click and right-click on the wool with a brush.
        * menu: open a menu.
        * none: can't modify the color.
    * Folder: folder containing the images usable by the picture factory and the model factory.
    * InitColorBlue: initial color of the wool when placed (blue component).
    * InitColorGreen: initial color of the wool when placed (green component).
    * InitColorRed: initial color of the wool when placed (blue component).
    * InitColorStep: initial step of the wool when placed.
    * MaxColorStep: maximal step.
    * SavedColors: file containing the saved colors.
* item
    * ColoredBrush: colored brush identifier.
    * ColoredDye: colored dye identifier.
* modelfactory
    * DontEraseAnything: don't replace existing blocks.
    * DontEraseTheseIds: don't replace existing blocks with these ids (`id1;id2;...;`).
    * DontRequireFuel: the factory doesn't require fuel (coal, wood...) to work.
    * DontRequireItems: the factory doesn't require items (dye, wool...) to work.
    * InstantCook: no cooking time.
* picturefactory
    * DontEraseAnything: don't replace existing blocks.
    * DontEraseTheseIds: don't replace existing blocks with these ids (`id1;id2;...;`).
    * DontRequireFuel: the factory doesn't require fuel (coal, wood...) to work.
    * DontRequireItems: the factory doesn't require items (dye, wool...) to work.
    * InstantCook: no cooking time.

## Bugs

Feel free to report any bug [here](https://github.com/Nauja/Minecraft/issues).