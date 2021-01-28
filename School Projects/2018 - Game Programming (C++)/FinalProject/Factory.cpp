#include "Factory.h"

/*
    init:
        Loads the images used by the factory.
    returns:
        true if loading was successful
        false if loading failed
    algorithm:
        load the player image
        load the enemy image
        load the projectile image
        load the explosion animation image
        check if any of the loading failed
            return false to indicate the fail
        return true to indicate success
*/
bool Factory::init()
{
    //load and verify entity images
    if (!playerImage.load("graphics/player.bmp", 32, 32) ||
        !enemyImage.load("graphics/enemy.bmp", 32, 32) ||
        !projectileImage.load("graphics/projectile.bmp", 8, 8) ||
        !explosionImage.load("graphics/explosion.bmp", 50, 50))
        return false;

    return true;
}

/*
    free:
        Frees memory taken by all of the images.
    algorithm:
        free the player image
        free the enemy image
        free the projectile image
        free the explosion image
*/
void Factory::free()
{
    playerImage.free();
    enemyImage.free();
    projectileImage.free();
    explosionImage.free();
}

/*
    create:
        Initializes a new entity and returns it as a SceneNode.
    arguments:
        entity - the entity ID to spawn
        x - x position to spawn the entity at
        y - y position to spawn the entity at
    returns:
        SceneNode containing the new entity
    algorithm:
        based on entity ID, choose which entity to initialize
            initialize a Player if entity ID is ENTITY_PLAYER
            initialize an Enemy if entity ID is ENTITY_ENEMY
            initialize a Projectile if entity ID is ENTITY_PROJECTILE
            initialize an ExplosionEntity if entity ID is ENTITY_EXPLOSION
*/
SceneNode* Factory::create(int entity, int x, int y)
{
    switch(entity)
    {
        case ENTITY_PLAYER:
            return new Player(&playerImage, this, x, y);
            break;

        case ENTITY_ENEMY:
            return new Enemy(&enemyImage, this, x, y);
            break;

        case ENTITY_PROJECTILE:
            return new Projectile(&projectileImage, x, y);
            break;

        case ENTITY_EXPLOSION:
            return new ExplosionEntity(&explosionImage, x, y);
            break;
    }
}

/*
    setScore:
        sets the score variable
*/
void Factory::setScore(int s)
{
    score = s;
}

/*
    setLives:
        sets the lives variable
*/
void Factory::setLives(int l)
{
    lives = l;
}

/*
    getScore:
        retrieves the score
    returns:
        score
*/
int Factory::getScore()
{
    return score;
}

/*
    getLives:
        retrieves the number of lives
    returns:
        lives
*/
int Factory::getLives()
{
    return lives;
}

