/*
    Title: Module 7: Program 5
    Author: Vitaliy Shydlonok
    Date: 3/5/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, shows a splash screen, draws a background,
        a grid of brick sprites, a HUD with the number of bricks left, lives,
        and game score, a paddle that's controlled by the left and right
        arrow keys, and a ball that bounces around and can be started with
        the space bar key. The game is paused with the ESC key.
    Data Requirements:
        "graphics/brick.tff"
        "graphics/background.bmp"
        "graphics/ball.bmp"
        "graphics/blocks.bmp"
        "graphics/player.bmp"
        "graphics/foreground.bmp"
        "graphics/gameover.bmp"
        "graphics/gamepaused.bmp"
        "graphics/splash.bmp"
        "audio/ball_bounce.wav"
        "audio/ball_spawn.wav"
        "audio/explosion.wav"
        "audio/song.mp3"

    Code Modifications:
        player.score
            Added score variable to player structure to keep track
            of the current game score
        ResetGame()
            Set player.score to 0 when reseting the game
        DrawGame()
            Draws the score string under the lives string
        UpdateBall()
            Calls DestroyBlock() every time a block is destroyed
            in order to handle the score updating
        DestroyBlock()
            destroys a block at a specific index and increments the score by 1,
            if it's a red block then destroys the blocks to the left and to the right
            and increments the score by 3 instead of 1, increments the player lives
            by 1 every time the score reaches a multiple of 10.
*/

#include <SDL/SDL.h>
#include <SDL/SDL_mixer.h>
#include <SDL/SDL_ttf.h>

//Game constants
const int SCREEN_WIDTH = 800;
const int SCREEN_HEIGHT = 600;

const int PADDLE_WIDTH = 100;
const int PADDLE_HEIGHT = 20;
const int PADDLE_Y = 550;
const int BALL_WIDTH = 20;
const int BALL_HEIGHT = 20;
const int BALL_SPEED = 10;

const int PLAYER_SPEED = 10;

const int FPS = 30;
const int FRAME_DELAY = 1000/FPS;

const int GAMEAREA_X1 = 20;
const int GAMEAREA_Y1 = 20;
const int GAMEAREA_X2 = 598;
const int GAMEAREA_Y2 = 600;

const int BLOCK_COLUMNS = 11;
const int BLOCK_ROWS = 5;
const int BLOCK_WIDTH = 50;
const int BLOCK_HEIGHT = 25;

const int GS_SPLASH = 0;
const int GS_RUNNING = 1;
const int GS_GAMEOVER = 2;
const int GS_PAUSED = 3;

//Surfaces
SDL_Surface *Backbuffer = NULL;
SDL_Surface *BackgroundImage = NULL;
SDL_Surface *BallImage = NULL;
SDL_Surface *PlayerPaddleImage = NULL;
SDL_Surface *BlockImage = NULL;
SDL_Surface *SplashImage = NULL;
SDL_Surface *GameoverImage = NULL;
SDL_Surface *GamepausedImage = NULL;

//Font
TTF_Font *GameFont = NULL;

//Sounds
Mix_Chunk *BallBounceSound = NULL;
Mix_Chunk *BallSpawnSound = NULL;
Mix_Chunk *ExplosionSound = NULL;

//Music
Mix_Music *GameMusic = NULL;

//Game strutctures

struct Block
{
    SDL_Rect rect;
    bool alive;
    int frame;
};

struct Ball
{
    SDL_Rect rect;
    int xVel;
    int yVel;
    bool isLocked;
};

struct Player
{
    SDL_Rect rect;
    int lives;
    int score; //Current game score
};

//Game Variables

Block blocks[BLOCK_COLUMNS * BLOCK_ROWS];
Player player;
Ball ball;
int gameState;

//Function Definitions

SDL_Surface* LoadImage(char* fileName);
void DrawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y);
void DrawImageFrame(SDL_Surface* image, SDL_Surface* destSurface, int x, int y, int width, int height, int frame);
void DrawText(SDL_Surface* surface, char* string, int x, int y, TTF_Font* font, Uint8 r, Uint8 g, Uint8 b);
bool ProgramIsRunning();
bool LoadFiles();
void FreeFiles();
bool RectsOverlap(SDL_Rect rect1, SDL_Rect rect2);
bool InitSDL();
void SetBlocks();
void DestroyBlock(int index); //Destroys a block at a specific index
void DrawBlocks();
int NumBlocksLeft();
void ResetGame();
bool InitGame();
void UpdatePlayer();
void UpdateBall();
void UpdateSplash();
void DrawSplash();
void UpdateGame();
void DrawGame();
void DrawGamePaused();
void UpdateGameOver();
void DrawGameOver();
void RunGame();
void DrawScreen();
void FreeGame();

int main(int argc, char *argv[])
{
    if(!InitGame())
    {
        FreeGame();   //If InitGame failed, kill the program
        return 0;
    }

    while(ProgramIsRunning())
    {
        long int oldTime = SDL_GetTicks();  //We will use this later to see how long it took to update the frame
        SDL_FillRect(Backbuffer, NULL, 0);  //Clear the screen
        RunGame();                          //Update the game
        DrawScreen();                         //Draw the screen

        int frameTime = SDL_GetTicks() - oldTime;

        if(frameTime < FRAME_DELAY)            //Dont delay if we dont need to
            SDL_Delay(FRAME_DELAY - frameTime);     //Delay
        SDL_Flip(Backbuffer);               //Flip the screen
    }

    FreeGame();     //Gracefully release SDL and its resources.

    return 0;
}

SDL_Surface* LoadImage(char* fileName)
{
    SDL_Surface* imageLoaded = NULL;
    SDL_Surface* processedImage = NULL;

    imageLoaded = SDL_LoadBMP(fileName);

    if(imageLoaded != NULL)
    {
        processedImage = SDL_DisplayFormat(imageLoaded);
        SDL_FreeSurface(imageLoaded);

        if( processedImage != NULL )
        {
            Uint32 colorKey = SDL_MapRGB( processedImage->format, 0xFF, 0, 0xFF );
            SDL_SetColorKey( processedImage, SDL_SRCCOLORKEY, colorKey );
        }

    }

    return processedImage;
}

void DrawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y)
{
    SDL_Rect destRect;
    destRect.x = x;
    destRect.y = y;

    SDL_BlitSurface( image, NULL, destSurface, &destRect);
}

void DrawImageFrame(SDL_Surface* image, SDL_Surface* destSurface, int x, int y, int width, int height, int frame)
{
    //Draws a specific frame from an image
    SDL_Rect destRect;
    destRect.x = x;
    destRect.y = y;

    int columns = image->w/width;

    SDL_Rect sourceRect;
    sourceRect.y = (frame/columns)*height;
    sourceRect.x = (frame%columns)*width;
    sourceRect.w = width;
    sourceRect.h = height;

    SDL_BlitSurface( image, &sourceRect, destSurface, &destRect);
}

void DrawText(SDL_Surface* surface, char* string, int x, int y, TTF_Font* font, Uint8 r, Uint8 g, Uint8 b)
{
    SDL_Surface* renderedText = NULL;

    SDL_Color color;

    color.r = r;
    color.g = g;
    color.b = b;

    renderedText = TTF_RenderText_Solid( font, string, color );

    SDL_Rect pos;

    pos.x = x;
    pos.y = y;

    SDL_BlitSurface( renderedText, NULL, surface, &pos );
    SDL_FreeSurface(renderedText);
}

bool ProgramIsRunning()
{
    SDL_Event event;

    bool running = true;

    while(SDL_PollEvent(&event))
    {
        if(event.type == SDL_QUIT)
            running = false;

        if( event.type == SDL_KEYDOWN )
        {
            if(event.key.keysym.sym == SDLK_ESCAPE)
            {
                 if(gameState == GS_RUNNING)
                    gameState = GS_PAUSED;
                else if(gameState == GS_PAUSED)
                    gameState = GS_RUNNING;
            }
        }
    }

    return running;
}

bool LoadFiles()
{
    //Load images
    BackgroundImage = LoadImage("graphics/background.bmp");
    SplashImage = LoadImage("graphics/splash.bmp");
    GameoverImage = LoadImage("graphics/gameover.bmp");
    GamepausedImage = LoadImage("graphics/gamepaused.bmp");
    BallImage = LoadImage("graphics/ball.bmp");
    PlayerPaddleImage = LoadImage("graphics/player.bmp");
    BlockImage = LoadImage("graphics/blocks.bmp");

    //Error checking images
    if(BackgroundImage == NULL)
        return false;
    if(SplashImage == NULL)
        return false;
    if(GameoverImage == NULL)
        return false;
    if(GamepausedImage == NULL)
        return false;
    if(BallImage == NULL)
        return false;
    if(PlayerPaddleImage == NULL)
        return false;
    if(BlockImage == NULL)
        return false;

    //Load font
    GameFont = TTF_OpenFont("graphics/brick.ttf", 20);

    //Error check font
    if(GameFont == NULL)
        return false;

    //Load sounds
    BallBounceSound = Mix_LoadWAV("audio/ball_bounce.wav");
    BallSpawnSound = Mix_LoadWAV("audio/ball_spawn.wav");
    ExplosionSound = Mix_LoadWAV("audio/explosion.wav");

    //Error check sounds
    if(BallBounceSound == NULL)
        return false;

    if(BallSpawnSound == NULL)
        return false;

    if(ExplosionSound == NULL)
        return false;

    //Load music
    GameMusic = Mix_LoadMUS("audio/song.mp3");

    //Error check music
    if(GameMusic == NULL)
        return false;

    return true;
}

void FreeFiles()
{
    //Free images
    SDL_FreeSurface(BackgroundImage);
    SDL_FreeSurface(SplashImage);
    SDL_FreeSurface(GameoverImage);
    SDL_FreeSurface(GamepausedImage);
    SDL_FreeSurface(BallImage);
    SDL_FreeSurface(PlayerPaddleImage);
    SDL_FreeSurface(BallImage);

    //Free font
    TTF_CloseFont(GameFont);

    //Free sounds
    Mix_FreeChunk(BallBounceSound);
    Mix_FreeChunk(BallSpawnSound);
    Mix_FreeChunk(ExplosionSound);

    //Free music
    Mix_FreeMusic(GameMusic);
}

bool RectsOverlap(SDL_Rect rect1, SDL_Rect rect2)
{
    if(rect1.x >= rect2.x+rect2.w)
        return false;

    if(rect1.y >= rect2.y+rect2.h)
        return false;

    if(rect2.x >= rect1.x+rect1.w)
        return false;

    if(rect2.y >= rect1.y+rect1.h)
        return false;

    return true;
}

bool InitSDL()
{
    if(SDL_Init(SDL_INIT_EVERYTHING) == -1)
        return false;

    //Init audio subsystem
    if(Mix_OpenAudio( 22050, MIX_DEFAULT_FORMAT, 2, 2048 ) == -1)
    {
        return false;
    }

    //Init TTF subsystem
    if(TTF_Init() == -1)
    {
        return false;
    }

    //Generate screen
    Backbuffer = SDL_SetVideoMode(SCREEN_WIDTH, SCREEN_HEIGHT, 32, SDL_SWSURFACE );

    //Error check Backbuffer
    if(Backbuffer == NULL)
        return false;

    return true;
}

void SetBlocks()
{
    int i = 0;

    for(int x = 0; x < BLOCK_COLUMNS; x++)
    {
        for(int y = 0; y < BLOCK_ROWS; y++)
        {
            blocks[i].rect.x = x * BLOCK_WIDTH + GAMEAREA_X1 + x*3;
            blocks[i].rect.y = (y*2) * BLOCK_HEIGHT + GAMEAREA_Y1 + y*3;
            blocks[i].rect.w = BLOCK_WIDTH;
            blocks[i].rect.h = BLOCK_HEIGHT;
            blocks[i].alive = true;
            blocks[i].frame = i%4;
            i++;
        }
    }
}

/*
    Function:
        void DestroyBlock: destroys a block at a specific index and increments the score by 1,
                        if it's a red block then destroys the blocks to the left and to the right
                        and increments the score by 3 instead of 1,
                        increments the player lives by 1 every time the score reaches a multiple
                        of 10.

    Parameters:
        int index: index of the block being destroyed

    Algorithm:
        destroy block at index
        save the current score
        check if the block at index is red
            increment the score by 3
            check if a block to the left of index is valid and exists
                destroy block to the left of index
            check if a block to the right of index is valid and exists
                destroy block to the right of index
        else (if the block at index is not red)
            increment the score by 1
        check if the new score is a higher multiple of 10 than the saved score
            increment player lives by 1
*/
void DestroyBlock(int index)
{
    blocks[index].alive = false; //Destroy the block at index

    //Save the current score
    int scoreStart = player.score;

    //Check if the block is red
    if (blocks[index].frame == 0)
    {
        player.score += 3; //increment the score by 3 for a red block

        //Check for an existing block on the left and destroy it
        if (index-BLOCK_ROWS >= 0 && blocks[index-BLOCK_ROWS].alive)
            blocks[index-BLOCK_ROWS].alive = false;

        //Check for an existing block on the right and destroy it
        if (index+BLOCK_ROWS < BLOCK_ROWS*BLOCK_COLUMNS && blocks[index+BLOCK_ROWS].alive)
            blocks[index+BLOCK_ROWS].alive = false;
    }
    else
    {
        player.score ++; //Increment the score by 1 for a non-red block
    }

    //Check if the new score is a higher multiple of 10 than before
    if (player.score/10 > scoreStart/10)
        player.lives ++; //Increment player lives every multiple of 10
}

void DrawBlocks()
{
    for(int i = 0; i < BLOCK_COLUMNS*BLOCK_ROWS; i++)
    {
        if(blocks[i].alive)
        {
            DrawImageFrame(BlockImage, Backbuffer, blocks[i].rect.x, blocks[i].rect.y, blocks[i].rect.w, blocks[i].rect.h, blocks[i].frame);
        }
    }
}

int NumBlocksLeft()
{
    int result = 0;

    for(int i = 0; i < BLOCK_COLUMNS*BLOCK_ROWS; i++)
    {
        if(blocks[i].alive)
        {
            result++;
        }
    }

    return result;
}

void ResetGame()
{
    //Position the player's paddle
    player.rect.x = (GAMEAREA_X2-GAMEAREA_X1)/2 - PADDLE_WIDTH/2 + GAMEAREA_X1;
    player.rect.y = PADDLE_Y;
    player.rect.w = PADDLE_WIDTH;
    player.rect.h = PADDLE_HEIGHT;

    //Position the ball
    ball.rect.x = SCREEN_WIDTH/2 - BALL_WIDTH/2;
    ball.rect.y = SCREEN_HEIGHT/2 - BALL_HEIGHT/2;
    ball.rect.w = BALL_WIDTH;
    ball.rect.h = BALL_HEIGHT;

    //Play the spawn sound
    Mix_PlayChannel(-1, BallSpawnSound, 0);

    //Set blocks
    SetBlocks();
    ball.isLocked = true;
    player.lives = 3;
    player.score = 0; //Reset score on game reset
}

bool InitGame()
{
    //Init SDL
    if(!InitSDL())
        return false;

    //Load Files
    if(!LoadFiles())
        return false;

    //Initiatialize game variables

    //Set the title
    SDL_WM_SetCaption("Paddle Game 2!",NULL);

    //Play Music
    Mix_PlayMusic(GameMusic, -1);

    //Set the game state
    gameState = GS_SPLASH;

    return true;
}

void UpdatePlayer()
{
    Uint8 *keys = SDL_GetKeyState(NULL);

    //Move the paddle when the left/right key is pressed
    if(keys[SDLK_LEFT])
        player.rect.x -= PLAYER_SPEED;

    if(keys[SDLK_RIGHT])
        player.rect.x += PLAYER_SPEED;

    if(keys[SDLK_SPACE] && ball.isLocked)
    {
        ball.isLocked = false;
        ball.xVel = rand()%3 - 1;
        ball.yVel = BALL_SPEED;
    }

    //Make sure the paddle doesn't leave the screen
    if(player.rect.x < GAMEAREA_X1)
        player.rect.x = GAMEAREA_X1;

    if(player.rect.x > GAMEAREA_X2-player.rect.w)
        player.rect.x = GAMEAREA_X2-player.rect.w;
}

void UpdateBall()
{
    if(ball.isLocked) //If the ball is locked, position it on top of the paddle and cenetred on the X axis
    {
        int PaddleCenterX = player.rect.x + player.rect.w/2;
        ball.rect.x = PaddleCenterX - ball.rect.w/2;
        ball.rect.y = player.rect.y - ball.rect.h;
    }
    else
    {
        ball.rect.x += ball.xVel;

        //If the ball hits the player while moving on the X-Axis, make it bounce accordingly
        if(RectsOverlap(ball.rect, player.rect))
        {
            ball.rect.x -= ball.xVel;
            ball.xVel *= -1;
            Mix_PlayChannel(-1, BallBounceSound, 0);
        }
        else  //If not, check if it hit any blocks while moving on the X-Axis
        {
            for(int i = 0; i < BLOCK_COLUMNS*BLOCK_ROWS; i++)
            {
                if(blocks[i].alive && RectsOverlap(ball.rect, blocks[i].rect))
                {
                    ball.rect.x -= ball.xVel;
                    ball.xVel *= -1;

                    DestroyBlock(i); //Destroy the block and update score

                    Mix_PlayChannel(-1, ExplosionSound, 0);
                }
            }
        }

        ball.rect.y += ball.yVel;

        //If the ball hits the player while moving in the Y-Axis, make it bounce accordingly
        if(RectsOverlap(ball.rect, player.rect))
        {
            ball.rect.y -= ball.yVel;
            ball.yVel *= -1;

            //Make the X velocity the distance between the paddle's center and the ball's center devided by 5
            int ballCenterX = ball.rect.x+ball.rect.w/2;
            int paddleCenterX = player.rect.x+player.rect.w/2;

            ball.xVel = (ballCenterX - paddleCenterX)/5;
            Mix_PlayChannel(-1, BallBounceSound, 0);
        }
        else  //If not, check if it hit any blocks while moving on the Y-Axis
        {
            for(int i = 0; i < BLOCK_COLUMNS*BLOCK_ROWS; i++)
            {
                if(blocks[i].alive && RectsOverlap(ball.rect, blocks[i].rect))
                {
                    ball.rect.y -= ball.yVel;
                    ball.yVel *= -1;

                    DestroyBlock(i); //Destroy the block and update score

                    Mix_PlayChannel(-1, ExplosionSound, 0);
                }
            }
        }

        //Make sure the ball doesn't leave the screen and make it
        //bounce randomly
        if(ball.rect.y < GAMEAREA_Y1)
        {
            ball.rect.y = GAMEAREA_Y1;
            ball.yVel *= -1;
            Mix_PlayChannel(-1, BallBounceSound, 0);
        }

        if(ball.rect.x > GAMEAREA_X2 - ball.rect.w)
        {
            ball.rect.x = GAMEAREA_X2 - ball.rect.w;
            ball.xVel *= -1;
            Mix_PlayChannel(-1, BallBounceSound, 0);
        }

        if(ball.rect.x < GAMEAREA_X1)
        {
            ball.rect.x = GAMEAREA_X1;
            ball.xVel *= -1;
            Mix_PlayChannel(-1, BallBounceSound, 0);
        }

        //If the player looses the ball

        if(ball.rect.y > GAMEAREA_Y2)
        {
            ball.isLocked = true;

            //Reposition Ball
            int PaddleCenterX = player.rect.x + player.rect.w/2;
            ball.rect.x = PaddleCenterX - ball.rect.w/2;
            ball.rect.y = player.rect.y - ball.rect.h;

            player.lives--;
            Mix_PlayChannel(-1, BallSpawnSound, 0);
        }
    }
}

void UpdateSplash()
{
    Uint8 *keys = SDL_GetKeyState(NULL);

    if(keys[SDLK_RETURN])
    {
        //This will start a new game
        ResetGame();
        gameState = GS_RUNNING;
    }
}

void DrawSplash()
{
    DrawImage(SplashImage, Backbuffer, 0, 0);
}

void UpdateGame()
{
    UpdatePlayer();
    UpdateBall();

    if(player.lives <= 0)
        gameState = GS_GAMEOVER;

    if(NumBlocksLeft() <= 0)
        ResetGame();
}

void DrawGame()
{
    DrawImage(BackgroundImage, Backbuffer, 0, 0);
    DrawImage(BallImage, Backbuffer, ball.rect.x, ball.rect.y);
    DrawImage(PlayerPaddleImage, Backbuffer, player.rect.x, player.rect.y);
    DrawBlocks();

    char blocksText[64];
    char livesText[64];
    char scoreText[64]; //String to contain the score

    sprintf(blocksText, "Blocks: %d", NumBlocksLeft());
    sprintf(livesText, "Lives: %d", player.lives);
    sprintf(scoreText, "Score: %d", player.score); //Concatenate "Score:" with the integer score value
    DrawText(Backbuffer, blocksText, 645, 150, GameFont, 255, 255, 255);
    DrawText(Backbuffer, livesText, 645, 170, GameFont, 255, 255, 255);
    DrawText(Backbuffer, scoreText, 645, 220, GameFont, 50, 200, 50); //Draw the score string
}

void DrawGamePaused()
{
    DrawGame();
    DrawImage(GamepausedImage, Backbuffer, 0, 0);
}

void UpdateGameOver()
{
    Uint8 *keys = SDL_GetKeyState(NULL);

    if(keys[SDLK_SPACE])
    {
        gameState = GS_SPLASH;
    }
}

void DrawGameOver()
{
    DrawGame();
    DrawImage(GameoverImage, Backbuffer, 0, 0);
}

void RunGame()
{
    switch(gameState)
    {
    case GS_SPLASH:
        UpdateSplash();
        break;
    case GS_RUNNING:
        UpdateGame();
        break;
    case GS_GAMEOVER:
        UpdateGameOver();
        break;
    default:
        break;
    }
}

void DrawScreen()
{
    switch(gameState)
    {
    case GS_SPLASH:
        DrawSplash();
        break;
    case GS_RUNNING:
        DrawGame();
        break;
    case GS_GAMEOVER:
        DrawGameOver();
        break;
    case GS_PAUSED:
        DrawGamePaused();
        break;
    default:
        break;
    }
}

void FreeGame()
{
    Mix_HaltMusic();    //Stop the music
    FreeFiles();        //Release the files we loaded
    Mix_CloseAudio();   //Close the audio system
    TTF_Quit();         //Close the font system
    SDL_Quit();         //Close SDL
}
