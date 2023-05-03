/*
    Title: Module 10: Program 7
    Author: Vitaliy Shydlonok
    Date: 4/2/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, draws a background and layered grid of tiles.
        The view is moved around using the arrow keys.
    Data Requirements:
        "graphics/tiles.bmp"
        "graphics/map.map"

    Code Modifications:
        Changed window title in SceneDemo::init()
        Everything else was done in "graphics/map.map"
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
