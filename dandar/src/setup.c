#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

void main() {
	printf("Preparing setup...\n");
   printf("Executing Java(tm)\n\n");
   if (system("javaw.exe Install")==-1) {
   	if (system("java.exe Install")==-1)
      	perror("can't find java.exe");
	}
}