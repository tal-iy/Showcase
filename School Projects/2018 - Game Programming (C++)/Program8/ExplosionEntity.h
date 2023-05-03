#ifndef EXPLOSIONENTITY_H
#define EXPLOSIONENTITY_H

#include "Core\SceneNode.h"
#include "Core\Sprite.h"

#include "EntityDef.h"
#include "Factory.h"

class Factory;

class ExplosionEntity : public SceneNode
{
private:
    static const int NUM_FRAMES = 7;
    Sprite sprite;
    int frame;
    int channel;
public:
    ExplosionEntity(Image* image, int x, int y);
    ~ExplosionEntity();
    void update();
    void draw(Rectangle* view, Graphics* g);
};

#endif
