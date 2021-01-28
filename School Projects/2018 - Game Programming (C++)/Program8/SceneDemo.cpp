#include "SceneDemo.h"

SceneDemo::SceneDemo()
{
}

SceneDemo::~SceneDemo()
{

}

bool SceneDemo::init()
{
    if(!initSystem("Module 10: Program 7", 800, 600, false))
        return false;

    if(!map.load("graphics/map.map", "graphics/tiles.bmp"))
        return false;

    if(!playerImage.load("graphics/player.bmp", 32, 32))  //load the sprite bitmap
        return false;

    if (!factory.init(&map))
        return false;

    player = new Player(ENTITY_PLAYER, PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, true, &map, &playerImage);

    scene.addNode(factory.create(ENTITY_END, 900, 600)); //add the end NPC to the scene
    scene.addNode(factory.create(ENTITY_CHECKPOINT, 200, 200)); //add the checkpoint npc to the scene
    scene.addNode(player);

    camera.set(0,0,800,600);

    return true;
}

void SceneDemo::update()
{
    scene.update();

    Input* in = getInput();

    int playerMoveX = 0;
    int playerMoveY = 0;

    if(in->keyDown(SDLK_UP))
        playerMoveY = -PLAYER_SPEED;

    if(in->keyDown(SDLK_DOWN))
        playerMoveY = PLAYER_SPEED;

    if(in->keyDown(SDLK_LEFT))
        playerMoveX = -PLAYER_SPEED;

    if(in->keyDown(SDLK_RIGHT))
        playerMoveX = PLAYER_SPEED;

    player->move(playerMoveX, playerMoveY);

    camera.setCenterX(player->getCenterX());
    camera.setCenterY(player->getCenterY());

    if(camera.getX() < 0)
        camera.setX(0);

    if(camera.getY() < 0)
        camera.setY(0);

    if(camera.getX2() >= map.getTotalWidth())
        camera.setX2(map.getTotalWidth() - 1);

    if(camera.getY2() >= map.getTotalHeight())
        camera.setY2(map.getTotalHeight() - 1);

    if(player->getX() < 0)
        player->setX(0);

    if(player->getY() < 0)
        player->setY(0);

    if(player->getX2() >= map.getTotalWidth())
        player->setX2(map.getTotalWidth() - 1);

    if(player->getY2() >= map.getTotalHeight())
        player->setY2(map.getTotalHeight() - 1);
}

void SceneDemo::draw(Graphics* g)
{
    map.draw(1, camera.getX(), camera.getY(), g);
    map.draw(2, camera.getX(), camera.getY(), g);
    map.draw(0, camera.getX(), camera.getY(), g);
    scene.draw(&camera, g);
}

void SceneDemo::free()
{
    scene.removeNode(NULL);

    map.free();
    freeSystem();
}
