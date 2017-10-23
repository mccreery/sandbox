#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int filln(int *n, char *str) {
	for(int i = 0; str[i] != '\0'; i++) {
		if(!isdigit(str[i])) return 0;
	}
	*n = atoi(str);
	return 1;
}

int main(int argc, char **argv) {
	int exp = 3;
	for(int i = 1; i < argc; i++) {
		if(filln(&exp, argv[i])) break;
	}

	for(int i = 0, n = exp, goal = 9; n > 0; i++) {
		printf("%0*d ", exp, i);

		if(i == goal) {
			goal = goal*10 + 9;
			--n;
		}
	}
	putchar('\n');
}
