#ifndef MAPDEMO_H
#define MAPDEMO_H

#include "Core/Game.h"
#include "Core/Image.h"
#include "Scene.h"
#include "Rectangle.h"
#include "Map.h"
#include "MapNode.h"

class SceneDemo : public Game
{
private:
    static const int PLAYER_SPEED = 10;

    Map map;
    Scene scene;
    Rectangle camera;
    MapNode* player;
public:
    SceneDemo();
    ~SceneDemo();
    virtual bool init();
    virtual void update();
    virtual void draw(Graphics* g);
    virtual void free();
};

#endif
