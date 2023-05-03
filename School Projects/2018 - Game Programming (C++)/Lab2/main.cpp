/*
    Title: Module 2: Lab 2
    Author: Vitaliy Shydlonok
    Date: 1/29/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, draws a background, and then
        continuously draws two sprites at random positions,
        until the window is closed.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/sprite1.bmp"
        "graphics/sprite2.bmp"
*/

#include <SDL/SDL.h>

SDL_Surface* background = NULL;
SDL_Surface* sprite1 = NULL; //SDL_Surface pointer to contain first sprite image
SDL_Surface* sprite2 = NULL; //SDL_Surface pointer to contain second sprite image
SDL_Surface* backbuffer = NULL;

bool ProgramIsRunning();
bool LoadImages();
void FreeImages();

int main(int argc, char* args[])
{
    if(SDL_Init(SDL_INIT_EVERYTHING) < 0)
    {
        printf("SDL failed to initialize!\n");
        SDL_Quit();
        return 0;
    }

    backbuffer = SDL_SetVideoMode(800, 600, 32, SDL_SWSURFACE);
    SDL_WM_SetCaption("Vitaliy Shydlonok", NULL); //set window caption to "Vitaliy Shydlonok"

    if(!LoadImages())
    {
        printf("Images failed to load!\n");
        FreeImages();
        SDL_Quit();
        return 0;
    }
    SDL_BlitSurface(background, NULL, backbuffer, NULL ); //blit the background to the buffer

    //Here is the Game Loop!!!
    while(ProgramIsRunning())
    {
        SDL_Rect spritePos1;           //define a structure (SDL_Rect) that contains data for a rectangle (spritePos1).
        spritePos1.x = rand()%800;     //select an x coordinate between 0 and 800
        spritePos1.y = rand()%600;     //select a y coordinate between 0 and 600
        SDL_BlitSurface(sprite1, NULL, backbuffer, &spritePos1);  //blit the rectangle to the buffer, using sprite1

        SDL_Rect spritePos2;           //define a structure (SDL_Rect) that contains data for a rectangle (spritePos2).
        spritePos2.x = rand()%800;     //select an x coordinate between 0 and 800
        spritePos2.y = rand()%600;     //select a y coordinate between 0 and 600
        SDL_BlitSurface(sprite2, NULL, backbuffer, &spritePos2);  //blit the rectangle to the buffer, using sprite2

        SDL_Flip(backbuffer);     //flip the buffer and draw to the game screen
        SDL_Delay(100);           //wait 1/10th of a second before drawing the next sprite to a random location
    }

    SDL_Quit();
    return 1;
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

bool LoadImages()
{
    background = SDL_LoadBMP("graphics/background.bmp");
    if(background == NULL)
        return false;

    //load and validate first sprite
    sprite1 = SDL_LoadBMP("graphics/sprite1.bmp");
    if(sprite1 == NULL)
        return false;

    //load and validate second sprite
    sprite2 = SDL_LoadBMP("graphics/sprite2.bmp");
    if(sprite2 == NULL)
        return false;

    return true;
}

void FreeImages()
{
    if(background != NULL)
    {
        SDL_FreeSurface(background);
        background = NULL;
    }

    //free memory reserved for sprite1
    if(sprite1 != NULL)
    {
        SDL_FreeSurface(sprite1);
        sprite1 = NULL;
    }

    //free memory reserved for sprite2
    if(sprite2 != NULL)
    {
        SDL_FreeSurface(sprite2);
        sprite2 = NULL;
    }
}
