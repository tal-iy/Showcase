/*
    Title: Midterm Project: One Player Pong
    Author: Vitaliy Shydlonok
    Date: 3/19/18
    Course & Section: CSC 222, 001W
    Description: Sets up an SDL window, shows a splash screen, draws a background,
        a HUD with the number of lives remaining and game score, a paddle that's
        controlled by the up and down arrow keys, and a ball that bounces around.
        The game is paused with the enter key and quit with the ESC key.

    Data Requirements:
        "graphics/font.tff"
        "graphics/background.bmp"
        "graphics/ball.bmp"
        "graphics/player.bmp"
        "graphics/goal.bmp"
        "graphics/gameStart.bmp"
        "graphics/gameOver.bmp"
        "graphics/gamePaused.bmp"
        "graphics/gameWin.bmp"
*/

#include <SDL/SDL.h>
#include <SDL/SDL_ttf.h>


//Constants
const int SCREEN_WIDTH = 800;
const int SCREEN_HEIGHT = 600;
const int FPS = 30;
const int FRAME_DELAY = 1000/FPS;

const int PADDLE_WIDTH = 20;
const int PADDLE_HEIGHT = 100;
const int PADDLE_SPEED = 10;

const int BALL_WIDTH = 20;
const int BALL_HEIGHT = 20;
const int BALL_SPEED = 10;
const int BALL_DELAY = 1000;

const int GS_START = 0;
const int GS_RUNNING = 1;
const int GS_GAMEOVER = 2;
const int GS_PAUSED = 3;
const int GS_WIN = 4;

//Surfaces
SDL_Surface *backBuffer = NULL;
SDL_Surface *backgroundImage = NULL;
SDL_Surface *ballImage = NULL;
SDL_Surface *playerImage = NULL;
SDL_Surface *goalImage = NULL;
SDL_Surface *gameStartImage = NULL;
SDL_Surface *gameOverImage = NULL;
SDL_Surface *gamePausedImage = NULL;
SDL_Surface *gameWinImage = NULL;

//Font
TTF_Font *gameFont = NULL;

//Game variables
SDL_Rect ballRect; //Size and position of the ball
int ballVelocityX; //X velocity of the ball
int ballVelocityY; //Y velocity of the ball

SDL_Rect playerRect; //Size and position of the player paddle
int playerLives; //Lives remaining
int playerScore; //Current game score

SDL_Rect goalRect; //Size and position of the goal paddle
int gameState; //Current state of the game
long int gameStartTime; //Used for the ball delay timer

//Function definitions
bool initGame();
void updateGame();
void drawGame();
void resetGame();
void freeGame();

void updatePlayer();
void updateBall();

bool loadFiles();
void freeFiles();

bool isRunning();
SDL_Surface* loadImage(char* fileName);
void drawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y);
void drawText(SDL_Surface* surface, char* string, int x, int y, TTF_Font* font, Uint8 r, Uint8 g, Uint8 b);
bool rectsOverlap(SDL_Rect rect1, SDL_Rect rect2);

/*
    Function:
        int main: Entry point into the program, takes care of initialization and the main game loop.
    Algorithm:
        Try to initialize the game,
            Quit the game if initialization fails.

        Loop until the game ends.
            Get the current game time.

            Update the game.
            Draw the game screen.

            Calculate how long the frame took to execute.
            If the frame took less than a defined amount of time to execute,
                Delay the game to limit the FPS.

        Release SDL and its resources.
*/
int main(int argc, char *argv[])
{
    if(!initGame())
    {
        freeGame(); //If initGame failed, kill the program
        return 0;
    }

    while(isRunning())
    {
        long int oldTime = SDL_GetTicks();  //Get the current game time

        updateGame(); //Update the game
        drawGame(); //Draw the game screen

        int frameTime = SDL_GetTicks() - oldTime; //Calculate how long the frame took to execute
        if(frameTime < FRAME_DELAY) //Check if the frame took less than a defined amount of time to execute
            SDL_Delay(FRAME_DELAY - frameTime); //Delay the game to limit the FPS
    }

    freeGame(); //Release SDL and its resources

    return 0;
}

/*
    Function:
        bool initGame: Initializes SDL, sets up the game window, and loads resource files.
    Returns:
        True if everything was initialized correctly, false if there were any errors.
    Algorithm:
        Initialize SDL.
        Initialize TTF subsystem.
        Create a game window and give it a title.
        Make sure the window initialized correctly.
        Load the resource files.
        Go to the start screen.
*/
bool initGame()
{
    //Initialize SDL
    if(SDL_Init(SDL_INIT_EVERYTHING) == -1)
        return false;

    //Initialize TTF subsystem
    if(TTF_Init() == -1)
        return false;

    //Create a game window and give it a title
    backBuffer = SDL_SetVideoMode(SCREEN_WIDTH, SCREEN_HEIGHT, 32, SDL_SWSURFACE );
    SDL_WM_SetCaption("Midterm: One Player Pong!", NULL);

    //Make sure the window initialized correctly
    if(backBuffer == NULL)
        return false;

    //Load Files
    if(!loadFiles())
        return false;

    //Set gameState to starting screen
    gameState = GS_START;

    return true;
}

/*
    Function:
        void updateGame: Updates the game based on what state the game is in.
    Algorithm:
        Retrieve the keyboard key states.

        If the game is on the start screen, the game over screen, or the you win screen,
            Check for the enter key,
                Restart the game.
                Reset the enter key.

        If the game is on the paused screen,
            Check for the enter key,
                Un-pause the game.
                Reset the enter key.

        If the game is on the main running screen,
            Update the player.
            Update the ball.
            Check for the enter key,
                Pause the game.
                Reset the enter key.

*/
void updateGame()
{
    Uint8 *keys = SDL_GetKeyState(NULL);

    switch(gameState)
    {
        case GS_START:
        case GS_GAMEOVER:
        case GS_WIN:
            if(keys[SDLK_RETURN])
            {
                resetGame(); //Restart the game
                keys[SDLK_RETURN] = false;
            }
            break;
        case GS_PAUSED:
            if(keys[SDLK_RETURN])
            {
                gameState = GS_RUNNING; //Un-pause the game
                keys[SDLK_RETURN] = false;
            }
            break;
        case GS_RUNNING:
            updatePlayer();
            updateBall();

            if(keys[SDLK_RETURN])
            {
                gameState = GS_PAUSED; //Pause the game
                keys[SDLK_RETURN] = false;
            }
            break;
        default:
            break;
    }
}

/*
    Function:
        void drawGame: Draws the game start splash screen, the main game background and paddles, the score and lives text,
                        the ball, and the overlay splash screens like "game over", "you win", and "paused".
    Algorithm:
        Clear the screen.

        If the game is on the start screen,
            Draw the start splash screen background.
        Otherwise,
            Draw the main game background.
            Draw the paddles.

            Generate strings for the lives remaining and the score.
            Draw the lives and score strings.
            Draw the ball.

            If it's game over,
                Draw the game over splash overlay image.
            Otherwise if the game is paused,
                Draw the paused screen overlay image.
            Otherwise if the game has been won,
                Draw the you win screen overlay image.

        Flip the back buffer.
*/
void drawGame()
{
    SDL_FillRect(backBuffer, NULL, 0); //Clear the screen

    if (gameState == GS_START)
        drawImage(gameStartImage, backBuffer, 0, 0); //Draw the start screen
    else
    {
        //Draw the background and paddles
        drawImage(backgroundImage, backBuffer, 0, 0);
        drawImage(playerImage, backBuffer, playerRect.x, playerRect.y);
        drawImage(goalImage, backBuffer, goalRect.x, goalRect.y);

        char livesText[64]; //String to contain the lives remaining
        char scoreText[64]; //String to contain the score

        sprintf(livesText, "Lives: %d", playerLives); //Concatenate "Lives:" with the integer lives value
        sprintf(scoreText, "Score: %d", playerScore); //Concatenate "Score:" with the integer score value

        drawText(backBuffer, livesText, 96, 48, gameFont, 0, 0, 0); //Draw the lives string
        drawText(backBuffer, scoreText, 548, 48, gameFont, 0, 0, 0); //Draw the score string

        //Draw the ball after the text so that the text is in the background
        drawImage(ballImage, backBuffer, ballRect.x, ballRect.y);

        //Draw the game over and game paused overlays
        if (gameState == GS_GAMEOVER)
            drawImage(gameOverImage, backBuffer, 0, 0);
        else if (gameState == GS_PAUSED)
            drawImage(gamePausedImage, backBuffer, 0, 0);
        else if (gameState == GS_WIN)
            drawImage(gameWinImage, backBuffer, 0, 0);
    }

    SDL_Flip(backBuffer); //Flip the back buffer
}

/*
    Function:
        void resetGame: Resets the position of the player and the ball, as well as the lives remaining and current score.
    Algorithm:
        Reset the player paddle's position and size.
        Reset the goal paddle's position and size.
        Reset the ball's position and size.
        Give the ball a random velocity.
        Reset the score to 0 and lives remaining to 3.
        Reset the ball delay timer.
        Start the game.
*/
void resetGame()
{
    //Position the player
    playerRect.x = SCREEN_WIDTH-PADDLE_WIDTH;
    playerRect.y = (SCREEN_HEIGHT/2)-(PADDLE_HEIGHT/2);
    playerRect.w = PADDLE_WIDTH;
    playerRect.h = PADDLE_HEIGHT;

    //Position the goal
    goalRect.x = 0;
    goalRect.y = (SCREEN_HEIGHT/2)-(PADDLE_HEIGHT/2);
    goalRect.w = PADDLE_WIDTH;
    goalRect.h = PADDLE_HEIGHT;

    //Position the ball
    ballRect.x = PADDLE_WIDTH;
    ballRect.y = rand()%(SCREEN_HEIGHT-ballRect.h);;
    ballRect.w = BALL_WIDTH;
    ballRect.h = BALL_HEIGHT;

    //Give the ball a random velocity
    ballVelocityX = BALL_SPEED;
    ballVelocityY = rand()%3 - 1;

    //Reset the score and lives remaining
    playerLives = 3;
    playerScore = 0;

    //Get current time for ball delay timer
    gameStartTime = SDL_GetTicks();

    //Set gameState to running screen
    gameState = GS_RUNNING;
}

/*
    Function:
        void freeGame: Frees resources from memory, closes the font system, and shuts down SDL.
*/
void freeGame()
{
    freeFiles(); //Release the files
    TTF_Quit(); //Close the font system
    SDL_Quit(); //Close SDL
}

/*
    Function:
        void updatePlayer: Handles keyboard input for moving the player up and down.
    Algorithm:
        Retrieve the keyboard key states.
        If the up arrow key is pressed,
            Move the player up.
        If the down arrow key is pressed,
            Move the player down.

        If the player hits the top of the screen,
            Bind the player to the top of the screen.
        If the player hits the bottom of the screen,
            Bind the player to the bottom of the screen.

*/
void updatePlayer()
{
    Uint8 *keys = SDL_GetKeyState(NULL);

    //Handle arrow keys for movement
    if(keys[SDLK_UP])
        playerRect.y -= PADDLE_SPEED;
    if(keys[SDLK_DOWN])
        playerRect.y += PADDLE_SPEED;

    //Prevent player from going off screen
    if (playerRect.y < 0)
        playerRect.y = 0;
    if (playerRect.y + playerRect.h > SCREEN_HEIGHT)
        playerRect.y = SCREEN_HEIGHT - playerRect.h;
}

/*
    Function:
        void updateBall: Updates the ball's position, bounces it off the walls and paddles, and keeps track of the
                        score and lives remaining.
    Algorithm:
        If enough time has passed since the ball was reset,
            Update the ball's Y position.
            If the ball hits the player on the Y axis,
                Move the ball back.
                Negate the ball's vertical velocity.
            Otherwise, if the ball hits the goal on the Y axis,
                Move the ball back.
                Negate the ball's vertical velocity.
                Increase the score by 1.

            Update the ball's X position.
            If the ball hits the player on the X axis,
                Move the ball back.
                Negate the ball's horizontal velocity.
                Make the ball's X velocity the distance between the paddle's center and the ball's center devided by 5.
            Otherwise, if the ball hits the goal on the X axis,
                Move the ball back.
                Negate the ball's horizontal velocity.
                Increase the score by 1.

            If the ball hits the top of the screen,
                Move the ball back into the screen.
                Negate the ball's vertical velocity.
            If the ball hits the bottom of the screen,
                Move the ball back into the screen.
                Negate the ball's vertical velocity.
            If the ball hits the left side of the screen,
                Move the ball back into the screen.
                Negate the ball's horizontal velocity.

            If the ball hits the right side of the screen,
                Decrease the lives remaining by 1.
                Reset the ball delay timer.
                Move the ball to a random position on the left side of the screen.
                Give the ball a random velocity.

        If lives reach 0,
            Show the "game over" screen.
        If score reaches 10,
            Show the "you win" screen.

*/
void updateBall()
{
    //Wait a while before letting the ball move when the ball is reset
    if (SDL_GetTicks() - gameStartTime > BALL_DELAY)
    {
        //Update the ball's Y position
        ballRect.y += ballVelocityY;

        //Check if the ball hit the player while moving on the Y axis
        if(rectsOverlap(ballRect, playerRect))
        {
            ballRect.y -= ballVelocityY;
            ballVelocityY *= -1;
        }
        else if (rectsOverlap(ballRect, goalRect)) //Check if the ball hit the goal while moving on the Y axis
        {
            ballRect.y -= ballVelocityY;
            ballVelocityY *= -1;

            playerScore ++; //Increase score
        }

        //Update the ball's X position
        ballRect.x += ballVelocityX;

        //Check if the ball hit the player while moving on the X axis
        if(rectsOverlap(ballRect, playerRect))
        {
            ballRect.x -= ballVelocityX;
            ballVelocityX *= -1;

            ballVelocityY = ((ballRect.y + ballRect.h/2) - (playerRect.y + playerRect.h/2))/5; //Make the X velocity the distance between the paddle's center and the ball's center devided by 5
        }
        else if (rectsOverlap(ballRect, goalRect)) //Check if the ball hit the goal while moving on the X axis
        {
            ballRect.x -= ballVelocityX;
            ballVelocityX *= -1;

            playerScore ++; //Increase score
        }

        //Bounce the ball off the top of the screen
        if (ballRect.y < 0)
        {
            ballRect.y = 0;
            ballVelocityY *= -1;
        }

        //Bounce the ball off the bottom of the screen
        if (ballRect.y + ballRect.h > SCREEN_HEIGHT)
        {
            ballRect.y = SCREEN_HEIGHT - ballRect.h;
            ballVelocityY *= -1;
        }

        //Bounce the ball off the left side of the screen
        if (ballRect.x < 0)
        {
            ballRect.x = 0;
            ballVelocityX *= -1;
        }

        //Handle the ball going past right side of the screen
        if (ballRect.x + ballRect.w > SCREEN_WIDTH)
        {
            playerLives --; //Decrease lives remaining

            gameStartTime = SDL_GetTicks(); //Reset the ball delay timer

            //Reset the ball's position
            ballRect.x = PADDLE_WIDTH;
            ballRect.y = rand()%(SCREEN_HEIGHT-ballRect.h);

            //Reset the ball's velocity
            ballVelocityX = BALL_SPEED;
            ballVelocityY = rand()%3 - 1;
        }
    }

    if (playerLives <= 0)
        gameState = GS_GAMEOVER; //Show game over if lives reach 0

    if (playerScore >= 10)
        gameState = GS_WIN; //Show you win if score reaches 10
}

/*
    Function:
        bool loadFiles: Loads image and font resources and returns whether it was successful.
    Returns:
        True if loading was successful, false otherwise.
*/
bool loadFiles()
{
    //Load images
    backgroundImage = loadImage("graphics/background.bmp");
    ballImage = loadImage("graphics/ball.bmp");
    playerImage = loadImage("graphics/player.bmp");
    goalImage = loadImage("graphics/goal.bmp");
    gameStartImage = loadImage("graphics/gameStart.bmp");
    gameOverImage = loadImage("graphics/gameOver.bmp");
    gamePausedImage = loadImage("graphics/gamePaused.bmp");
    gameWinImage = loadImage("graphics/gameWin.bmp");

    //Error checking images
    if(backgroundImage == NULL || ballImage == NULL || playerImage == NULL || goalImage == NULL || gameStartImage == NULL || gameOverImage == NULL || gamePausedImage == NULL || gameWinImage == NULL)
        return false;

    //Load font
    gameFont = TTF_OpenFont("graphics/font.ttf", 32);

    //Error check font
    if(gameFont == NULL)
        return false;

    return true;
}

/*
    Function:
        void freeFiles: Frees image and font resources from memory.
*/
void freeFiles()
{
    //Free images
    SDL_FreeSurface(backgroundImage);
    SDL_FreeSurface(ballImage);
    SDL_FreeSurface(playerImage);
    SDL_FreeSurface(goalImage);
    SDL_FreeSurface(gameStartImage);
    SDL_FreeSurface(gameOverImage);
    SDL_FreeSurface(gamePausedImage);
    SDL_FreeSurface(gameWinImage);

    //Free font
    TTF_CloseFont(gameFont);
}

/*
    Function:
        bool isRunning: Returns whether the game is running.
    Returns:
        True if the game is still running, false if the game is quitting.
*/
bool isRunning()
{
    SDL_Event event;
    bool running = true;

    //Poll events queue for an SDL_QUIT event or ESCAPE key press
    while(SDL_PollEvent(&event))
        if(event.type == SDL_QUIT || (event.type == SDL_KEYDOWN && event.key.keysym.sym == SDLK_ESCAPE))
            running = false;

    return running;
}

/*
    Function:
        SDL_Surface* loadImage: Loads an image from a file into an SDL_Surface, using R:255, G:0, B:255 as the transparency color.
    Returns:
        An SDL_Surface containing the image for drawing later.
*/
SDL_Surface* loadImage(char* fileName)
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

/*
    Function:
        void drawImage: Draws an image on a surface at a specific position.
*/
void drawImage(SDL_Surface* image, SDL_Surface* destSurface, int x, int y)
{
    SDL_Rect destRect;
    destRect.x = x;
    destRect.y = y;

    SDL_BlitSurface( image, NULL, destSurface, &destRect);
}

/*
    Function:
        void drawText: Draws a string of text on a surface using a specific position, font, and color.
*/
void drawText(SDL_Surface* surface, char* string, int x, int y, TTF_Font* font, Uint8 r, Uint8 g, Uint8 b)
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

/*
    Function:
        bool rectsOverlap: Determines whether the first rectangle overlaps with the second rectangle.
    Returns:
        True if the two rectangles overlap, false if they don't.
*/
bool rectsOverlap(SDL_Rect rect1, SDL_Rect rect2)
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
