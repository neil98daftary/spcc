#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#define MOP_TABLE_LENGTH 10   //store number of elements in mop table
#define POP_TABLE_LENGTH 7    //store number of elements in pop table

struct MOP_TABLE{
    char inst[10];
    int length;
}mop[] = {
            {"LA",4},
            {"SR",2},
            {"L",4},
            {"AR",2},
            {"A",4},
            {"ST",4},
            {"C",4},
            {"BNE",4},
            {"LR",2},
            {"BR",2}            
        };

struct POP_TABLE{
    char inst[10];
    int length;
}pop[] = {
            {"START",1},
            {"USING",1},
            {"EQU",1},
            {"DS",4},
            {"DC",4},
            {"END",1},
            {"LTORG",1}
        };

struct SYM_TABLE{
    char symbol[20];
    int value;
    int length;
    char reloc;
}sym[100];

struct LIT_TABLE{
    char symbol[20];
    int value;
    int length;
    char reloc;
}lit[100];

static int lc = 0;  //store the current location of lc
static int SYM_TABLE_LENGTH = 0;  //store the number of symbol in symbol table
static int LIT_TABLE_LENGTH = 0;  //store the number of elements in literal table
char inst[50];  //For remebering the current inst MOP or POP inst
int newSymbol = 0;  //Flag for determining whether a symbol is created on that line
int reg[16] = {0};

void printString(char *str){
    printf("%s",str);
    printf(" %ld\n",strlen(str));
}

int isMOP(char *str){
    int i;

    for (i = 0; i < MOP_TABLE_LENGTH; ++i)
    {
        if(strcmp(str,mop[i].inst) == 0){
            strcpy(inst,str);
            return i;
        }
    }

    return -1;
}

int isPOP(char *str){
    int i;

    for (i = 0; i < POP_TABLE_LENGTH; ++i)
    {
        if(strcmp(str,pop[i].inst) == 0){
            strcpy(inst,str);
            return i;
        }
    }

    return -1;
}

int isSymbol(char *str){
    int i;

    for (i = 0; i < SYM_TABLE_LENGTH; ++i)
    {
        if(strcmp(str,sym[i].symbol) == 0)
            return i;
    }

    return -1;
}

int isLiteral(char *str){
    int i;

    for (i = 0; i < LIT_TABLE_LENGTH; ++i)
    {
        if(strcmp(str,lit[i].symbol) == 0)
            return i;
    }

    return -1;
}


int generateSymbol(char *str){
    
    if(isPOP(str) > -1 || isMOP(str) > -1 || isSymbol(str) > -1)
        return 0;
    
    strcpy(sym[SYM_TABLE_LENGTH].symbol, str);
    newSymbol = 1;
    return 1;
}

void completeSymbolTable(char *str, char *value){
    int index = 0;

    if(strcmp(str,"EQU") == 0){
        if(strcmp(value,"*") == 0)
            sym[SYM_TABLE_LENGTH].value = lc;
        else
            sym[SYM_TABLE_LENGTH].value = atoi(value);
        
        sym[SYM_TABLE_LENGTH].reloc = 'A';
    }
    else{
        sym[SYM_TABLE_LENGTH].value = lc;
        sym[SYM_TABLE_LENGTH].reloc = 'R';
    }

    if((index = isPOP(str)) > -1){
        sym[SYM_TABLE_LENGTH].length = pop[index].length;
    }
    else if((index = isMOP(str)) > -1){
        sym[SYM_TABLE_LENGTH].length = mop[index].length;
    }
}

void printSymbolTable(){
    int i;

    printf("Symbol" "\t\t" "Value" "\t" "Length" "\t" "Relocation" "\n");
    for (i = 0; i < SYM_TABLE_LENGTH; ++i)
    {
        /*If just to make the table linear this is optional*/
        if(strlen(sym[i].symbol) < 8)
            printf("%s\t\t",sym[i].symbol);
        else
            printf("%s\t",sym[i].symbol);
        printf("%d\t",sym[i].value);
        printf("%d\t",sym[i].length);
        printf("%c\n",sym[i].reloc);
    }
}

void getLiteralSymbol(char *str){
    int i = 0;
    int count = strlen(str);
    for (i = 1; i < count; ++i)
    {
        str[i-1] = str[i];
    }
    str[count-1] = '\0';
    //printf("%s\n", str);
}

void assignValueToLiterals(){
    int i;

    if(lc%8 != 0){
        lc = ( lc/8 + 1)*8;
    }

    for (i = 0; i < LIT_TABLE_LENGTH; ++i)
    {
        lit[i].value = lc;
        lc = lc + lit[i].length;
    }
}

void printLiteralTable(){
    int i;
    printf("Symbol" "\t\t" "Value" "\t" "Length" "\t" "Relocation" "\n");
    for (i = 0; i < LIT_TABLE_LENGTH; ++i)
    {
        /*If just to make the table linear this is optional*/
        if(lit[i].symbol[0] == 'A')
            printf("%s\t",lit[i].symbol);
        else
            printf("%s\t\t",lit[i].symbol);
        printf("%d\t",lit[i].value);
        printf("%d\t",lit[i].length);
        printf("%c\n",lit[i].reloc);
    }
}

void printRegisterValues(){
    int i;
    printf("\n%s\n", "Register Values");
    for(i = 0;i<16;i++){
        printf("%d\t", reg[i]);
    }
    printf("\n");
}

void assignValueToRegister(char *value, char *regis){
    int index1 = -1;
    int index2 = -1;

    if(strcmp(value,"*") == 0)
        reg[atoi(regis)] = lc;
    else if((index1 = isSymbol(value)) > -1 && (index2 = isSymbol(regis)) > -1){
        reg[sym[index2].value] = sym[index1].value;
    }
    else if((index1 = isSymbol(value)) > -1 && (index2 = isSymbol(regis)) == -1){
        reg[atoi(regis)] = sym[index1].value;
    }
    else if((index1 = isSymbol(value)) == -1 && (index2 = isSymbol(regis)) > -1){
        reg[sym[index2].value] = atoi(value);
    }
    else{
        reg[atoi(regis)] = atoi(value);
    }
}

void printSecondContext(FILE *file,int indx, int value){
    //printf("Value = %d\n", value);
    int offset = 0, base = 15;
    int i;
    int min_base_diff = 9999;

    for(i=15;i>=0;i--){
        if(value - reg[i] > 0 && value - reg[i] < min_base_diff){
            base = i;
            min_base_diff = value - reg[i];
        }
    }

    offset = value - reg[base] - indx;

    fprintf(file,"%d(%d,%d)\n", offset,indx,base);
}

void main()
{
    FILE *file, *output; 
    char c,str[50],attr1[50],attr2[50];
    int t = 0, segment = 1;

    file = fopen("code.asm","r");
    
    /* Pass 1 */
    while(1)
    {
        c = fgetc(file);
        
        if(c == EOF)   //Exiting the code on EOF
        {
            printf("%s\n","Symbol Table");
            printSymbolTable();
            printf("\n\n");
            printf("%s\n","Literal Table");
            printLiteralTable();
            //exit(0);
            goto PASS2;
        }
        
        if(c == ' ' || c == '\t' || c == '\n')  //Splitting the word on space, tab or new line
        {
            str[t] = '\0';   //Ending the string
            
            if(strlen(str) > 0)
            {
                switch(segment){
                    case 1: if(generateSymbol(str))
                                segment = 2;
                            else
                                segment = 3;
                            break;
                    
                    case 2: if(isPOP(str) > -1 || isMOP(str) > -1){
                                strcpy(inst,str);
                            }
                            segment = 3;
                            break;
                    
                    case 3: if(c == '\n'){
                                //printf("%s\n", inst);
                                //printf("%s\n", str);
                                segment = 1;
                                if(newSymbol){
                                    if(strcmp(inst,"START") == 0)
                                        lc = atoi(str);
                                    completeSymbolTable(inst,str);
                                    SYM_TABLE_LENGTH++;
                                    newSymbol = 0;
                                }

                                int index = 0;
                                if ((index = isMOP(inst)) > -1)
                                {
                                    lc = lc + mop[index].length;
                                }

                                if(str[0] == '='){
                                    //This is literal and add it to the literal table
                                    getLiteralSymbol(str);
                                    strcpy(lit[LIT_TABLE_LENGTH].symbol,str);
                                    lit[LIT_TABLE_LENGTH].length = mop[index].length;
                                    lit[LIT_TABLE_LENGTH].reloc = 'R';
                                    LIT_TABLE_LENGTH++;
                                }

                                if(strcmp(inst,"DS") == 0){
                                    /*Hardcoding for float*/
                                    str[strlen(str)-1] = '\0';
                                    lc = lc + atoi(str)*4;
                                }

                                strcpy(inst,"\0");
                            }
                            break;
                }
            }

            if(c == '\n'){
                if(strcmp(inst,"LTORG") == 0){
                    //printf("%s\n", inst);
                    //printf("%s\n", str);
                    assignValueToLiterals();
                    segment = 1;
                }
                segment = 1;
            }

            strcpy(str,"\0");  //Replacing previous string with empty string
            t = 0;
            continue;
        }
        
        str[t++] = c;
    }
    
    PASS2:
    strcpy(str,"\0");
    fclose(file);
    
    file = fopen("code.asm","r");
    output = fopen("output.obj","w");
    segment = 1;
    lc = 0;
    int flag = 0;
    int index = 0;
    
    while(1)
    {
        c = fgetc(file);
        
        if(c == EOF)
        {
            printRegisterValues();
            exit(0);
        }
        
        if(c == ' ' || c == '\t' || c == '\n')  //Splitting the word on space, tab or new line
        {
            str[t] = '\0';   //Ending the string
            
            if(strlen(str) > 0)
            {   
                switch(segment){
                    case 1: if(isSymbol(str) > -1)
                                segment = 2;
                            else{
                                strcpy(inst,str);
                                segment = 3;
                            }
                            break;
                    
                    case 2: strcpy(inst,str);
                            segment = 3;
                            break;
                    
                    case 3: if(flag == 0){
                                strcpy(attr1,str);
                                flag = 1;
                            }
                            else if(strcmp(str,",") == 0){
                                //do nothing
                            }
                            else if(flag == 1 && strlen(str) > 0){
                                strcpy(attr2,str);
                                flag = 1;
                            }

                            if(c == '\n'){
                                if(strcmp(inst,"START") == 0){
                                    lc = atoi(attr1);
                                }
                                else if(strcmp(inst,"EQU") == 0){
                                    //do nothing in pass 2
                                }
                                else if(strcmp(inst,"USING") == 0){
                                    assignValueToRegister(attr1,attr2);
                                    //printRegisterValues();
                                }
                                else if((index = isMOP(inst)) > -1){
                                    //printf("%s\t", inst);
                                    //printf("%s\t%s\n", attr1, attr2);

                                    int index1 = -1;
                                    int index2 = -1;

                                    if((index1 = isSymbol(attr1)) > -1 && (index2 = isSymbol(attr2)) > -1){
                                        //Both attr1 and attr2 are symbol
                                        fprintf(output,"%s\t",inst);    //print the instruction
                                        fprintf(output,"%d , ",sym[index1].value); //print the first attr1 value and comma
                                        if(inst[strlen(inst)-1] == 'R')     //instruction of RR type
                                            fprintf(output,"%d",sym[index2].value);
                                        else
                                            printSecondContext(output,0,sym[index2].value);
                                        fprintf(output,"\n");
                                    }
                                    else if((index1 = isSymbol(attr1)) > -1 && (index2 = isSymbol(attr2)) == -1){
                                        //only attr1 is symbol
                                        fprintf(output,"%s\t",inst);    //print the instruction
                                        fprintf(output,"%d , ",sym[index1].value); //print the first attr1 value and comma
                                        if(inst[strlen(inst)-1] == 'R')     //instruction of RR type
                                            fprintf(output,"%d",atoi(attr2));
                                        else{
                                            if(attr2[0] == '='){
                                                //This is a literal
                                                getLiteralSymbol(attr2);
                                                index2 = isLiteral(attr2);
                                                printSecondContext(output,0,lit[index2].value);
                                            }
                                            else{
                                                //This is INDEX var
                                                int v = 0;
                                                char s[50],c;
                                                //Hardcoding only for INDEX
                                                if(strstr(attr2,"INDEX") != NULL){
                                                    while(attr2[v] != '('){
                                                        s[v] = attr2[v];
                                                        v++;
                                                    }
                                                    s[v] = '\0';
                                                    v = isSymbol("INDEX");
                                                    index2 = isSymbol(s);
                                                    printSecondContext(output,sym[v].value,sym[index2].value + sym[v].value);
                                                }
                                                else
                                                    printSecondContext(output,0,atoi(attr2));
                                            }
                                        }
                                        fprintf(output,"\n");
                                    }
                                    else if((index1 = isSymbol(attr1)) == -1 && (index2 = isSymbol(attr2)) > -1){
                                        //only attr2 is symbol
                                        fprintf(output,"%s\t",inst);    //print the instruction
                                        fprintf(output,"%d , ", atoi(attr1));
                                        if(inst[strlen(inst)-1] == 'R')     //instruction of RR type
                                            fprintf(output,"%d",sym[index2].value);
                                        else
                                            printSecondContext(output,0,sym[index2].value);
                                        fprintf(output,"\n");           
                                    }
                                    else{
                                        //attr1 and attr2 both are not symbol
                                        fprintf(output,"%s\t",inst);    //print the instruction
                                        fprintf(output,"%d , ", atoi(attr1));     //print the first instruction
                                        if(inst[strlen(inst)-1] == 'R')     //instruction of RR type
                                            fprintf(output,"%d",atoi(attr2));
                                        else
                                            printSecondContext(output,0,atoi(attr2));
                                        fprintf(output,"\n");
                                    }

                                    lc = lc + mop[index].length;
                                }

                                segment = 1;
                                flag = 0;
                                index = -1;
                                strcpy(attr1,"\0");
                                strcpy(attr2,"\0");
                            }
                            break;
                }
            }

            strcpy(str,"\0");  //Replacing previous string with empty string
            t = 0;
            continue;
        }
        
        str[t++] = c;
    }
}