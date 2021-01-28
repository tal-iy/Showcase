/*
    Title: Module 3: Program 1
    Author: Vitaliy Shydlonok
    Date: 2/5/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, then continuously draws
        a background and a sprite. Moves the sprite in
        a random diagonal direction, and bounces it off
        the borders of the screen. Does this until the
        window is closed.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/sprite.bmp"
*/

#include <SDL/SDL.h>

SDL_Surface* backgroundImage = NULL; //background image surface
SDL_Surface* spriteImage = NULL; //sprite image surface
SDL_Surface* backBuffer = NULL; //screen buffer surface

SDL_Rect spritePos; //SDL_Rect to hold sprites position

void MoveBitMap(int& xPosition, int& yPosition, int width, int height, int& xDirection, int& yDirection);
SDL_Surface* LoadImage(char* fileName);
bool LoadFiles();
void FreeFiles();
bool ProgramIsRunning();

int main(int argc, char* args[])
{
    if(SDL_Init(SDL_INIT_EVERYTHING) < 0)
    {
        printf("Failed to initialize SDL!\n");
        return 0;
    }

    //set up SDL window
    backBuffer = SDL_SetVideoMode(800, 600, 32, SDL_SWSURFACE);
    SDL_WM_SetCaption("Bouncing Off The Walls", NULL);

    if(!LoadFiles())
    {
        printf("Failed to load all files!\n");
        FreeFiles();
        SDL_Quit();
        return 0;
    }

    //starting position of the sprite
    spritePos.x = rand()%(800-32);
    spritePos.y = rand()%(600-32);

    //position of the sprite used for movement
    int xPos = spritePos.x;
    int yPos = spritePos.y;

    //speed/direction of the sprite used for movement
    int xDir = 4;
    int yDir = 4;

    while(ProgramIsRunning())
    {
        xPos = spritePos.x; //copy x position of the sprite
        yPos = spritePos.y; //copy y position of the sprite
        MoveBitMap(xPos, yPos, 96, 96, xDir, yDir); //update position and movement direction of the sprite
        spritePos.x = xPos; //update x position of the sprite
        spritePos.y = yPos; //update y position of the sprite

        SDL_BlitSurface(backgroundImage, NULL, backBuffer, NULL); //draw background
        SDL_BlitSurface(spriteImage, NULL, backBuffer, &spritePos); //draw sprite

        SDL_Delay(20);
        SDL_Flip(backBuffer);
    }

    FreeFiles();
    SDL_Quit();

    return 0;
}

/*
    Function:
        void MoveBitMap: updates the position and direction of a moving bitmap, negates direction when a screen edge is reached.

    Parameters:
        int& xPosition: x component of the position to use and update
        int& yPosition: y component of the position to use and update
        int width: width of the bitmap collision box
        int height: height of the bitmap collision box
        int& xDirection: x component of the direction to use and update
        int& yDirection: y component of the direction to use and update

    Algorithm:
        copy parameters to temporary variables
        update x position
        check if x position is past right screen edge
            bind the x position to the right screen edge
            negate x direction
        check if x position is past left screen edge
            bind the x position to the left screen edge
            negate x direction
        update y position
        check if y position is past bottom screen edge
            bind the y position to the bottom screen edge
            negate y direction
        check if y position is past top screen edge
            bind the y position to the top screen edge
            negate y direction
        update parameters based on new temporary variable values

*/
void MoveBitMap(int& xPosition, int& yPosition, int width, int height, int& xDirection, int& yDirection)
{
    //copy parameters
    int x = xPosition;
    int y = yPosition;
    int xDir = xDirection;
    int yDir = yDirection;

    //update x position
    x += xDir;

    //check for right edge collision
    if (x >= 800-width)
    {
        x = 800-width;
        xDir *= -1;
    }

    //check for left edge collision
    if (x <= 0)
    {
        x = 0;
        xDir *= -1;
    }

    //update y position
    y += yDir;

    //check for bottom edge collision
    if (y >= 600-height)
    {
        y = 600-height;
        yDir *= -1;
    }

    //check for top edge collision
    if (y <= 0)
    {
        y = 0;
        yDir *= -1;
    }

    //update parameters
    xPosition = x;
    yPosition = y;
    xDirection = xDir;
    yDirection = yDir;
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

        if(processedImage != NULL)
        {
            Uint32 colorKey = SDL_MapRGB(processedImage->format, 0xFF, 0x00, 0xFF); //generate pink color using RGB values
            SDL_SetColorKey(processedImage, SDL_SRCCOLORKEY, colorKey); //mask pink color as transparent
        }
    }

    return processedImage;
}

bool LoadFiles()
{
    //load and validate background image
    backgroundImage = LoadImage("graphics/background.bmp");

    if(backgroundImage == NULL)
        return false;

    //load and validate sprite image
    spriteImage = LoadImage("graphics/sprite.bmp");

    if(spriteImage == NULL)
        return false;

    return true;
}

void FreeFiles()
{
    //free memory reserved for background image
    if (backgroundImage != NULL)
    {
        SDL_FreeSurface(backgroundImage);
        backgroundImage = NULL;
    }

    //free memory reserved for sprite image
    if (spriteImage != NULL)
    {
        SDL_FreeSurface(spriteImage);
        spriteImage = NULL;
    }
}

bool ProgramIsRunning()
{
    SDL_Event event;

    bool running = true;

    while(SDL_PollEvent(&event))
    {
        if(event.type == SDL_QUIT)
            running = false;
    }

    return running;
}
