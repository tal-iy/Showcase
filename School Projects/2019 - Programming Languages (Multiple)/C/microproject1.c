/*
 * Assignment: Mini Project 1
 * Author: Vitaliy Shydlonok
 * Date: 9/13/2019
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Item {
    char name[32];
    float price;
};

int main()
{
    int shelves = 0;
    int slots = 0;

    // Ask for the size of the shelves
    printf("Enter the number of shelves in the unit:");
    scanf("%d", &shelves);
    printf("Enter the number of slots on each shelf:");
    scanf("%d", &slots);

    // Allocate memory for a 2D array of size: shelves x slots
    struct Item **items = (struct Item**)malloc(shelves * sizeof(struct Item*));
    for (int i = 0; i<shelves; i++)
        items[i] = (struct Item*)malloc(slots * sizeof(struct Item));

    // Mark all open slots to know which ones are empty
    for (int i=0; i<shelves; i++) {
        for (int j=0; j<slots; j++) {
            items[i][j].name[0] = '\0';
        }
    }

    char line[100];
    int shelf = 0;
    int slot = 0;

    int done = 0;
    while(done == 0) {

        // Read a whole input line
        printf("Enter an item to add:");
        scanf(" %99[^\n]", line);

        char name[32];
        float price = 0.0f;

        const char delim[3] = ", ";
        char *token;
        char *end;

        // Tokenize the input and get the first token: name
        token = strtok(line, delim);
        if (token != NULL)
            strncpy(name, token, 32);

        // Get the second token: price
        token = strtok(NULL, delim);
        if (token != NULL)
            price = strtof(token, &end);

        // Get the third token: shelf number
        token = strtok(NULL, delim);
        if (token != NULL)
            shelf = strtol(token, &end, 10);

        // Get the fourth token: slot number
        token = strtok(NULL, delim);
        if (token != NULL)
            slot = strtol(token, &end, 10);

        // Verify that the shelf and slot numbers are valid
        if (shelf > shelves || shelf < 1) {
            printf("Shelf number must be greater than 0 and less than %d!", (shelves+1));
        } else if (slot > slots || slot < 1) {
            printf("Slot number must be greater than 0 and less than %d!", (slots+1));
        } else {
            // Put the item into the slot
            strncpy(items[shelf-1][slot-1].name, name, 32);
            items[shelf-1][slot-1].price = price;
            printf("Item %s, priced at $%f, added to shelf %d in slot %d!", name, price, shelf, slot);
        }

        // Ask to repeat the adding process
        printf("\n\nWould you like to add another item? (Y/N):");
        scanf(" %c", line);
        if (line[0] != 'Y' && line[0] != 'y')
            done = 1;
    }

    done = 0;
    while(done == 0) {

        // Ask for a shelf coordinate to check
        printf("Enter the shelf to check:");
        scanf("%d", &shelf);
        printf("Enter the slot to check:");
        scanf("%d", &slot);

        // Verify that the shelf and slot numbers are valid
        if (shelf > shelves || shelf < 1) {
            printf("Shelf number must be greater than 0 and less than %d!", (shelves+1));
        } else if (slot > slots || slot < 1) {
            printf("Slot number must be greater than 0 and less than %d!", (slots+1));
        } else if (items[shelf-1][slot-1].name[0] == '\0') { // Verify that the slot is not empty
            printf("Slot %d on shelf %d is empty!", slot, shelf);
        } else {
            // Print the item information
            printf("Item %s priced at $%f!", items[shelf-1][slot-1].name, items[shelf-1][slot-1].price);
        }

        // Ask to repeat the quiery process
        printf("\n\nWould you like to look at another item? (Y/N):");
        scanf(" %c", line);
        if (line[0] != 'Y' && line[0] != 'y')
            done = 1;
    }

    // Free memory used by the 2D array
    for (int i = 0; i<shelves; i++)
        free(items[i]);
    free(items);
    items = NULL;

    return 0;
}


