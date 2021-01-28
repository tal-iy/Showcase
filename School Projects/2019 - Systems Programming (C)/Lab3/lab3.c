//#include <unistd.h>
#include <getopt.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/*
	int binToDec: Takes a binary string and converts it to a decimal number.
*/
int binToDec(char *str) {
	
	// Start with 0
	int result = 0;
	
	// Determine the length of binary string
	int size = 0;
	while(str[size] != '\0')
		size++;
	
	// Go through every bit character from right to left
	int i;
	int mult = 1;
	for(i=size-1; i>=0; i--) {
		
		// Add the multiplier to the result if there is a '1' character bit
		if (str[i] == '1')
			result += mult;
		
		// Increase the multiplier by a power of 2
		mult = mult << 1;
	}
	
	return result;
}

/*
	int binToDec: Takes a hex string and converts it to a binary string.
*/
char* hexToBin(char *str, int numBits) {
	
	// Allocate a new binary string
	char *bits = (char*)malloc((numBits+1) * sizeof(char));
	
	// Start with all 0s
	int i;
	for(i=0; i<numBits; i++)
		bits[i] = '0';
	bits[numBits] = '\0';
	
	// Determine the length of address string
	int size = 0;
	while(str[size] != '\0')
		size++;
	
	// Convert the address hex string into a binary string
	for(i=size-1; i>=0; i--) {
		
		// Convert hex string to 4 bit decimal
		int num;
		if (str[i]-'0' < 10) num = str[i] - '0';
		else num = 10 + (str[i] - 'A');
		
		// Convert from 4 bit decimal to binary string
		if (num & 0x8) bits[numBits-(4*(size-i))] = '1';
		if (num & 0x4) bits[numBits-(4*(size-i))+1] = '1';
		if (num & 0x2) bits[numBits-(4*(size-i))+2] = '1';
		if (num & 0x1) bits[numBits-(4*(size-i))+3] = '1';
	}
	
	return bits;
}

/*
	int readParameters: Reads the parameters that the program started with and then store them
				in a set of variables given as arguments to the function.
				
				Makes sure that the parameters have the correct syntax: returns 0 if the
				syntax is correct, 1 if the syntax is incorrect.		
*/
int readParameters(int argc, char** argv, int *mArg, int *sArg, int *eArg, int *bArg, char **iArg, char **rArg) {
	
	int opt, invalid = 0, ret = 0;
	
	// Read all of the parameters
	while(-1 != (opt = getopt(argc, argv, "m:s:e:b:i:r:"))) {
		
		// Store each parameter value in a variable
		switch(opt) {
			case 'm':
				*mArg = atoi(optarg);
			break;
			case 's':
				*sArg = atoi(optarg);
			break;
			case 'e':
				*eArg = atoi(optarg);
			break;
			case 'b':
				*bArg = atoi(optarg);
			break;
			case 'i':
				*iArg = optarg;
			break;
			case 'r':
				*rArg = optarg;
			break;
			default:
				invalid = 1;
			break;
		}
	}
	
	// Make sure parameters have the proper syntax
	if (invalid == 0 && *mArg >= 0 && *sArg >= 0 && *eArg >= 0 && *bArg >= 0 && *iArg != NULL && *rArg != NULL)
		ret = 1;
		
	return ret;
}

/*
	hex* readAddressFile: Opens a file containing a list of hex addresses,
				reads those addresses and stores them into an array.
				
				Returns the array and the number of addresses that were added.			
*/
int readAddressFile(char *fileName, char ***addresses, int addressSize) {
	
	// Open the file
	FILE *file = fopen(fileName, "r");
	char buffer[addressSize+1];
	
	// Reset address number counter
	int numAddresses = 0;
	
	// Count the number of lines in the file
	while(fscanf(file,"%[^\n]\n", buffer)>0)
		numAddresses++;
		
	// Rewind back to the start of the file
	rewind(file);
	
	// Allocate the address array with numAddresses addresses
	*addresses = (char**)malloc(numAddresses * sizeof(char*)); 
	
	// Allocate each address in the array with addressSize characters
	int i;
	for(i=0; i<numAddresses; i++) 
		(*addresses)[i] = (char*)malloc((addressSize+1) * sizeof(char));
	
	// Store every address contained in the file
	i = 0;
	while(fscanf(file,"%[^\n]\n", (*addresses)[i])>0) 
		i++;
	
	// Close the file
	fclose(file);
	
	// Return the number of addresses in the array
	return numAddresses;
}

/*
	void freeCacheMemory: Frees the memory used by the array of addresses and
				the memory used by the arrays of lines in every set of
				the cache.
				
*/
void freeCacheMemory(char **addresses, int numAddresses, char ***cache, int numSets, int *numLines) {

	// Loop through the addresses and free each address string
	int i;
	for(i=0; i<numAddresses; i++)
		free(addresses[i]);
		
	// Free the array of addresses
	free(addresses);
	
	// Loop through the cache sets and free each line string
	int j;
	for(i=0; i<numSets; i++)
		for(j=0; j<numLines[i]; j++)
			free(cache[i][j]);
	
	// Loop through the cache sets and free each array of lines
	for(i=0; i<numSets; i++)
		free(cache[i]);
		
	// Free the array of cache sets
	free(cache);
}

/*
	void printCache: Debug function for printing the contents of the cache.	
*/
void printCache(char ***cache, int numSets, int *numLines) {
	
	// Loop through the cache sets
	int i;
	for(i = 0;i < numSets;i++) {
		
		// Print the current set only if it's not empty
		if (numLines[i] != 0) {
			
			// Print the set number
			printf("Set %d: ", i);
		
			// Loop through every line in the set
			int j;
			for(j=0; j<numLines[i]; j++)
				printf("%d ", binToDec(cache[i][j])); // Print the contents of the current line
			printf("\n");
		}
	}
}

/*
	char* isolateTag: Takes a binary string of an address and then
				isolates the tag portion. Returns the binary tag string.
*/
char* isolateTag(char *address, int size) {
	
	// Allocate a new tag string
	char *tag = (char*)malloc((size+1) * sizeof(char));
	
	// Copy the tag portion of the address into the new string
	int i;
	for(i=0; i<size; i++)
		tag[i] = address[i];
		
	// End the tag string
	tag[size] = '\0';
	
	// Return the tag string
	return tag;
}

/*
	char* isolateSet: Takes a binary string of an address and then
				isolates the set portion. Returns the binary set string.
*/
char* isolateSet(char *address, int offset, int size) {
	
	// Allocate a new set string
	char *set = (char*)malloc((size+1) * sizeof(char));
	
	// Copy the set portion of the address into the new string
	int i;
	for(i=offset; i<offset+size; i++)
		set[i-offset] = address[i];
		
	// End the set string
	set[size] = '\0';
	
	return set;
}

/*
	int searchAddress: Searches for a tag and set combination in the array of addresses
					after a given offset, then returns the index of that address.
*/
int searchAddress(char **addresses, int numAddresses, char *searchTag, char *searchSet, int offset, int addressBits, int setBits, int blockBits) {
	
	// Default index returned is max integer value
	int index = 0x7FFFFFFF;
	
	// Loop through the address array after the offset
	int i;
	for(i=offset; i<numAddresses; i++) {

		// Convert the hex address to binary
		char *address = hexToBin(addresses[i], addressBits);

		// Isolate the tag bits from the address
		char *currentTag = isolateTag(address, addressBits-setBits-blockBits);

		// Isolate the set bits from the address
		char *currentSet = isolateSet(address, addressBits-setBits-blockBits, setBits);

		// Check if the target tag and set combination is found
		if (strcmp(currentTag, searchTag) == 0 && strcmp(currentSet, searchSet) == 0) {
			
			// Stop searching
			index = i;
			i = numAddresses;
		}
		
		// Free the bit strings
		free(address);
		free(currentTag);
		free(currentSet);
	}
	
	return index;
}

/*
	int main: Reads the parameters given to the program, reads a list of addresses in a
				file given by one of the parameters, sets up a cache with a size given
				by the parameters, and simulates access to each address in the file by
				using the cache.
				
				Calculates the number of hits, misses, and the miss rate. Also determines
				the number of clock cycles that the chosen page replacement algorithm takes.
				
				Required starting parameters: -m -s -e -b -i -r
*/
int main(int argc, char** argv) {
	
	int mArg = -1; // Size of address (bits)
	int sArg = -1; // Number of index bits (2 ^ s = number of sets)
	int eArg = -1; // Number of line bits (2 ^ e = number of lines per set)
	int bArg = -1; // Size of block bits (2 ^ b = size of each block)
	char *iArg; // File of addresses
	char *rArg; // Replacement algorithm
    
	int hits = 0, misses = 0, missRate = 0, cycles = 0;
	
	// Temporary index variables for loops
	int i,j,k,l;
	
	// Read and validate the parameters
	if (readParameters(argc, argv, &mArg, &sArg, &eArg, &bArg, &iArg, &rArg)) {
		
		int numSets = 1 << sArg; // Number of sets in the cache
		int numLines = 1 << eArg; // Number of lines in each set
		int numBits = mArg-sArg-bArg; // Number of bits used for the tag
		
		// Read the address file
		int numAddresses = 0;
		char **addresses;
		numAddresses = readAddressFile(iArg, &addresses, mArg/4);
		
		// Allocate the cache with 2^sArg sets
		char ***cache = (char***)malloc(numSets * sizeof(char**)); 
		
		// Allocate each set with 2^eArg lines
		for(i=0; i<numSets; i++) 
			cache[i] = (char**)malloc(numLines * sizeof(char*));
        	
		// Loop through every line in the cache
		for(i=0; i<numSets; i++) {
		for(j=0; j<numLines; j++) {
    			
				// Allocate each line string with mArg-sArg-bArg+1 characters
				cache[i][j] = (char*)malloc((numBits+1) * sizeof(char));
				
				// Fill each line string with '0' characters
				for(k=0; k<numBits; k++)
					cache[i][j][k] = '0';
				
				// Add endline to each line string
				cache[i][j][numBits] = '\0';
			}
		}
        	
		// Initialize the number of lines filled for each set to 0
		int cacheLines[numSets];
		for(i=0; i<numSets; i++)
			cacheLines[i] = 0;
        
		// Simulate the cache
	    for(i=0; i<numAddresses; i++) {
	    	
	    	// Get an address value from the list of addresses
	    	char *address = addresses[i];
	    	
	    	// Convert the hex address to binary
	    	char *addressBits = hexToBin(address, mArg);
	    	
	    	// Isolate the tag bits from the address
	        char *tagBits = isolateTag(addressBits, numBits);
	        
	        // Isolate the set bits from the address
	        char *setBits = isolateSet(addressBits, numBits, sArg);
	        
	        // Get the set index from the set bit string
	        int set = binToDec(setBits);
	        
	        //printf("A: %s, Ab: %s, Tb: %s, Sb: %s, S: %d\n", address, addressBits, tagBits, setBits, set); // DEBUG
	        
	        // Check if the set is empty
	        if (cacheLines[set] == 0) {
	        	
	        	// Miss every time set is empty
	        	misses++;
	        	printf("%s M\n", address);
	        	
	        	// Add the first tag to the set
	        	strcpy(cache[set][0], tagBits);
	        	cacheLines[set] ++;
	        	
	        } else {
	        	
	        	int found = 0; // Flag for whether or not the tag is found
	        	
	        	// Loop through the set to look for the tag
	        	for(j=0; j<cacheLines[set]; j++) {
	        		
	        		// Check the tag of the current line
					if (strcmp(cache[set][j], tagBits) == 0) {
						
						// Hit when the tag is found
						hits++;
			        	printf("%s H\n", address);
			        	
			        	// Check if set is full and using lru algorithm
			        	if (cacheLines[set] == numLines && (strcmp(rArg, "lru") == 0 || strcmp(rArg, "LRU") == 0 || strcmp(rArg, "Lru") == 0)) {
			        		
			        		// Shift the set to the left, discarding the found tag
			        		for(k=j+1; k<numLines; k++)
			        			strcpy(cache[set][k-1], cache[set][k]);
			        			
			        		// Put the tag at the end of the set
			        		strcpy(cache[set][numLines-1], tagBits);
			        	}
			        	
			        	// Exit the for loop
			        	found = 1;
			        	break;
					}
				}
				
				if (!found) {
					
					// Miss when the tag isn't found in the set
					misses++;
		        	printf("%s M\n", address);
		        	
		        	// Check if set is full
		        	if (cacheLines[set] == numLines) {
		        		
						// Shift the set to the left, discarding the first tag
		        		for(k=1; k<numLines; k++)
		        			strcpy(cache[set][k-1], cache[set][k]);
		        			
		        		// Put the tag at the end of the set
		        		strcpy(cache[set][numLines-1], tagBits);
		        		
					} else {
						
						// Add new tag to the end of the set
						strcpy(cache[set][cacheLines[set]], tagBits);
			        	cacheLines[set] ++;
					}
				}
				
				// Sort the set based on how soon each tag will come up in the list of addresses
				if (strcmp(rArg, "optimal") == 0 || strcmp(rArg, "Optimal") == 0 || strcmp(rArg, "OPTIMAL") == 0) {
					
					// Keep track of how many lines have been sorted
					int sorted = 1;
					
					// Loop through the unordered lines
					for(j=1; j<cacheLines[set]; j++) {
						
						// Reset the found flag
						found = 0;
						
						// Get how far away the tag of the current unordered line appears again
						int unorderedTime = searchAddress(addresses, numAddresses, cache[set][j], setBits, i+1, mArg, sArg, bArg);
						
						// Loop through the ordered lines
						for(k=0; k<sorted; k++) {
							
							// Get how far away the tag of the current ordered line appears again
							int orderedTime = searchAddress(addresses, numAddresses, cache[set][k], setBits, i+1, mArg, sArg, bArg);
							
							// Compare unordered line and current ordered line
							if (unorderedTime > orderedTime) {
								
								// Save the unordered line to a temporary variable
								char tmp[numBits];
								strcpy(tmp, cache[set][j]);
								
								// Shift the ordered lines to the right, overwriting the current unordered line
								for(l=j-1; l>=0; l--)
									strcpy(cache[set][l+1], cache[set][l]);
									
								// Put unordered line infront of current ordered line
								strcpy(cache[set][k], tmp);
								
								// Set the found flag
								found = 1;
								
								// Increment the number of ordered lines
								sorted++;
								
								// Stop the loop through the ordered lines
								break;
							}
						}
						
						// Check if a place hasn't been found for the unordered line
						if (!found)
							sorted++; // Increase the number of ordered lines to add the unordered line to the end
					}
				}
			}
			
			// Free the binary strings from the hexToBin and isolate functions
			free(addressBits);
			free(tagBits);
			free(setBits);
			
			//printCache(cache, numSets, cacheLines); // DEBUG
	    }
	    
	    // Calculate the miss rate
	    missRate = (100*misses/(hits+misses));
	    
	    // Calculate the number of cycles
	    cycles = (hits + (101*misses));
	    
	    // Print the results
	    printf("[result] hits: %d misses: %d miss rate: %d%% total running time: %d cycle\n", hits, misses, missRate, cycles);
	    
	    // Free memory used by the cache and address list
		freeCacheMemory(addresses, numAddresses, cache, numSets, cacheLines);
		
	} else {
		
		// Print an error
		printf("[Error] Please enter the following syntax: ./cachelab -m 4 -s 2 -e 0 -b 1 -i address01 -r rlu\n");
	}
	
	return 0;
}

