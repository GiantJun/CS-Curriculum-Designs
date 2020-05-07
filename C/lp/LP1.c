/*///////////////////////////////////////////////////////////
//
//			Programable Interface
//
//			Programer :  hyperion
//			Student ID:  99070108
//
//			Date:	   2001/1/11
//			Last Build:  1.00.1225b
//
///////////////////////////////////////////////////////////*/


#include <stdio.h>

#define MAXLINE 1000
#define TRUE 1
#define FALSE 0

/*Define Key Map*/
#define UP_Press 72
#define LF_Press 75
#define RH_Press 77
#define DN_Press 80
#define BAK_Press 8
#define INS_Press 82
#define DEL_Press 83
#define ESC_Press 27
#define PGUP_Press 73
#define PGDN_Press 81

int INS_STA=1;
int Headline=1;

typedef struct{
	int length;
	char *data;
}_Line;

char c;
char filename[40]="\0";

_Line lt[MAXLINE];

void Line_Content(int y);
void Refresh(int linenum);
void Show_Pos(int X,int Y);

void Show_Pos(int X,int Y)
{
        gotoxy(1,25);
	if(X<10&&Y<10)
	   printf("0%d/0%d",X,Y);
	if(X>=10&&Y<10)
	   printf("%d/0%d",X,Y);
	if(X<10&&Y>=10)
	   printf("0%d/%d",X,Y);
	if(X>=10&&Y>=10)
	   printf("%d/%d",X,Y);
}

int min(int a,int b){
	if(a<b)return a;
	return b;
}

void Line_Content(int y)
{
	int i;

	for(i=1;i<=lt[y].length;i++){
		if(i<=76)gotoxy(i,y-Headline+1);
		if(i<=76)cprintf("%c",lt[y].data[i]);
	}

}

void Refresh(int linenum)
{
	int i;

	window(1,1,80,25);
	clrscr();
	gotoxy(1,25);
	printf("%s",filename);
	gotoxy(20,25);cprintf("ESC=Exit  Ctrl+S=Save  Ctrl+L=Load");

	if(INS_STA){
		gotoxy(70,25);
		printf("Insert");
		gotoxy(1,1);
	}
	for(i=Headline;i<=min(linenum,Headline+23);i++){
		Line_Content(i);
	}
}

void main()
{
	int linenum=1,x=1,y=1,i,j;
	int ptr;
	FILE *fp;
	lt[1].length=0;
	clrscr();
	textbackground(1);
	textcolor(14);

	gotoxy(x,y);
	Refresh(linenum);

	c=getch();

	while(1){

		if(c==ESC_Press){
			gotoxy(1,25);
			printf("exit, ok? (y/n)                         ");
			if(getch()=='y')exit(0);
			Refresh(linenum);
		}
		if(c==0){
			c=getch();
			switch(c){
			case UP_Press:
				if(y!=1)
				  y--;
				if(x>lt[y].length+1)
				  x=lt[y].length+1;

				Show_Pos(x,y);
				break;
			case DN_Press:
				if(y!=linenum)y++;
				if(x>lt[y].length+1)x=lt[y].length+1;
				Show_Pos(x,y);
				break;
			case LF_Press:
				if(x==1){
					if(y!=1){
						y--;
						x=lt[y].length+1;
					}
				}
				else{
					x--;
				}
				Show_Pos(x,y);
				break;
			case RH_Press:
				if(x>lt[y].length){
					if(y<linenum){
						y++;
						x=1;
					}
				}
				else{
					x++;
				}
				Show_Pos(x,y);
				break;
			case PGUP_Press:
				if(Headline>24){y-=24;Headline-=24;}
				Refresh(linenum);

				break;

			case PGDN_Press:
				if(y+24<=linenum){y+=24;Headline+=24;}
				Refresh(linenum);
				break;

			case INS_Press:
				INS_STA=(INS_STA+1)%2;
				Refresh(linenum);
				Show_Pos(x,y);
				break;

			case DEL_Press:
				if(x<=lt[y].length){
					for(i=x;i<=lt[y].length-1;i++){
						lt[y].data[i]=lt[y].data[i+1];
					}
					lt[y].length--;
					Refresh(linenum);
					Show_Pos(x,y);
				}
				else if(y<linenum){
					ptr=x;
					lt[y].data=(char*)realloc(lt[y].data,lt[y].length+lt[y+1].length+1);

					for(i=1;i<=lt[y+1].length;i++){
						lt[y].data[ptr++]=lt[y+1].data[i];
					}
					lt[y].length+=lt[y+1].length;
					free(lt[y+1].data);
					for(i=y+1;i<linenum;i++){
						lt[i]=lt[i+1];

					}
					linenum--;
					Show_Pos(x,y);
					Refresh(linenum);

				}
				break;
			}
		}
		else{/*is alpha num*/
			if(c>=32&&c<127&&lt[y].length<=76){
				if(lt[y].length==0){
					lt[y].data=(char*)malloc(2);
					lt[y].data[1]=c;
					lt[y].length++;
					cprintf("%c",c);
					x++;
					Show_Pos(x,y);
				}
				else{
					if(INS_STA)lt[y].length++;
					if((!INS_STA)&&x>lt[y].length)lt[y].length++;

					lt[y].data=(char*)realloc(lt[y].data,lt[y].length+1);
					if(INS_STA)
						for(i=lt[y].length;i>x;i--){
							lt[y].data[i]=lt[y].data[i-1];
						}
					lt[y].data[x]=c;
					x++;
					if(x>lt[y].length)lt[y].length=x-1;
					Line_Content(y);
					Show_Pos(x,y);
				}

			}
			if(c==BAK_Press){
				if(x!=1){
					for(i=x;i<=lt[y].length;i++){
						lt[y].data[i-1]=lt[y].data[i];
					}
					lt[y].length--;
					x--;
					Show_Pos(x,y);
				}
				Refresh(linenum);
				Show_Pos(x,y);
			}
			if(c==25){/*Ctrl+Y*/
				if(linenum==1){
					lt[1].length=0;
					linenum=1;x=1;
					free(lt[1].data);
					Show_Pos(x,y);
				}
				else{
					lt[y].length=0;
					free(lt[y].data);
					for(i=y;i<=linenum-1;i++){
						lt[i]=lt[i+1];
					}
					linenum--;
					if(y>linenum)y=linenum;
					x=1;
					Show_Pos(x,y);
				}

				Refresh(linenum);
			}

			if(c==12){/*Ctrl+L*/
				window(3,1,79,25);
				gotoxy(1,25);
				printf("Please input filename:");
				scanf("%s",filename);
				if(!(fp=fopen(filename,"r")))exit(-1);
				for(i=1;i<=linenum;i++){
					free(lt[i].data);
					lt[i].length=0;
				}
				linenum=1;
				lt[1].data=(char*)malloc(1);

				while(!feof(fp)){
					char c;
					c=fgetc(fp);

					if(c!=10){
						lt[linenum].data=(char*)realloc(lt[linenum].data,lt[linenum].length+2);

						lt[linenum].data[lt[linenum].length+1]=c;
						lt[linenum].length++;
					}
					else{
						linenum++;
						lt[linenum].data=(char*)malloc(1);
					}

				}
				Refresh(linenum);

			}
			if(c==19){/*Ctrl+S*/
				window(3,1,80,25);
				gotoxy(1,25);
				printf("Please input filename:");
				scanf("%s",filename);
				if(!(fp=fopen(filename,"w")))exit(-1);
				for(i=1;i<=linenum;i++){
					for(j=1;j<=lt[i].length;j++){
						fputc(lt[i].data[j],fp);
					}
					fputc(10,fp);
				}
				Refresh(linenum);
			}

			if(c==13){
				linenum++;
				for(i=linenum;i>y+1;i--){
					lt[i]=lt[i-1];
				}
				ptr=1;
				lt[y+1].data=(char*)malloc(lt[y].length-x+2);

				for(i=x;i<=lt[y].length;i++){
					lt[y+1].data[ptr++]=lt[y].data[i];
				}
				lt[y+1].length=ptr-1;

				lt[y].length=x-1;
				Refresh(linenum);

				y++;x=1;
				Show_Pos(x,y);
			}

		}

		if(y>Headline+23){Headline++;Refresh(linenum);}
		if(y<Headline){Headline--;Refresh(linenum);}
		gotoxy(x,y-Headline+1);

		c=getch();
	}
}
