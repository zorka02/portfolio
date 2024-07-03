#define _POSIX_C_SOURCE 200201L
#include <stdio.h>
#include <stdlib.h>
#include <ncurses.h>
#include <time.h>

void randomize_meteors(int meteorites[LINES - 4][3]);

int main() {
srand(time(NULL));

printf("CHOOSE DIFFICULTY:\n");
printf("EASY : 1\n");
printf("MEDIUM : 2\n");
printf("HARD : 3\n\n");

int diff;
scanf("%d", &diff);

if (diff != 1 && diff != 2 && diff != 3)
{ printf("INVALID OUTPUT\n");
    return EXIT_FAILURE;}

initscr();
cbreak();
noecho();
keypad(stdscr, TRUE);
    // invisible cursor, visibility of cursor (0,1,2)
curs_set(FALSE);
    // getch() will be non-blocking
nodelay(stdscr, TRUE);

start_color();

refresh();

/*int x,y;
int ylower = (float)LINES/5;
int yupper = (float)(4*LINES)/5;
int xlower = COLS;
int xupper = 2*COLS;

struct timespec ts = {
    .tv_sec = 0,
    .tv_nsec = 0.1 * 1000000000L
};


while (1) {
 y = (rand() % (yupper - ylower + 1)) + ylower;
 int randx = (rand() % (xupper - xlower + 1)) + xlower;
 for (x = randx; x >= 0; x--){
    mvprintw(y, x, "o ");
    refresh();
    nanosleep(&ts, NULL);
 }
mvprintw(y, 0, " ");
refresh();
}
*/

init_pair(1, COLOR_CYAN, COLOR_BLACK);
init_pair(2, COLOR_YELLOW, COLOR_BLACK);
init_pair(3, COLOR_MAGENTA, COLOR_BLACK);
init_pair(4, COLOR_BLUE, COLOR_BLACK);


float para;
if (diff == 1) {para = 0.5;}
else if (diff == 2) {para = 0.27;}
else if (diff == 3) {para = 0.2;}

float speedpara = (50 * para) / COLS;

int meteorites[LINES - 4][3];
randomize_meteors(meteorites);


struct timespec ts = {
    .tv_sec = 0,
    .tv_nsec = speedpara * 100000000L
};

struct timespec ts2 = {
    .tv_sec = 1,
    .tv_nsec = 0L
};


//rocket indexes
int y = LINES - 1;
int x = COLS / 2;
attron(COLOR_PAIR(3));
mvprintw(y, x,"^");
attroff(COLOR_PAIR(3));

attron(COLOR_PAIR(4));
mvprintw(LINES - 1, COLS / 2 - 2, "*");
mvprintw(LINES - 2, COLS / 2 - 2, "*");
mvprintw(LINES - 1, COLS / 2 + 2, "*");
mvprintw(LINES - 2, COLS / 2 + 2, "*");

mvprintw(0, COLS / 2 - 2, "*");
mvprintw(1, COLS / 2 - 2, "*");
mvprintw(0, COLS / 2 + 2, "*");
mvprintw(1, COLS / 2 + 2, "*");
attroff(COLOR_PAIR(4));

refresh();

int attempts = 1;
// game loop
while(1) {
attron(COLOR_PAIR(1));
mvprintw(0,0, "YOUR ATTEMPTS: %d", attempts);
attroff(COLOR_PAIR(1));

    // check if end was reached
    if (y == 0) {
        attron(COLOR_PAIR(1));
        mvprintw(LINES/2, COLS/2 - 4, "YOU WON!");
        attroff(COLOR_PAIR(1));
        refresh();
        getchar();
        getchar();
        endwin();
        return EXIT_SUCCESS;

    }
    //check collision
    for (int i = 0; i < LINES - 4; i++) {
        if ((meteorites[i][2] == 0 && y == meteorites[i][0] && (x == meteorites[i][1] || x == meteorites[i][1] - 1)) || (meteorites[i][2] == 1 && y == meteorites[i][0] && (x == meteorites[i][1] || x == meteorites[i][1] + 1)))  {
            attron(COLOR_PAIR(3));
            mvprintw(y,x,"*");

            refresh();
            nanosleep(&ts2, NULL);

            mvprintw(y - 1,x - 1,"*");
            mvprintw(y - 1,x + 1,"*");
            mvprintw(y + 1,x - 1,"*");
            mvprintw(y + 1,x + 1,"*");
            mvprintw(y,x," ");
            mvprintw(y,x - 2,"*");
            mvprintw(y,x + 2,"*");

            refresh();
            nanosleep(&ts2, NULL);
            
            mvprintw(y - 1,x - 1," ");
            mvprintw(y - 1,x + 1," ");
            mvprintw(y + 1,x - 1," ");
            mvprintw(y + 1,x + 1," ");
            mvprintw(y,x - 2," ");
            mvprintw(y,x + 2," ");

            
            y = LINES - 1;
            x = COLS/2;
            attempts = attempts + 1;
            mvprintw(y,x,"^");
            
            attroff(COLOR_PAIR(3));
            refresh();

        }
    }
    
    int input = getch();
    switch (input) {
        case 'q': {
            attron(COLOR_PAIR(1));
            mvprintw(LINES/ 2, COLS/2 - 6, "QUITTING...");
            attroff(COLOR_PAIR(1));
            refresh();
            nanosleep(&ts2, NULL);
            endwin();
            return EXIT_SUCCESS;

        break;}
        case KEY_UP: {
        if (y > 0) {
            attron(COLOR_PAIR(3));
            mvprintw(y - 1, x, "^");
            attroff(COLOR_PAIR(3));

            mvprintw(y,x," ");
            
            y = y - 1;
            }
        break;}
        case KEY_DOWN: {
            if (y < LINES - 1) {
                attron(COLOR_PAIR(3));
                mvprintw(y + 1, x, "^");
                attroff(COLOR_PAIR(3));

                mvprintw(y, x, " ");
                y = y + 1;
            }
        break;}


    }
    
    // meteor placing shenanigans
    int i;

    attron(COLOR_PAIR(2));
    for (i = 0; i < LINES - 4; i++) {

        if (meteorites[i][2] == 0) {
            mvprintw(meteorites[i][0], meteorites[i][1], "o");
            mvprintw(meteorites[i][0], meteorites[i][1] - 1, " ");
            meteorites[i][1] = meteorites[i][1] + 1;
        }
        else {
        mvprintw(meteorites[i][0], meteorites[i][1], "o"); // "o " 
        mvprintw(meteorites[i][0], meteorites[i][1] + 1, " ");     
        meteorites[i][1] = meteorites[i][1] - 1;}

    
    if (meteorites[i][2] == 0 && meteorites[i][1] == COLS - 1 ) {

        mvprintw(meteorites[i][0], meteorites[i][1] - 1, "  ");
         int xleft = -1 * (rand() % COLS);
        meteorites[i][1] = xleft;
}         
    else if (meteorites[i][2] == 1 && meteorites[i][1] == 0) {
        mvprintw(meteorites[i][0], meteorites[i][1] , "  ");
        int xright = (rand() % COLS) + COLS;
        meteorites[i][1] = xright;}
} 
attroff(COLOR_PAIR(2));


    //screen update
    refresh();
    nanosleep(&ts, NULL);

}



getchar();

endwin();

return EXIT_SUCCESS;
}

void randomize_meteors(int meteorites[LINES - 4][3]) {

//assign a side
for (int i = 0; i < LINES - 4; i++) {
    int randside = rand() % 2;
    meteorites[i][2] = randside;

}

//give random x indexes
int line = 2;

for (int i = 0; i < LINES - 4; i++) {
    meteorites[i][0] = line;
    line = line + 1;
    if (meteorites[i][2] == 0) {
        int xleft = -1 * (rand() % COLS);
        meteorites[i][1] = xleft;
    }
    else{
        int xright = (rand() % COLS) + COLS;
        meteorites[i][1] = xright;
    }


}


}
