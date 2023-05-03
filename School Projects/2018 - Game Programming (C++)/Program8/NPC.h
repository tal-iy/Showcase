#ifndef NPC_H
#define NPC_H

#include "Core/MapNode.h"
#include "Core/Sprite.h"
#include "Factory.h"
#include "EntityDef.h"

class Factory;

class NPC : public MapNode
{
private:
    int yVel;
    int xVel;
    int oldY;
    Map* map;
    Sprite sprite;
    Factory* factory;
public:
    NPC(Image* i, Factory* f, int x, int y, Map* m);
    ~NPC(){};

    void update();
    void draw(Rectangle* view, Graphics* g);
};

#endif
