#
# Assignment: Project 5
# Author: Vitaliy Shydlonok
# Date: 12/6/2019
#

import os
import sys
import subprocess
from string import Formatter

project_dir = "csc344"

if len(sys.argv) > 1:
    email = sys.argv[1]

if len(sys.argv) > 2:
    project_dir = sys.argv[2]


def process_all(fname):
    # Create an index html file
    index_message = "<html><head></head><body style=\"text-align: center\"><h1>CSC344 Fall 2019</h1><h2>Vitaliy Shydlonok</h2><hr><h3>Assignments:</h3><hr>"
    index_file = open(project_dir+"/index.html","w")

    # Process all sub-folders in the given directory
    for x in subprocess.check_output(["ls", fname]).split():
        if os.path.isdir(fname+"/"+x):
            index_message = index_message+process_project(fname+"/"+x)

    # Finish the index html file
    index_message = index_message+"<hr></body></html>"
    index_file.write(index_message)
    index_file.close()

    # Create a tar.gz file
    subprocess.check_output(["tar", "-czvf", fname+".tar.gz", fname])

    # Email the tar.gz file to the given email address
    ps = subprocess.Popen(("echo", "Hello, this is my final project for CSC344. \n\n-Vitaliy Shydlonok"), stdout=subprocess.PIPE)
    output = subprocess.check_output(("mutt", email, "-s", "CSC344 Final Project", "-a", fname+".tar.gz"), stdin=ps.stdout)
    ps.wait()
    print(output)

    print("Successfully summarized and emailed the project directory "+fname+" to "+email+"!\n")

def process_project(fname):
    # Create a summary html file
    summary_file = open(project_dir+"/summary_"+os.path.basename(fname)+".html","w")
    message = "<html><head></head><body style=\"text-align: center\"><h1>CSC344 Fall 2019</h1><h2>Vitaliy Shydlonok</h2><hr><h3>Assignment "+os.path.basename(fname)+" files:</h3>"

    # Process every source file within the given sub-folder
    for x in subprocess.check_output(["ls", fname]).split():
        message = message+"<hr><p><b>File Name:</b> <a href=\""+os.path.basename(fname)+"/"+x+"\">"+x+"</a><p><b>Line Count:</b> "+subprocess.check_output(["wc", "-l", fname+"/"+x]).split()[0]+" lines"
        message = message+process_source(fname+"/"+x)

    # Finish the summary html file
    message = message+"<hr></body></html>"
    summary_file.write(message)
    summary_file.close()

    # Return a link to be added to the index html file
    return "<p><a href=\"summary_"+os.path.basename(fname)+".html\">"+os.path.basename(fname)+"</a>"

def process_source(fname):
    # Read the source file to process
    file = open(fname, "r")
    source = file.read()
    file.close()
    message = "<p><b>Identifiers:</b><p>| "

    # Clean and split the source text based on the language used
    _, lang = os.path.splitext(fname)
    cleaned_source = clean_string(source, lang)

    # Add all identifiers to the html page
    for x in cleaned_source:
        message = message+x+" | "

    return message

def clean_string(initial_string, lang):
    list_string = list(initial_string)

    # Remove strings and comments
    instring = 0
    comment = 0
    for i, c in enumerate(list_string):
        if (instring == 0):
            if (comment == 0 and c == '/' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment = 1
            elif (comment == 1 and c == '/' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment=2
            elif (comment == 1 and c == '*' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment=3
            elif (comment == 0 and instring == 0 and c == '#' and lang == ".c"): # Remove includes in c
                list_string[i] = ' '
                comment=2
            elif (comment == 2 and c == '\n' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment=0
            elif (comment == 3 and c == '*' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment=4
            elif (comment == 4 and c == '/' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment=0
            elif (comment == 4 and c != '*' and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
                comment = 3
            elif ((comment == 2 or comment == 3 or comment == 4) and (lang == ".c" or lang == ".scala")):
                list_string[i] = ' '
            elif (comment == 0 and c == ';' and lang == ".clj"):
                list_string[i] = ' '
                comment = 1
            elif (comment == 0 and c == '%' and lang == ".pl"):
                list_string[i] = ' '
                comment = 1
            elif (comment == 0 and c == '#' and lang == ".py"):
                list_string[i] = ' '
                comment = 1
            elif (comment == 1 and c == '\n' and (lang == ".clj" or lang == ".pl" or lang == ".py")):
                list_string[i] = ' '
                comment=0
            elif (comment == 1 and (lang == ".clj" or lang == ".pl" or lang == ".py")):
                list_string[i] = ' '

            if (comment == 0 and c == "\""): # Remove double quotes
                list_string[i] = ' '
                instring = 1
            elif (comment == 0 and c == "'" and lang != ".clj"): # Remove single quotes in all but Clojure
                list_string[i] = ' '
                instring = 3
        else:
            if (instring == 1 and c == "\\"):
                list_string[i] = ' '
                instring = 2
            elif (instring == 2 and c == "\\"):
                list_string[i] = ' '
                instring = 1
            elif (instring == 2):
                list_string[i] = ' '
                instring = 1
            elif (instring == 1 and c == "\""):
                list_string[i] = ' '
                instring = 0
            elif (instring == 3 and c == "\\"):
                list_string[i] = ' '
                instring = 4
            elif (instring == 4 and c == "\\"):
                list_string[i] = ' '
                instring = 3
            elif (instring == 4):
                list_string[i] = ' '
                instring = 3
            elif (instring == 3 and c == "'"):
                list_string[i] = ' '
                instring = 0
            elif (instring == 1 or instring == 3):
                list_string[i] = ' '

    working_string = "".join(list_string)

    # Remove special characters
    format_list = ["!","@","#","$","%","^","&","*","(",")","+","=","{","}","[","]","|","\\",":",";","\'","<",">",",",".","?","/","~","`"]
    for x in format_list:
        working_string = working_string.replace(x, "\n")

    # Don't remove '-' from Clojure
    if (lang != ".clj"):
        working_string = working_string.replace("-", " ")

    # Remove numbers and invalid names
    working_list = [x for x in working_string.split() if "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_".find(x[0]) > -1]

    # Remove duplicates
    working_list = list(set(working_list))

    # Sort by alphabetical order
    working_list.sort()

    return working_list

process_all(project_dir)
