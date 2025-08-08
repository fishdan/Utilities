/**
 * File: chat.c
 * Author: Darsono Sutedja
 * Date: January 24, 1997
 *
 * This file is used to execute the Java Chat.
 * It will look for "class.env" in the Jchat directory, and will use
 * it as the reference to user's java classpath.
 * It will then execute the chat program with the following command:
 *       javaw -classpath [classpath] Welcome
 * (If javaw fails, then it will try java.  If fails again, then print error
 * message.)
 * "Welcome" is the main program.
 *
 * e-mail: dsutedja@cs.umb.edu
 *
 * Copyright (c) 1998 Darsono Sutedja
 */
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define BUFFER 2048

typedef char * string;

void main() {
   FILE *fp;
   char classpath[BUFFER];
   string cmd2, cmd3;
	printf("Executing java chat...\n");
   /**
    * Look for classpath environment file.
    * This file should be generated during installation
    * (or you can type them by hand...but it's gonna be a long file...
    * and shouldn't be done by human).
    * NOTE: To get the classpath environment, you can use:
    * System.getProperty("java.class.path"), and it will produce a
    * string of the env.
    **/
   if ((fp=fopen("class.env", "r")) == NULL) {
   	fprintf(stderr, "file class.env not found, please reinstall software\n");
   	exit(0);
	} else {
   	fgets(classpath, BUFFER, fp);
      fclose(fp);
   }
   cmd2 = (char *)classpath;
   cmd3 = " Welcome";
   strcat(cmd2, cmd3);
	/**
    * javaw.exe will run the process in background.  So in this case,
    * everytime, user double-click on the program icon, it will be run
    * in background with no console-window at all.
    **/
   if (system(strcat("javaw.exe -classpath ",cmd2))==-1) {
   	if (system(strcat("java.exe -classpath ",cmd2))==-1) {
         fprintf(stderr, "either javaw.exe or java.exe is not found.\n");
      }
	}
}
