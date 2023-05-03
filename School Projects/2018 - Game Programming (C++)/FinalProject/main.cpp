/*
    Title: Final Project
    Author: Vitaliy Shydlonok
    Date: 45/7/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, shows a splash screen, draws a background,
        a HUD with the number of lives remaining and game score, a player space
        ship that shoots projectiles with the space bar key and controlled
        by the left and right arrow keys, and enemy space ships scrolling across
        the screen. The game is paused with the P key and quit with the ESC key.
    Data Requirements:
        "graphics/player.bmp"
        "graphics/enemy.bmp"
        "graphics/projectile.bmp"
        "graphics/explosion.bmp"
        "graphics/background.bmp"
        "graphics/start.bmp"
        "graphics/paused.bmp"
        "graphics/over.bmp"
        "graphics/brick.ttf"
*/

#include "GameScene.h"

int main(int argc, char *argv[])
{
    GameScene game;

    if(!game.init())
    {
        game.free();
        return 0;
    }

    game.run();

    return 0;
}
