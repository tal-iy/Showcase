/*
 * Assignment: Project 1
 * Author: Vitaliy Shydlonok
 * Date: 9/20/2019
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Cell {
    char value;
    struct Cell* prev;
    struct Cell* next;
};

struct Rule {
    char writeVal;
    char moveDir;
    int newState;
};

int main() {

    // Allocate space for the first cell in the tape
    struct Cell* start = malloc(sizeof(struct Cell));
    start->prev = NULL;
    start->next = NULL;
    start->value = 'A';
    struct Cell* head = start;

    char line[500];
    char fileName[100];

    // Ask for a file name
    printf("Input file:");
    scanf(" %99[^\n]", fileName);

    // Open a TM file for reading
    FILE* file = fopen(fileName,"r");
    if (file == NULL) {
        printf("That file doesn't exist!");
        return 0;
    }

    printf("Writing tape...\n");

    // Read the first line
    fscanf(file," %499[^\n]", line);
    for(int pos=0; line[pos] != '\0'; pos++) {

        // Add each cell to the linked list
        struct Cell* currentCell = malloc(sizeof(struct Cell));
        currentCell->prev = head;
        currentCell->next = NULL;
        currentCell->value = line[pos];
        head->next = currentCell;
        head = currentCell;
    }

    // Print the initial tape
    head = start;
    printf("Initial tape contents: ");
    while(head != NULL) {
        printf("%c", head->value);
        head = head->next;
    }

    int numStates = 0;
    int startState = 0;
    int endState = 0;
    char* end;

    // Read the next three values from the file
    fscanf(file," %499[^\n]", line);
    numStates = strtol(line, NULL, 10);
    fscanf(file," %499[^\n]", line);
    startState = strtol(line, NULL, 10);
    fscanf(file," %499[^\n]", line);
    endState = strtol(line, NULL, 10);

    // Allocate memory for the transition rules
    struct Rule **rules = (struct Rule**)malloc(numStates * sizeof(struct Rule*));
    for (int i = 0; i<numStates; i++)
        rules[i] = (struct Rule*)malloc(128 * sizeof(struct Rule));

    // Read the transition rules one by one
    while(fscanf(file," %499[^\n]", line) == 1) {
        int state = 0;
        char readVal = 0;
        char writeVal = 0;
        char moveDir = 0;
        int newState = 0;

        sscanf(line, "%*c%d%*c%c%*c%*c%*c%*c%c%*c%c%*c%d", &state, &readVal, &writeVal, &moveDir, &newState);

        rules[state][readVal].writeVal = writeVal;
        rules[state][readVal].moveDir = moveDir;
        rules[state][readVal].newState = newState;
    }

    // Simulate the TM
    head = start;
    int currentState = startState;
    int headPos = 0;
    while(currentState != endState) {
        int tempState = currentState;
        char tempValue = head->value;
        currentState = rules[tempState][tempValue].newState;
        head->value = rules[tempState][tempValue].writeVal;

        // Determine which direction to move the head
        if (rules[tempState][tempValue].moveDir == 'R') {
            if (head->next == NULL) {
                // Add a new blank cell if the end of the tape is reached
                struct Cell* currentCell = malloc(sizeof(struct Cell));
                currentCell->prev = head;
                currentCell->next = NULL;
                currentCell->value = 'B';
                head->next = currentCell;
            }

            // Go right
            head = head->next;
            headPos ++;
        } else if (rules[tempState][tempValue].moveDir == 'L') {
            // Go left
            head = head->prev;
            headPos --;
        }
    }

    // Print the final tape
    head = start;
    printf("\nFinal tape contents: ");
    while(head != NULL) {
        printf("%c", head->value);
        head = head->next;
    }

    // Free memory used by the tape
    head = start;
    struct Cell* nextCell = head->next;
    while(head != NULL) {
        free(head);
        head = nextCell;
        if (nextCell != NULL)
            nextCell = nextCell->next;
    }

    // Free memory used by the transition rules array
    for(int i=0; i<numStates; i++)
        free(rules[i]);
    free(rules);

    return 0;
}