/*** Definition section ***/

%{
/* C code to be copied verbatim */
#include <stdio.h>
%}

digit         [0-9]
letter        [a-zA-Z]

/* This tells flex to read only one input file */
%option noyywrap

%%
    /*** Rules section ***/
  
("+"|"-"|"*"|"/"|"%")     {  printf("%s \t ARITHMETIC OPERATOR \n", yytext);	}
("("|")"|"{"|"}"|"["|"]")    {  printf("%s \t PARENTHESES \n", yytext);	}
(";"|","|".")     {  printf("%s \t PUNCTUATION \n", yytext);	}
"="     {  printf("%s \t ASSIGNMENT \n", yytext);	}
("=="|"<"|">"|"<="|">="|"!=")    {  printf("%s \t RELATIONAL OPERATOR \n", yytext);	}
("||"|"&&"|"!"|"^"|"&"|"|")      {  printf("%s \t LOGICAL OPERATOR \n", yytext);	}
"public"	{  printf("%s \t RESERVED \n", yytext);	}
"static"    {  printf("%s \t RESERVED \n", yytext);	}
"void"      {  printf("%s \t RESERVED \n", yytext);	}
"main"      {  printf("%s \t RESERVED \n", yytext);	}
"do"        {  printf("%s \t RESERVED \n", yytext);	}
"while"     {  printf("%s \t RESERVED \n", yytext);	}
"if"        {  printf("%s \t RESERVED \n", yytext);	}
"class"     {  printf("%s \t RESERVED \n", yytext);	}
"int"       {  printf("%s \t RESERVED \n", yytext);	}
"String"    {  printf("%s \t RESERVED \n", yytext);	}
"float"     {  printf("%s \t RESERVED \n", yytext);	}
"System.out.println" {  printf("%s \t RESERVED \n", yytext);	}

{letter}({letter}|{digit})* {  printf("%s \t IDENTIFIER \n", yytext);	}
                       
{digit}+    {  printf("%s \t DIGIT \n", yytext);	}

[ \t\n\r]            /* skip whitespace */

.                    { printf("Unknown character [%c]\n",yytext[0]); }

.|\n    {   /* Ignore all other characters. */   }

%%
/*** C Code section ***/

int main(void)
{
    /* Call the lexer, then quit. */
    yylex();
    return 0;
}
