#ifndef GAMESCENE_H
#define GAMESCENE_H

#include "Core/Game.h"
#include "Core/Image.h"
#include "Core/Scene.h"
#include "Core/SceneNode.h"
#include "Core/Rectangle.h"
#include "Core/OutlineFont.h"
#include "Factory.h"
#include "Player.h"
#include "Enemy.h"
#include "Projectile.h"
#include "ExplosionEntity.h"

class GameScene : public Game
{
private:
    static const int PLAYER_SPEED = 10; //speed at which the player can move
    static const int PLAYER_WIDTH = 32; //player image width
    static const int PLAYER_HEIGHT = 32; //player image height
    static const int PLAYER_X = 384; //starting player x position
    static const int PLAYER_Y = 536; //starting player y position
    static const int PLAYER_LIVES = 5; //starting number of lives

    int shootTimer; //time until the player can shoot again
    int enemyTimer; //time until the next enemy spawns
    int backgroundScroll; //position of the scrolling background
    int gameState; //state of the game (start, game, paused, or game over)

    //background images
    Image backgroundImage;
    Image startImage;
    Image pausedImage;
    Image overImage;

    OutlineFont font; //game font

    Scene scene; //game scene
    Rectangle camera; //camera for position and size
    Player* player; //player entity
    Factory factory; //entity factory

public:
    GameScene();
    ~GameScene();
    virtual bool init();
    virtual void update();
    virtual void draw(Graphics* g);
    virtual void free();
};

#endif
