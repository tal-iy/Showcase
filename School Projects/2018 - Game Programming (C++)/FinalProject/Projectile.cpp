#include "Projectile.h"

/*
    Projectile:
        Constructor for the Projectile entity.
    algorithm:
        set the sprite image to i
        set the vertical velocity to -5
        set the horizontal velocity to 0
        set the width based on image width
        set the height based on image height
*/
Projectile::Projectile(Image* i, int x, int y) : SceneNode(ENTITY_PROJECTILE, x, y, 0, 0)
{
    sprite.setImage(i);
    yVel = -5;
    xVel = 0;

    setWidth(sprite.getWidth());
    setHeight(sprite.getHeight());
}

/*
    update:
        Updates the projectile entity.
    algorithm:
        move the x position based on horizontal velocity
        move the y position based on vertical velocity
*/
void Projectile::update()
{
    //update position based on velocity
    setX(getX()+xVel);
    setY(getY()+yVel);
}

/*
    draw:
        Draws the projectile entity.
    algorithm:
        draw the sprite image
        check if the projectile is past the top of the screen
            remove the projectile from the scene
*/
void Projectile::draw(Rectangle* view, Graphics* g)
{
    //draw the projectile image
    sprite.draw(getX()-view->getX(), getY()-view->getY(), g);

    //remove the projectile when it goes above the screen
    if (getY() < view->getY())
        remove();
}
