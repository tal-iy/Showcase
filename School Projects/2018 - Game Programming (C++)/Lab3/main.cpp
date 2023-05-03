/*
    Title: Module 3: Lab 3
    Author: Vitaliy Shydlonok
    Date: 2/5/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, then continuously draws
        a background and two spaceship sprites. Moves
        both spaceship sprites in opposite x directions,
        and wraps them around the screen when they reach
        an edge. Does this until the window is closed.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/spaceship1.bmp"
        "graphics/spaceship2.bmp"
*/

#include <SDL/SDL.h>

SDL_Surface* Background = NULL;
SDL_Surface* SpriteImage1 = NULL; //SDL_Surface pointer to contain spaceship1 sprite image
SDL_Surface* SpriteImage2 = NULL; //SDL_Surface pointer to contain spaceship2 sprite image
SDL_Surface* Backbuffer = NULL;

SDL_Rect SpritePos1; //SDL_Rect for spaceship1 sprite position
SDL_Rect SpritePos2; //SDL_Rect for spaceship2 sprite position

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

    Backbuffer = SDL_SetVideoMode(800, 600, 32, SDL_SWSURFACE);

    SDL_WM_SetCaption("Color Keying", NULL);

    if(!LoadFiles())
    {
        printf("Failed to load files!\n");
        FreeFiles();
        SDL_Quit();

        return 0;
    }

    //starting positions of both spaceship sprites
    SpritePos1.x = 0;
    SpritePos1.y = 250;
    SpritePos2.x = 700;
    SpritePos2.y = 250;

    while(ProgramIsRunning())
    {
        //move spaceship1 in positive x direction and wrap around the screen
        SpritePos1.x+=5;
        if(SpritePos1.x > 800)
            SpritePos1.x = -100;

        //move spaceship2 in negative x direction and wrap around the screen
        SpritePos2.x-=8;
        if(SpritePos2.x < -100)
            SpritePos2.x = 800;

        //use temporary SDL_Rects to pass sprite positions, prevents SDL_BlitSurface from changing negative values to 0 after drawing
        SDL_Rect TempPos1 = SpritePos1;
        SDL_Rect TempPos2 = SpritePos2;

        //draw background
        SDL_BlitSurface(Background, NULL, Backbuffer, NULL);

        //draw both spaceship sprites using temporary SDL_Rect positions
        SDL_BlitSurface(SpriteImage1, NULL, Backbuffer, &TempPos1);
        SDL_BlitSurface(SpriteImage2, NULL, Backbuffer, &TempPos2);

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
            Uint32 colorKey = SDL_MapRGB(processedImage->format, 255, 0, 255);
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

    //load and validate first spaceship sprite
    SpriteImage1 = LoadImage("graphics/spaceship1.bmp");
    if(SpriteImage1 == NULL)
        return false;

    //load and validate second spaceship sprite
    SpriteImage2 = LoadImage("graphics/spaceship2.bmp");
    if(SpriteImage2 == NULL)
        return false;

    return true;
}

void FreeFiles()
{
    SDL_FreeSurface(Background);
    SDL_FreeSurface(SpriteImage1);
    SDL_FreeSurface(SpriteImage2);
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
