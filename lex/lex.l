letter[a-zA-Z]  
digit[0-9]
%% 
"if"|"else"|"int"|"char"|"scanf"|"printf"|"switch"|"return"|"struct"|"do"|"while"|"void"|"for"|"float" printf("\n%s\t is keyword", yytext);  
["\a"|"\\n"|"\\b"|"\t"|"\\t"|"\b"|"\\a"|"\n"|" "]+ printf("\n%s\tis Escape sequence", yytext);  
{letter}({letter}|{digit}|"_")* printf("\n%s\tis identifier", yytext);  
"&&"|"<"|">"|"<="|">="|"="|"+"|"-"|"?"|"*"|"/"|"%"|"&"|"||" printf("\n%s\tis an operator", yytext);  
"#"|"'"|"."|"\""|"\\"|";"|"," printf("\n%s\t is a special character", yytext);  
"%d"|"%s"|"%c"|"%f"|"%e" printf("\n%s\tis a format specifier", yytext);  
"{"|"}"|"["|"]"|"("|")" printf("\n%s\t is a parenthesis", yytext);  
%%
main(){yylex();}

