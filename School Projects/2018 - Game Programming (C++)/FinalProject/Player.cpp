#include "Player.h"

/*
    Player:
        Constructor for initializing the Player entity.
    algorithm:
        set collision timer to 3 seconds
        set the sprite image to img
        set the factory to f
        set the width based on image width
        set the height based on image height
*/
Player::Player(Image* img, Factory* f, int x, int y) : SceneNode(ENTITY_PLAYER, x, y, 0, 0)
{
    solidTimer = 90; //set collision timer to 3 seconds

    sprite.setImage(img);
    factory = f;

    setWidth(sprite.getWidth());
    setHeight(sprite.getHeight());
}

Player::~Player()
{

}

/*
    update:
        Updates the player entity.
    algorithm:
        check if the collision timer is active
            decrement the collision timer
        check if the collision timer is at 0
            retrieve all nodes in the scene
            loop through the retrieved nodes
                check for ENTITY_ENEMY entities that overlap the player
                    create an explosion at the players position
                    remove the enemy
                    decrease lives by 1
                    reset collision timer to 3 seconds
*/
void Player::update()
{
    //update collision timer if the player isn't solid
    if (solidTimer > 0)
        solidTimer--;
    else
    {
        //loop through all scene entities
        std::list<SceneNode*>* nodes = getScene()->getNodes();
        for(list<SceneNode*>::iterator it = nodes->begin(); it != nodes->end(); it++)
        {
            //check for collisions with enemy entities
            if((*it)->getID() == ENTITY_ENEMY && overlaps(**it))
            {
                //create an explosion and remove the enemy
                getScene()->addNode(factory->create(ENTITY_EXPLOSION, getCenterX()-25, getCenterY()-25));
                (*it)->remove();

                //decrease lives by 1
                factory->setLives(factory->getLives()-1);

                //reset collision timer to 3 seconds
                solidTimer = 90;
            }
        }
    }
}

/*
    draw:
        Draws the player.
    algorithm:
        set the animation frame to 0 if the collision timer is at 0
        set the animation frame to 1 if the collision timer is active
        check if the player is crossing the left side of the screen
            reset the players position to the left side of the screen
        check if the player is crossing the right side of the screen
            reset the players position to the right side of the screen
        draw the player sprite image
*/
void Player::draw(Rectangle* view, Graphics* g)
{
    //change image frame based on collision timer
    if (solidTimer == 0)
        sprite.setFrame(0); //set image frame to solid version
    else
        sprite.setFrame(1); //set image frame to transparent version

    //make sure the player stays within the view
    if (getX() < view->getX())
        setX(view->getX());

    if (getX()+getWidth() > view->getX()+view->getWidth())
        setX(view->getX()+view->getWidth()-getWidth());

    //draw the player image
    sprite.draw(getX()-view->getX(), getY()-view->getY(), g);
}
