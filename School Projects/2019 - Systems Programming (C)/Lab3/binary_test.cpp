#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
	int sArg = 4;
	int bArg = 4;
	int mArg = 12;
	int tArg = mArg-sArg-bArg;
	char address[] = "A72";
	char bits[mArg+1];
	
	// Start with all 0s
	int i;
	for(i=0; i<mArg; i++)
		bits[i] = '0';
	bits[mArg] = '\0';
	
	// Determine the length of address string
	int size = 0;
	while(address[size] != '\0')
		size++;
	
	// Convert the address string into a string of bits
	for(i=size-1; i>=0; i--) {
		int num;
		if (address[i]-'0' < 10) num = address[i] - '0';
		else num = 10 + (address[i] - 'A');
		
		if (num & 0x8) bits[mArg-(4*(size-i))] = '1';
		if (num & 0x4) bits[mArg-(4*(size-i))+1] = '1';
		if (num & 0x2) bits[mArg-(4*(size-i))+2] = '1';
		if (num & 0x1) bits[mArg-(4*(size-i))+3] = '1';
	}
	
	printf("Address Bits: %s\n", bits);
	
	// Isolate the tag bits
	char tagBits[tArg+1];
	for(i=0; i<tArg; i++)
		tagBits[i] = bits[i];
	tagBits[tArg] = '\0';
		
	// Isolate the set bits
	char setBits[sArg+1];
	for(i=tArg; i<tArg+sArg; i++)
		setBits[i-tArg] = bits[i];
	setBits[sArg] = '\0';
	
	printf("Tag Bits: %s\n", tagBits);
	printf("Set Bits: %s\n", setBits);
	
	
}
