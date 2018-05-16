%{
#include<stdio.h>
%}
%%
boolean|int|float|class|public|main|static|void|for|if|args|String|char|do|double|else|extends|final|import|interface|new|private|super|this|try|System|out|println printf("Keyword  ");
[-,+]?[0-9]+ printf("Constants  ");
[,.'"]+ printf("Punctuation Chars  ");
[!@#$^&*()]+ printf("Special Chars  ");
[a-zA-Z]+ printf("Identifiers  ");
[+,-,*,/,%]+ printf("Arithmetic Operators  ");
[==,!=,>,<,>=,<=,]+ printf("Relational Operators  ");
[}|)]+ printf("\nRight Parenthesis - %s",yytext);
%%
main(){yylex();}
