#include<stdio.h>
#include<ctype.h>
#include<string.h>
int n,m=0,p,i=0,j=0,b=0;
char a[10][10],followResult[10],firstResult[10];
void follow(char c);
void first(char c);
void firstf(char c);
void addToResult(char);
int main()
{
 int i;
 int choice;
 char c,ch;
 printf("Enter the no.of productions: ");
scanf("%d", &n);
 printf(" Enter %d productions\nProduction with multiple terms should be give as separate productions \n", n);
 for(i=0;i<n;i++)
  scanf("%s%c",a[i],&ch);
    do
 {
  m=0,b=0;
  printf("Find FIRST & FOLLOW of -->");
  scanf(" %c",&c);
  follow(c);
  printf("FOLLOW(%c) = { ",c);
  for(i=0;i<m;i++)
   printf("%c ",followResult[i]);
  printf(" }\n");
  firstf(c);
  printf("FIRST(%c) = { ",c);
  for(i=0;i<b;i++)
   printf("%c ",firstResult[i]);
  printf(" }\n");
  printf("Do you want to continue(Press 1 to continue....)?");
 scanf("%d",&choice);
 }
 while(choice==1);
}
void follow(char c)
{
    if(a[0][0]==c)addToResult('$');
 for(i=0;i<n;i++)
 {
  for(j=2;j<strlen(a[i]);j++)
  {
   if(a[i][j]==c)
   {
    if(a[i][j+1]!='\0')first(a[i][j+1]);
    if(a[i][j+1]=='\0'&&c!=a[i][0])
     follow(a[i][0]);
   }
  }
 }
}
void firstf(char c)
{
      int k;
                 if(!(isupper(c)))
                     firstResult[b++]=c;
                 for(k=0;k<n;k++)
                 {
                 if(a[k][0]==c)
                 {
                 if(a[k][2]=='#') firstResult[b++]='#';
                 else if(islower(a[k][2]))
                     firstResult[b++]=a[k][2];
                 else firstf(a[k][2]);
                 }
                 }
}
void first(char c)
{
      int k;
                 if(!(isupper(c)))
                     addToResult(c);
                 for(k=0;k<n;k++)
                 {
                 if(a[k][0]==c)
                 {
                 if(a[k][2]=='#') follow(a[i][0]);
                 else if(islower(a[k][2]))
                     addToResult(a[k][2]);
                 else first(a[k][2]);
                 }
                 }
}
void  addToResult(char c)
{
    int i;
    for( i=0;i<=m;i++)
        if(followResult[i]==c)
            return;
   followResult[m++]=c;
} 

