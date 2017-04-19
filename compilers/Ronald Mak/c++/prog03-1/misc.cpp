#include <string.h>
#include<ctype.h>

char *strlwr(char *str)
{
  char *p = str;

  while (*p) {
     *p = tolower(*p);
      p++;
  }

  return str;
}
