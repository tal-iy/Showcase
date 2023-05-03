#ifndef EXPLOSIONENTITY_H
#define EXPLOSIONENTITY_H

#include "Core\SceneNode.h"
#include "Core\Sprite.h"

#include "EnumDef.h"
#include "Factory.h"

class Factory;

class ExplosionEntity : public SceneNode
{
private:
    static const int NUM_FRAMES = 7; //number of frames in the explosion animation
    Sprite sprite; //sprite image
    int frame; //current frame to keep track of when to remove it
public:
    ExplosionEntity(Image* image, int x, int y);
    ~ExplosionEntity();

    void update();
    void draw(Rectangle* view, Graphics* g);
};

#endif
