#
# Assignment: Microproject 5
# Author: Vitaliy Shydlonok
# Date: 11/30/2019
#

import os
import sys
import subprocess

def count_lines(fname):

    # Loop through all files/folders within the current directory
    for x in subprocess.check_output(["ls", fname]).split():

        # Go into directories recursively and print the number of lines in each file
        if os.path.isdir(fname+"/"+x):
            count_lines(fname+"/"+x)
        else:
            print(fname+"/"+x+" has "+subprocess.check_output(["wc", "-l", fname+"/"+x]).split()[0]+" lines.")
        
# Count the lines of all files within a directory given by the first argument
if len(sys.argv) > 1:
    count_lines(sys.argv[1])
