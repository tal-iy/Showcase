#ifndef FACTORY_H
#define FACTORY_H

#include "Core/Image.h"
#include "Core/Map.h"
#include "Core/SceneNode.h"

#include "NPC.h"
#include "EntityDef.h"
#include "ExplosionEntity.h"
#include "Player.h"

class Player;
class ExplosionEntity;

class Factory
{
private:
    Map* map;

    Image endImage;
    Image checkpointImage;
    Image playerImage;
    Image explosionImage;

public:
    bool init(Map* m);
    void free();

    SceneNode* create(int entity, int x, int y);
};

#endif
