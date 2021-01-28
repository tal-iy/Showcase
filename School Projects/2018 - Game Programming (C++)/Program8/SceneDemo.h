#ifndef MAPDEMO_H
#define MAPDEMO_H

#include "Core/Game.h"
#include "Core/Image.h"
#include "Core/Scene.h"
#include "Core/Rectangle.h"
#include "Core/Map.h"
#include "Core/MapNode.h"
#include "Factory.h"
#include "Player.h"
#include "NPC.h"
#include "ExplosionEntity.h"

class SceneDemo : public Game
{
private:
    static const int PLAYER_SPEED = 10;
    static const int PLAYER_WIDTH = 32; //player image width
    static const int PLAYER_HEIGHT = 32; //player image height
    static const int PLAYER_X = 0; //player x position
    static const int PLAYER_Y = 0; //player y position

    Map map;
    Scene scene;
    Rectangle camera;
    Player* player;
    Image playerImage;
    Factory factory;
public:
    SceneDemo();
    ~SceneDemo();
    virtual bool init();
    virtual void update();
    virtual void draw(Graphics* g);
    virtual void free();
};

#endif
