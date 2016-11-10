#include <unistd.h>
#include <stdlib.h>
#include <string.h>
 
typedef enum {
    PRINT, NO_PRINT
} state;
char *buffer;

void print_string(char *buffer, int count) {
    int was_written = 0;
    while (was_written < count) {
        int cur = write(1, buffer + was_written, count - was_written);    
        if (cur > 0) {
            was_written += cur;
        }
    }
}

void print_string_ntimes(char *buffer, int x, int n) {
    int i = 0;
    for (; i < n; i++) {
        print_string(buffer, x);
    }
}

void destructor() {
    if (buffer) free(buffer);
}

char *safe_malloc(int size) {
    char *res = (char *)malloc(size);
    if (res == NULL) {
        destructor();
        exit(3);
    }
    return res;
}

int main(int argc, char* argv[]) {
   if (argc < 2) {
       return 1;
   }
   int k = atoi(argv[1]);
   if (k < 1) {
       return 2;
   }
   
   state current_state = PRINT;
   int len = 0;
   
   buffer = safe_malloc(k + 1);

   while (1) {
       int read_result = read(0, buffer + len, k + 1 - len);
       if (read_result < 0) return 1;

       if (read_result == 0) {
           if (len != 0 && len <= k && current_state == PRINT) {
               buffer[len] = '\n';
               print_string_ntimes(buffer, len + 1, 2);
           }
           break;
       }
       
       if (read_result > 0) {
           read_result += len;
           for (; len < read_result;) {
               if (buffer[len] == '\n') {
                   if (current_state == PRINT) {
                       print_string_ntimes(buffer, len + 1, 2);
                   } else if (current_state == NO_PRINT) {
                       current_state = PRINT;
                   }
                   memmove(buffer, buffer + len + 1, read_result - len - 1);
                   read_result = read_result - len - 1;
                   len = 0;
               } else {
                   len++;
               }
           }

           if (len == k + 1) {
               len = 0;
               current_state = NO_PRINT;
           }
       }
   }  
   destructor();
   return 0;
}
