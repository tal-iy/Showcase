/*
    Title: Module 5: Program 3
    Author: Vitaliy Shydlonok
    Date: 2/19/18
    Course & Section: CSC 222, 001W
    Description:
        Sets up an SDL window, then continuously draws
        a background and two spaceship sprites. Moves one
        sprite using the arrow keys and the other using
        the WASD keys, and keeps them both within the borders
        of the screen, playing a sound when either one hits a
        border. Plays background music, using the Space key
        for play and pause, and the Tab key for stop. Does
        this until the window is closed or the ESC key
        is pressed.
    Data Requirements:
        "graphics/background.bmp"
        "graphics/spaceship.bmp"
        "audio/music.mp3"
        "audio/hit1.wav"
        "audio/hit2.wav"
*/

#include <SDL/SDL.h>
#include <SDL/SDL_mixer.h>

Mix_Music *backgroundMusic = NULL; //background music sound
Mix_Chunk *soundHit1 = NULL; //first hit sound
Mix_Chunk *soundHit2 = NULL; //second hit sound
int channelHit1 = -1; //channel in which the first hit sound is playing
int channelHit2 = -1; //channel in which the second hit sound is playing

SDL_Surface* backgroundImage = NULL; //background image surface
SDL_Surface* spriteImage1 = NULL; //first spaceship image surface
SDL_Surface* spriteImage2 = NULL; //second spaceship image surface
SDL_Surface* backBuffer = NULL; //screen buffer surface

int spriteX1 = 150; //x position of the first spaceship sprite
int spriteY1 = 400; //y position of the first spaceship sprite
int spriteX2 = 550; //x position of the second spaceship sprite
int spriteY2 = 400; //y position of the second spaceship sprite

int WINDOW_WIDTH = 800; //width of the window
int WINDOW_HEIGHT = 600; //height of the window

bool LoadFiles();
void FreeFiles();

bool CheckBorder(int& x, int& y, int w, int h);
SDL_Surface* LoadImage(char* fileName);
void DrawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y);
bool ProgramIsRunning();

int main(int argc, char* args[])
{
    //initialize SDL
    if(SDL_Init(SDL_INIT_EVERYTHING) < 0)
        return false;

    //initialize audio
    if(Mix_OpenAudio(22050, MIX_DEFAULT_FORMAT, 2, 2048 ) == -1 )
    {
        SDL_Quit();
        return 0;
    }

    //create a game window and give it a title caption
    backBuffer = SDL_SetVideoMode(WINDOW_WIDTH, WINDOW_HEIGHT, 32, SDL_SWSURFACE);
    SDL_WM_SetCaption("Use arrows and WASD keys to move the spaceships, ESC key to quit", NULL);


    if(!LoadFiles())
    {
        FreeFiles();
        Mix_CloseAudio();
        SDL_Quit();
        return 0;
    }

    //start playing background music
    Mix_PlayMusic(backgroundMusic, -1);

    while(ProgramIsRunning())
    {
        //retrieve keyboard state
        Uint8* keys = SDL_GetKeyState(NULL);

        //handle arrow keys for the first spaceships movement
        if(keys[SDLK_LEFT])
            spriteX1-=8;
        if(keys[SDLK_RIGHT])
            spriteX1+=8;
        if(keys[SDLK_UP])
            spriteY1-=8;
        if(keys[SDLK_DOWN])
            spriteY1+=8;

        //handle WASD keys for the second spaceships movement
        if(keys[SDLK_a])
            spriteX2-=8;
        if(keys[SDLK_d])
            spriteX2+=8;
        if(keys[SDLK_w])
            spriteY2-=8;
        if(keys[SDLK_s])
            spriteY2+=8;

        //make sure the sprites are within the screen borders
        bool collision1 = CheckBorder(spriteX1, spriteY1, 80, 94);
        bool collision2 = CheckBorder(spriteX2, spriteY2, 80, 94);

        if (collision1 && Mix_Playing(channelHit1) == 0) //check for collision and if the first collision sound isn't already playing
            channelHit1 = Mix_PlayChannel(-1, soundHit1, 0); //play the first collision sound
        if (collision2 && Mix_Playing(channelHit2) == 0) //check for collision and if the second collision sound isn't already playing
            channelHit2 = Mix_PlayChannel(-1, soundHit2, 0); //play the second collision sound

        //draw the scene
        DrawImage(backgroundImage,backBuffer, 0, 0);
        DrawImage(spriteImage1, backBuffer, spriteX1, spriteY1);
        DrawImage(spriteImage2, backBuffer, spriteX2, spriteY2);

        SDL_Delay(20);
        SDL_Flip(backBuffer);
    }

    FreeFiles();
    Mix_CloseAudio();
    SDL_Quit();

    return 1;
}

/*
    Function:
        bool CheckBorder: determines whether a bitmap is outside of the window
            and binds the bitmap position to the window borders.

    Returns:
        true if the bitmap position was corrected
        false if the bitmap was already within the window borders

    Parameters:
        int& x: x component of the position to use and update
        int& y: y component of the position to use and update
        int w: width of the bitmap collision box
        int h: height of the bitmap collision box

    Algorithm:
        check if x position is past right window edge
            bind the x position to the right window edge
        check if x position is past left window edge
            bind the x position to the left window edge
        check if y position is past bottom window edge
            bind the y position to the bottom window edge
        check if y position is past top window edge
            bind the y position to the top window edge
*/
bool CheckBorder(int& x, int& y, int w, int h)
{
    bool changed = false;

    //check for right edge collision
    if (x > WINDOW_WIDTH-w)
    {
        x = WINDOW_WIDTH-w;
        changed = true;
    }

    //check for left edge collision
    if (x < 0)
    {
        x = 0;
        changed = true;
    }

    //check for bottom edge collision
    if (y > WINDOW_HEIGHT-h)
    {
        y = WINDOW_HEIGHT-h;
        changed = true;
    }

    //check for top edge collision
    if (y < 0)
    {
        y = 0;
        changed = true;
    }

    return changed;
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

    SDL_BlitSurface(image, NULL, destSurface, &destRect);
}

bool ProgramIsRunning()
{
    SDL_Event event;

    bool running = true;

    while(SDL_PollEvent(&event))
    {
        if(event.type == SDL_QUIT)
            running = false;

        if(event.type == SDL_KEYDOWN )
        {
            switch(event.key.keysym.sym )
            {
                case SDLK_ESCAPE: //quit the game with the ESC key
                    running = false;
                    break;

                case SDLK_TAB:  //stop the music with the Tab Key
                    Mix_HaltMusic();
                    break;

                case SDLK_SPACE: //play and pause the music with the Space key
                    if(!Mix_PlayingMusic())
                    {
                        Mix_PlayMusic(backgroundMusic, -1);
                    }
                    else
                    {
                        if(Mix_PausedMusic())
                        {
                            Mix_ResumeMusic();
                        }
                        else
                        {
                            Mix_PauseMusic();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    return running;
}

bool LoadFiles()
{
    //load image files
    spriteImage1 = LoadImage("graphics/spaceship1.bmp");
    spriteImage2 = LoadImage("graphics/spaceship2.bmp");
    backgroundImage = LoadImage("graphics/background.bmp");

    //validate image files
    if(spriteImage1 == NULL || spriteImage2 == NULL || backgroundImage == NULL)
        return false;

    //load audio files
    backgroundMusic = Mix_LoadMUS("audio/music.mp3");
    soundHit1 = Mix_LoadWAV("audio/hit1.wav");
    soundHit2 = Mix_LoadWAV("audio/hit2.wav");

    //validate audio files
    if(backgroundMusic == NULL || soundHit1 == NULL || soundHit2 == NULL)
        return false;

    return true;
}

void FreeFiles()
{
    //free memory reserved for image resources
    if(spriteImage1 != NULL)
    {
        SDL_FreeSurface(spriteImage1);
        spriteImage1 = NULL;
    }

    if(spriteImage2 != NULL)
    {
        SDL_FreeSurface(spriteImage2);
        spriteImage2 = NULL;
    }

    if(backgroundImage != NULL)
    {
        SDL_FreeSurface(backgroundImage);
        backgroundImage = NULL;
    }

    //free memory reserved for audio resources
    if(backgroundMusic != NULL)
    {
        Mix_FreeMusic(backgroundMusic);
        backgroundMusic = NULL;
    }

    if(soundHit1 != NULL)
    {
        Mix_FreeChunk(soundHit1);
        soundHit1 = NULL;
    }

    if(soundHit2 != NULL)
    {
        Mix_FreeChunk(soundHit2);
        soundHit2 = NULL;
    }
}
