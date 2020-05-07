#include <stdio.h>
#define MAXLINE 1000
#define TRUE 1
#define FALSE 0
#define ESC 27
#define U_ARRO 72
#define L_ARRO 75
#define R_ARRO 77
#define D_ARRO 80
#define INSERT 82
#define DELETE 83
#define BACKSPACE 8
#define PGUP 73
#define PGDN 81

typedef int Boolean;
Boolean insert=1;
int firstline=1;

typedef struct{
	int length;
	char *data;
}linetable;

char c;
char filename[40]="\0";

linetable lt[MAXLINE];

int min(int a,int b){
	if(a<b)return a;
	return b;
}

void showline(int y)
{
	int i;

	for(i=1;i<=lt[y].length;i++){
		if(i<=76)gotoxy(i,y-firstline+1);
		if(i<=76)cprintf("%c",lt[y].data[i]);
	}

}

void repaint(int linenum)
{
	int i;

	window(1,1,80,25);
	clrscr();
	gotoxy(1,25);
	printf("%s",filename);
	gotoxy(20,25);cprintf("ESC=Exit  Ctrl+S=Save  Ctrl+L=Load");


	if(insert){
		gotoxy(70,25);
		printf("Insert");
		gotoxy(1,1);
	}
	for(i=firstline;i<=min(linenum,firstline+23);i++){
		showline(i);
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
	repaint(linenum);

	c=getch();

	while(1){

		if(c==ESC){
			gotoxy(1,25);
			printf("Are you sure to close the LPEDIT?(y/n)                         ");
			if(getch()=='y')exit(0);
			repaint(linenum);
		}
		if(c==0){
			c=getch();
			switch(c){
			case U_ARRO:
				if(y!=1)
				  y--;
				if(x>lt[y].length+1)
				  x=lt[y].length+1;

				gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				break;
			case D_ARRO:
				if(y!=linenum)y++;
				if(x>lt[y].length+1)x=lt[y].length+1;
				gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				break;
			case L_ARRO:
				if(x==1){
					if(y!=1){
						y--;
						x=lt[y].length+1;
					}
				}
				else{
					x--;
				}
				gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				break;
			case R_ARRO:
				if(x>lt[y].length){
					if(y<linenum){
						y++;
						x=1;
					}
				}
				else{
					x++;
				}
				gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);

				break;
			case PGUP:
				if(firstline>24){y-=24;firstline-=24;}
				repaint(linenum);

				break;

			case PGDN:
				if(y+24<=linenum){y+=24;firstline+=24;}
				repaint(linenum);
				break;

			case INSERT:
				insert=(insert+1)%2;
				repaint(linenum);
				break;

			case DELETE:
				if(x<=lt[y].length){
					for(i=x;i<=lt[y].length-1;i++){
						lt[y].data[i]=lt[y].data[i+1];
					}
					lt[y].length--;
					repaint(linenum);
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
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
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);

					repaint(linenum);

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
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				}
				else{
					if(insert)lt[y].length++;
					if((!insert)&&x>lt[y].length)lt[y].length++;

					lt[y].data=(char*)realloc(lt[y].data,lt[y].length+1);
					if(insert)
						for(i=lt[y].length;i>x;i--){
							lt[y].data[i]=lt[y].data[i-1];
						}
					lt[y].data[x]=c;
					x++;
					if(x>lt[y].length)lt[y].length=x-1;
					showline(y);
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				}

			}
			if(c==BACKSPACE){
				if(x!=1){
					for(i=x;i<=lt[y].length;i++){
						lt[y].data[i-1]=lt[y].data[i];
					}
					lt[y].length--;
					x--;
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				}
				repaint(linenum);
                                gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
			}
			if(c==25){/*Ctrl+Y*/
				if(linenum==1){
					lt[1].length=0;
					linenum=1;x=1;
					free(lt[1].data);
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
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
                                        gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
				}


				repaint(linenum);
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
				repaint(linenum);


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
				repaint(linenum);
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
				repaint(linenum);

				y++;x=1;
                                gotoxy(1,25);
				if(x<10&&y<10)
				   printf("0%d/0%d",x,y);
				if(x>=10&&y<10)
				   printf("%d/0%d",x,y);
				if(x<10&&y>=10)
				   printf("0%d/%d",x,y);
				if(x>=10&&y>=10)
				   printf("%d/%d",x,y);
			}


		}

		if(y>firstline+23){firstline++;repaint(linenum);}
		if(y<firstline){firstline--;repaint(linenum);}
		gotoxy(x,y-firstline+1);


		c=getch();
	}
}


