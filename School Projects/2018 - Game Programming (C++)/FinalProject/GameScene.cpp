#include "GameScene.h"

GameScene::GameScene()
{

}

GameScene::~GameScene()
{

}

/*
    init:
        Initializes the game scene to start the game.
    returns:
        true if initialization was successful
        false if initialization failed
    algorithm:
        initialize SDL and the game window
            return false if SDL initialization failed
        initialize the entity factory
            return false if factory initialization failed
        load all background and splash images
        verify that image loading was successful
            return false if loading failed
        load and verify the font
            return false if font loading failed
        use the entity factory to initialize the player entity
        add the player to the scene
        reset the camera position and size
        reset the shooting timer to 0
        reset the enemy spawn timer to 0
        reset the score to 0
        reset the lives remaining to PLAYER_LIVES
        start background position at 0
        go to the start screen
        return true for success
*/
bool GameScene::init()
{
    //initialize SDL and the game window
    if(!initSystem("Final Project", 800, 600, false))
        return false;

    //initialize entity factory
    if (!factory.init())
        return false;

    //load and verify background and splash images
    if (!backgroundImage.load("graphics/background.bmp") ||
        !startImage.load("graphics/start.bmp") ||
        !pausedImage.load("graphics/paused.bmp") ||
        !overImage.load("graphics/over.bmp"))
        return false;

    //load font
    if (!font.load("graphics/brick.ttf", 20))
        return false;

    //spawn the player in the game
    player = (Player*)factory.create(ENTITY_PLAYER, PLAYER_X, PLAYER_Y);
    scene.addNode(player);

    //reset camera
    camera.set(0,0,800,600);

    //reset timers
    shootTimer = 0;
    enemyTimer = 0;

    //reset score and lives
    factory.setScore(0);
    factory.setLives(PLAYER_LIVES);

    //start background position at 0
    backgroundScroll = 0;

    //go to the start screen
    gameState = STATE_START;

    return true;
}

/*
    update:
        Updates the game scene based on the game state.
    algorithm:
        capture keyboard inputs
        check for escape key to quit
            end the game if the escape key is pressed
        if the game state is STATE_START
            check for enter key to start the game
                set game state to STATE_GAME
        if the game state is STATE_GAME
            update all scene entities
            update shooting timer if it's active
            update enemy spawner timer if it's active
            scroll the background image
            check if the background is scrolled past the screen
                reset the background scrolling
            check if the left key is pressed
                set the players horizontal velocity to negative player speed
            check if the right key is pressed
                set the players horizontal velocity to positive player speed
            move the player based on new velocity
            check if the space bar is pressed and the shooting timer is at 0
                spawn a projectile using the entity factory
                set shooting timer to 12 frames to limit shooting speed
            check if the enemy spawning timer is at 0
                spawn an enemy at a random position using the entity factory
                set enemy spawner timer to 24 frames to limit how fast enemies spawn
            check if the P key is pressed
                set the game state to STATE_PAUSED
            check if the lives remaining reaches 0
                set the game state to STATE_OVER
        if the game state is STATE_PAUSED
            check if the enter key is pressed
                set the game state to STATE_GAME
        if the game state is STATE_OVER
            check if the enter key is pressed
                reset the shooting timer
                reset the enemy spawning timer
                start background position at 0
                clear the scene
                spawn a new player in the game
                add the player to the scene
                reset score to 0
                reset lives to PLAYER_LIVES
                set game state to STATE_GAME
*/
void GameScene::update()
{
    //capture keyboard inputs
    Input* in = getInput();

    //check for escape key to quit
    if (in->keyDown(SDLK_ESCAPE))
        end();

    //temporary player movement velocity
    int playerMoveX = 0;

    //update game based on current game state
    switch(gameState)
    {
        case STATE_START:
            //check for enter key to start the game
            if (in->keyDown(SDLK_RETURN))
                gameState = STATE_GAME;

            break;
        case STATE_GAME:
            //update all scene entities
            scene.update();

            //update shooting timer
            if (shootTimer > 0)
                shootTimer--;

            //update enemy spawner timer
            if (enemyTimer > 0)
                enemyTimer--;

            //scroll the background image
            backgroundScroll += 3;
            if (backgroundScroll > backgroundImage.getHeight())
                backgroundScroll = 0;

            //determine whether to move the player
            if(in->keyDown(SDLK_LEFT))
                playerMoveX -= PLAYER_SPEED;
            if(in->keyDown(SDLK_RIGHT))
                playerMoveX += PLAYER_SPEED;

            //move the player based on input
            player->setX(player->getX()+playerMoveX);

            //shoot a projectile when space bar is pressed and the player can shoot
            if (in->keyDown(SDLK_SPACE) && shootTimer == 0)
            {
                //spawn a projectile using the entity factory
                scene.addNode(factory.create(ENTITY_PROJECTILE, player->getX()+12, player->getY()-8));
                //set shooting timer to 12 frames to limit shooting speed
                shootTimer = 12;
            }

            //spawn a new enemy when the enemy spawner timer reaches 0
            if (enemyTimer == 0)
            {
                //spawn an enemy at a random position using the entity factory
                scene.addNode(factory.create(ENTITY_ENEMY, rand()%camera.getWidth(), -32));
                //set enemy spawner timer to 24 frames to limit how fast enemies spawn
                enemyTimer = 24;
            }

            //check for P key to pause the game
            if (in->keyDown(SDLK_p))
                gameState = STATE_PAUSED;

            //show game over screen when lives reach 0
            if (factory.getLives() <= 0)
                gameState = STATE_OVER;

            break;
        case STATE_PAUSED:
            //check for enter key to uppause the game
            if (in->keyDown(SDLK_RETURN))
                gameState = STATE_GAME;

            break;
        case STATE_OVER:
            //check for enter key to restart the game
            if (in->keyDown(SDLK_RETURN))
            {
                //reset timers
                shootTimer = 0;
                enemyTimer = 0;

                //start background position at 0
                backgroundScroll = 0;

                //clear the scene
                scene.removeNode(NULL);

                //spawn a new player in the game
                player = (Player*)factory.create(ENTITY_PLAYER, PLAYER_X, PLAYER_Y);
                scene.addNode(player);

                //reset score and lives
                factory.setScore(0);
                factory.setLives(PLAYER_LIVES);

                //go to game screen
                gameState = STATE_GAME;
            }

            break;
    }
}

/*
    draw:
        Draws everything in the game.
    arguments:
        g - Graphics object to use for drawing
    algorithm:
        draw the scrolling background in two parts
        draw the scene
        define strings to contain lives and score text
        concatenate text with integer values of lives and score
        raw the lives and score strings on screen
        if the game state is STATE_START
            draw the start splash screen image
        if the game state is STATE_PAUSED
            draw the paused splash screen image
        if the game state is STATE_OVER
            draw the game over splash screen image
*/
void GameScene::draw(Graphics* g)
{
    //draw the scrolling background in two parts
    backgroundImage.draw(camera.getX(), camera.getY()+backgroundScroll-backgroundImage.getHeight(), g);
    backgroundImage.draw(camera.getX(), camera.getY()+backgroundScroll, g);

    //draw the scene
    scene.draw(&camera, g);

    //strings to contain lives and score text
    char livesText[64];
    char scoreText[64];

    //concatenate text with integer values of lives and score
    sprintf(livesText, "Lives: %d", factory.getLives());
    sprintf(scoreText, "Score: %d", factory.getScore());

    //draw the lives and score strings on screen
    font.draw(livesText, 50, 48, 255, 255, 255, g);
    font.draw(scoreText, 650, 48, 255, 255, 255, g);

    //draw the screen overlays
    if (gameState == STATE_START)
        startImage.draw(camera.getX(), camera.getY(), g);
    else if (gameState == STATE_PAUSED)
        pausedImage.draw(camera.getX(), camera.getY(), g);
    else if (gameState == STATE_OVER)
        overImage.draw(camera.getX(), camera.getY(), g);
}

/*
    free:
        Frees memory used by the game scene.
*/
void GameScene::free()
{
    //free memory of factory and font
    factory.free();
    font.free();

    //free the background images
    backgroundImage.free();
    startImage.free();
    pausedImage.free();
    overImage.free();

    //clear the scene
    scene.removeNode(NULL);
}
