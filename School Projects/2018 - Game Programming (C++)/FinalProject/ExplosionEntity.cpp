#include "ExplosionEntity.h"

/*
    ExplosionEntity:
        Constructor for the explosion entity.
    algorithm:
        set the explosion entities sprite image
        initialize the animation frame as -1
*/
ExplosionEntity::ExplosionEntity(Image* image, int x, int y) :SceneNode(1, x, y, image->getFrameWidth(), image->getFrameHeight())
{
    sprite.setImage(image);
    frame = -1;
}

ExplosionEntity::~ExplosionEntity()
{

}

/*
    update:
        Updates the explosion animation.
    algorithm:
        increment the current animation frame by 1
        check for the end of the animation
            remove the explosion entity from the scene
        propagate the animation frame to the sprite
        update the sprite
*/
void ExplosionEntity::update()
{
    frame++;

    if(frame >= NUM_FRAMES)
        remove();

    sprite.setFrame(frame);
    sprite.update();
}

/*
    draw:
        Draws the explosion sprite.
    algorithm:
        draw the sprite image
*/
void ExplosionEntity::draw(Rectangle* view, Graphics* g)
{
    sprite.draw(getX() - view->getX(), getY() - view->getY(), g);
}
