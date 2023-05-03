/*
    Title: Module 3: Lab 4
    Author: Vitaliy Shydlonok
    Date: 2/5/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, then continuously draws
        a background and an animated bat sprite. Moves
        the bat sprite in the x direction, and wraps it
        around the screen when it reaches the right edge.
        Does this until the window is closed.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/bats.bmp"
*/

#include <SDL/SDL.h>

SDL_Surface* Background = NULL;
SDL_Surface* SpriteImage = NULL;
SDL_Surface* Backbuffer = NULL;

SDL_Rect SpritePos; //SDL_Rect for bat sprite position
int SpriteFrame = 0;
int FrameCounter = 0;

const int MaxSpriteFrame = 11;
const int FrameDelay = 4;

SDL_Surface* LoadImage(char* fileName);
bool LoadFiles();
void FreeFiles();
void DrawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y);
void DrawImageFrame(SDL_Surface* image, SDL_Surface* destSurface, int x, int y, int width, int height, int frame);
bool ProgramIsRunning();

int main(int argc, char* args[])
{
    if(SDL_Init(SDL_INIT_EVERYTHING) < 0)
    {
        printf("Failed to initialize SDL!\n");
        return 0;
    }

    Backbuffer = SDL_SetVideoMode(800, 600, 32, SDL_SWSURFACE);

    SDL_WM_SetCaption("Image Animation", NULL);

    if(!LoadFiles())
    {
        printf("Failed to load all files!\n");
        FreeFiles();
        SDL_Quit();

        return 0;
    }

    //starting position of the bat sprite
    SpritePos.x = 0;
    SpritePos.y = 250;

    while(ProgramIsRunning())
    {
        //update the bat sprites frame
        FrameCounter++;

        if(FrameCounter > FrameDelay)
        {
            FrameCounter = 0;
            SpriteFrame++;
        }

        if(SpriteFrame > MaxSpriteFrame)
            SpriteFrame = 0;

        //update the bat sprites position and wrap it around the screen
        SpritePos.x += 5;
        if (SpritePos.x > 800)
            SpritePos.x = -32;

        //render the scene
        DrawImage(Background,Backbuffer, 0, 0);
        DrawImageFrame(SpriteImage, Backbuffer, SpritePos.x, SpritePos.y, 32, 32, SpriteFrame);

        SDL_Delay(20);
        SDL_Flip(Backbuffer);
    }

    FreeFiles();

    SDL_Quit();

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

        if(processedImage != NULL)
        {
            Uint32 colorKey = SDL_MapRGB(processedImage->format, 0xFF, 0xFF, 0xFF); //mask white color as transparent
            SDL_SetColorKey(processedImage, SDL_SRCCOLORKEY, colorKey);
        }
    }

    return processedImage;
}

bool LoadFiles()
{
    Background = LoadImage("graphics/background.bmp");

    if(Background == NULL)
        return false;

    SpriteImage = LoadImage("graphics/bats.bmp"); //load bat sprite image

    if(SpriteImage == NULL)
        return false;

    return true;
}

void FreeFiles()
{
    SDL_FreeSurface(Background);
    SDL_FreeSurface(SpriteImage);
}

void DrawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y)
{
    SDL_Rect destRect;
    destRect.x = x;
    destRect.y = y;

    SDL_BlitSurface(image, NULL, destSurface, &destRect);
}

void DrawImageFrame(SDL_Surface* image, SDL_Surface* destSurface, int x, int y, int width, int height, int frame)
{
    SDL_Rect destRect;
    destRect.x = x;
    destRect.y = y;

    int columns = image->w/width;

    SDL_Rect sourceRect;
    sourceRect.y = (frame/columns)*height;
    sourceRect.x = (frame%columns)*width;
    sourceRect.w = width;
    sourceRect.h = height;

    SDL_BlitSurface(image, &sourceRect, destSurface, &destRect);
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
