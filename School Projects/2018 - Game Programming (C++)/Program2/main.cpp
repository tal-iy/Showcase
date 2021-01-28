/*
    Title: Module 4: Program 2
    Author: Vitaliy Shydlonok
    Date: 2/12/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, then continuously draws
        a background and a spaceship sprite. Moves the
        sprite using the arrow keys, and keeps it within
        the borders of the screen. Does this until the
        window is closed or the escape key is pressed.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/spaceship.bmp"
*/

#include <SDL/SDL.h>

SDL_Surface* backgroundImage = NULL; //background image surface
SDL_Surface* spriteImage = NULL; //sprite image surface
SDL_Surface* backBuffer = NULL; //screen buffer surface

int spriteX = 350; //x position of the spaceship sprite
int spriteY = 250; //y position of the spaceship sprite

bool LoadFiles();
void FreeFiles();

void CheckBorder(int& x, int& y, int w, int h);
SDL_Surface* LoadImage(char* fileName);
void DrawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y);
bool ProgramIsRunning();

int main(int argc, char* args[])
{
    if(SDL_Init(SDL_INIT_EVERYTHING) < 0)
        return false;

    backBuffer = SDL_SetVideoMode(800, 600, 32, SDL_SWSURFACE);

    SDL_WM_SetCaption("Use the arrow keys to move the sprite around and ESC to exit.", NULL);

    if(!LoadFiles())
    {
        FreeFiles();
        SDL_Quit();
        return 0;
    }

    while(ProgramIsRunning())
    {
        //handle Input
        Uint8* keys = SDL_GetKeyState(NULL);

        if(keys[SDLK_ESCAPE])
            break;

        if(keys[SDLK_LEFT])
            spriteX-=8;

        if(keys[SDLK_RIGHT])
            spriteX+=8;

        if(keys[SDLK_UP])
            spriteY-=8;

        if(keys[SDLK_DOWN])
            spriteY+=8;

        //make sure the sprite is within the screen borders
        CheckBorder(spriteX, spriteY, 100, 90);

        //draw the scene
        DrawImage(backgroundImage,backBuffer, 0, 0);
        DrawImage(spriteImage, backBuffer, spriteX, spriteY);

        SDL_Delay(20);
        SDL_Flip(backBuffer);
    }

    FreeFiles();

    SDL_Quit();

    return 1;
}

/*
    Function:
        void CheckBorder: determines whether a bitmap is outside of the screen
            and binds the bitmap position to the screen borders.

    Parameters:
        int& x: x component of the position to use and update
        int& y: y component of the position to use and update
        int w: width of the bitmap collision box
        int h: height of the bitmap collision box

    Algorithm:
        check if x position is past right screen edge
            bind the x position to the right screen edge
        check if x position is past left screen edge
            bind the x position to the left screen edge
        check if y position is past bottom screen edge
            bind the y position to the bottom screen edge
        check if y position is past top screen edge
            bind the y position to the top screen edge
*/
void CheckBorder(int& x, int& y, int w, int h)
{
    //check for right edge collision
    if (x > 800-w)
        x = 800-w;

    //check for left edge collision
    if (x < 0)
        x = 0;

    //check for bottom edge collision
    if (y > 600-h)
        y = 600-h;

    //check for top edge collision
    if (y < 0)
        y = 0;
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

bool LoadFiles()
{
    spriteImage = LoadImage("graphics/spaceship.bmp");

    if(spriteImage == NULL)
        return false;

    backgroundImage = LoadImage("graphics/background.bmp");

    if(backgroundImage == NULL)
        return false;

    return true;

}

void FreeFiles()
{
    if(spriteImage != NULL)
    {
        SDL_FreeSurface(spriteImage);
        spriteImage = NULL;
    }

    if(backgroundImage != NULL)
    {
        SDL_FreeSurface(backgroundImage);
        backgroundImage = NULL;
    }
}
