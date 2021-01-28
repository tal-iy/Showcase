/*
    Title: Module 9: Program 6
    Author: Vitaliy Shydlonok
    Date: 3/26/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, draws a background and layered grid of tiles.
        The view is moved around using the arrow keys.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/tiles.bmp"
        "graphics/platformer.map"

    Code Modifications:
        Changed window title in MapDemo::init()
        Everything else was done in "graphics/platformer.map"
*/

#include "MapDemo.h"

int main(int argc, char *argv[])
{
    MapDemo game;

    if(!game.init())
    {
        game.free();
        return 0;
    }

    game.run();

    return 0;
}
