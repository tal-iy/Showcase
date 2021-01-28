#ifndef Player_H
#define Player_H

#include "Core/Sprite.h"
#include "Core/SceneNode.h"
#include "Core/Map.h"
#include "Core/Rectangle.h"

#include "EntityDef.h"
#include "Factory.h"

class Factory;

class Player : public SceneNode
{
private:
    Map* map;
    Sprite sprite;
    bool solid;
    Factory* factory;

public:
    Player(int i, int x, int y, int width, int height, bool s, Map* m, Image* img);
    ~Player();

    Player(Image* img, Map* m, int x, int y, int width, int height, bool s, Factory* f );

    void update();
    virtual void draw(Rectangle* view, Graphics* g);
    bool overlapsMap();
    void move(int x, int y);
};

#endif
