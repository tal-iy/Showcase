/*
    Title: Module 11: Program 8
    Author: Vitaliy Shydlonok
    Date: 4/16/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, draws a background and layered grid of tiles.
        Creates two moving entities that explode on contact with the player.
        The player is moved around using the arrow keys.
    Data Requirements:
        "graphics/tiles.bmp"
        "graphics/player.bmp"
        "graphics/end.bmp"
        "graphics/checkpoint.bmp"
        "graphics/map.map"
*/

#include "SceneDemo.h"

int main(int argc, char *argv[])
{
    SceneDemo game;

    if(!game.init())
    {
        game.free();
        return 0;
    }

    game.run();

    return 0;
}
