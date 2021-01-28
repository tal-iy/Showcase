#include "Enemy.h"

/*
    Enemy:
        Constructor for the Enemy entity.
    algorithm:
        set the enemies sprite image
        initialize vertical velocity
        initialize horizontal velocity
        set the factory
        initialize the width based on the image width
        initialize the height based on the image height
*/
Enemy::Enemy(Image* i, Factory* f, int x, int y) : SceneNode(ENTITY_ENEMY, x, y, 0, 0)
{
    sprite.setImage(i);
    yVel = 7;
    xVel = 0;

    factory = f;

    setWidth(sprite.getWidth());
    setHeight(sprite.getHeight());
}

/*
    update:
        Updates the enemies position and does collision detection with projectiles.
    algorithm:
        update x position based on horizontal velocity
        update y position based on vertical velocity
        retrieve all entities within the scene
        loop through all retrieved entities
            check for projectile entities that overlap with the enemy
                spawn an explosion at the enemy position
                remove the enemy entity from the scene
                add 1 to the score
*/
void Enemy::update()
{
    //update position based on velocity
    setX(getX()+xVel);
    setY(getY()+yVel);

    //loop through all scene entities
    std::list<SceneNode*>* nodes = getScene()->getNodes();
    for(list<SceneNode*>::iterator it = nodes->begin(); it != nodes->end(); it++)
    {
        //check for collisions with projectile entities
        if((*it)->getID() == ENTITY_PROJECTILE && overlaps(**it))
        {
            //create an explosion and remove the enemy
            getScene()->addNode(factory->create(ENTITY_EXPLOSION, getCenterX()-25, getCenterY()-25));
            remove();

            //increase score by 1
            factory->setScore(factory->getScore()+1);
        }
    }
}

/*
    draw:
        Draws the enemy image and makes sure the enemy gets removed when it leaves the screen.
    algorithm:
        draw the sprite image
        check if the enemy is below the screen
            remove the enemy from the scene
*/
void Enemy::draw(Rectangle* view, Graphics* g)
{
    //draw the enemy image
    sprite.draw(getX()-view->getX(), getY()-view->getY(), g);

    //remove the enemy when it goes below the screen
    if (getY()+sprite.getHeight() > view->getHeight())
        remove();
}
