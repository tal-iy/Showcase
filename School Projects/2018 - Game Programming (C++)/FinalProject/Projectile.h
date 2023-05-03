#ifndef PROJECTILE_H
#define PROJECTILE_H

#include "Core/SceneNode.h"
#include "Core/Sprite.h"
#include "Factory.h"
#include "EnumDef.h"

class Factory;

class Projectile : public SceneNode
{
private:
    //velocity
    int yVel;
    int xVel;

    Sprite sprite; //sprite image
public:
    Projectile(Image* i, int x, int y);
    ~Projectile(){};

    void update();
    void draw(Rectangle* view, Graphics* g);
};

#endif
