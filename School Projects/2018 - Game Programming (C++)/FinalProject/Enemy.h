#ifndef NPC_H
#define NPC_H

#include "Core/SceneNode.h"
#include "Core/Sprite.h"
#include "Factory.h"
#include "EnumDef.h"

class Factory;

class Enemy : public SceneNode
{
private:
    //velocity
    int yVel;
    int xVel;

    Sprite sprite; //sprite image
    Factory* factory; //factory that spawned this enemy
public:
    Enemy(Image* i, Factory* f, int x, int y);
    ~Enemy(){};

    void update();
    void draw(Rectangle* view, Graphics* g);
};

#endif
