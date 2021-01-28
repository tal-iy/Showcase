#ifndef FACTORY_H
#define FACTORY_H

#include "Core/Image.h"
#include "Core/SceneNode.h"

#include "Enemy.h"
#include "Projectile.h"
#include "EnumDef.h"
#include "ExplosionEntity.h"
#include "Player.h"

class Player;
class ExplosionEntity;

class Factory
{
private:
    //entity images
    Image playerImage;
    Image enemyImage;
    Image projectileImage;
    Image explosionImage;

    //game score and lives remaining
    int score;
    int lives;
public:
    bool init();
    void free();

    SceneNode* create(int entity, int x, int y);

    void setScore(int s);
    void setLives(int l);
    int getScore();
    int getLives();
};

#endif
