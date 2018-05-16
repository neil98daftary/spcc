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


/*
gcc -Wall -c hello_fn.c
gcc -Wall -c bye_fn.c


ar cr libhello.a hello_fn.o bye_fn.o

ar t libhello.a
hello_fn.o
bye_fn.o

gcc -Wall main.c libhello.a -o hello
OR
gcc -Wall -L. main.c -lhello -o hello
*/