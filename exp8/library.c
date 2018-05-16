hello.c

#include <stdio.h>
#include "mylib.h"

void
hello (const char * name)
{
	printf ("Hello, %s!\n", name);
}

bye.c

#include<stdio.h>
#include"mylib.h"

void
bye(void){
	printf("Bye!!");
}

mylib.h

void hello (const char * name);
void bye(void);

test.c

#include "mylib.h"

void main(){
	hello("Yash");
	bye();
}
