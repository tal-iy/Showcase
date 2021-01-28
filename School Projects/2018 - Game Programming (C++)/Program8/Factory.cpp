#include "Factory.h"

bool Factory::init(Map* m)
{
    map = m;

    if(!endImage.load("graphics/end.bmp", 32, 32))
        return false;

    if(!checkpointImage.load("graphics/checkpoint.bmp", 32, 32))
        return false;

    if(!playerImage.load("graphics/player.bmp", 32, 32))
        return false;

    if(!explosionImage.load("graphics/explosion.bmp", 50, 50))
        return false;

    return true;
}

void Factory::free()
{
    endImage.free();
    checkpointImage.free();
    playerImage.free();
    explosionImage.free();
}

SceneNode* Factory::create(int entity, int x, int y)
{
    switch(entity)
    {
        case ENTITY_END:
            return new NPC(&endImage, this, x, y, map);
            break;

        case ENTITY_CHECKPOINT:
            return new NPC(&checkpointImage, this, x, y, map);
            break;

        case ENTITY_EXPLOSION:
            return new ExplosionEntity(&explosionImage, x, y);
            break;

        default:
            return new SceneNode(1, x, y, 50, 50);
    }
}
