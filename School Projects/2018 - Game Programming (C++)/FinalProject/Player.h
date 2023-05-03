#ifndef Player_H
#define Player_H

#include "Core/Sprite.h"
#include "Core/SceneNode.h"
#include "Core/Rectangle.h"

#include "EnumDef.h"
#include "Factory.h"

class Factory;

class Player : public SceneNode
{
private:
    Sprite sprite; //image sprite
    Factory* factory; //entity factory class
    int solidTimer; //time until player can collide with enemies
public:
    Player(Image* img, Factory* f, int x, int y);
    ~Player();

    void update();
    virtual void draw(Rectangle* view, Graphics* g);
};

#endif
