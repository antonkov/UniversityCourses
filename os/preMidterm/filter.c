#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int size_buffer = 4 * 1024;
char split_char = '\n';
char *buffer;
char **args;
char **argv;
int argc;

void destructor() {
    if (buffer) free(buffer);
    if (args) free(args);
}

void *safe_malloc(int size) {
    void *res = malloc(size);
    if (res == NULL) {
        destructor();
        exit(3);
    }
    return res;
}

void print_string(char *buffer, int count) {
    int was_written = 0;
    while (was_written < count) {
        int cur = write(1, buffer + was_written, count - was_written);
        if (cur > 0) {
            was_written += cur;
        }
    }
}

int EXIT_SUCCESFULLY(int status) {
    return WIFEXITED(status) && (WEXITSTATUS(status) == 0);
}

void trythis(char *s) {
    int i = optind;
    args = (char **)safe_malloc(sizeof(char*) * (argc + 1));
    for (; *(argv + i); i++) {
        if (strcmp(*(argv + i), "{}") == 0) {
            (*(args + i)) = s;
        } else {
            *(args + i) = *(argv + i);
        }
    }
    *(args + argc) = 0;
    int pid = fork();
    int pipefd[2];
    pipe(pipefd);
    if (pid == 0) {
      dup2(pipefd[1], 1); 
        
       execvp(args[optind], args + optind);
       exit(5);
    } 
    int status;
    waitpid(pid, &status, 0);
    if (EXIT_SUCCESFULLY(status)) { 
        int slen = strlen(s);
        s[slen] = '\n';
        print_string(s, slen + 1);
    }
    free(args);
    args = NULL;
}

int main(int argcc, char **argvv) {
    argc = argcc;
    argv = argvv;
    int c = 0;
    while ((c = getopt(argc, argv, "nzb:")) != -1) {
        switch (c) 
        {
            case 'n':
                split_char = '\n';
                break;
            case 'z':
                split_char = 0;
                break;
            case 'b':
                size_buffer = atoi(optarg);
                break;
            default:
                abort();
        }
    }
   
    buffer = (char *)safe_malloc(size_buffer + 1);
    int len = 0;
    while (1) {
        int read_result = read(0, buffer + len, size_buffer + 1 - len);
        
        if (read_result < 0) {
            return 1;
        }

        if (read_result == 0) {
            if (len != 0 && len <= size_buffer) {
                buffer[len] = 0;
                trythis(buffer);
            }
            break;
        }

        if (read_result > 0) {
            read_result += len;
            for (; len < read_result; ) {
                if (buffer[len] == split_char) {
                    buffer[len] = 0;
                    trythis(buffer);
                    memmove(buffer, buffer + len + 1, read_result - len - 1);
                    read_result = read_result - len - 1;
                    len = 0;
                } else {
                    len++;
                }
            }
            read_result += len;
        }
        
        if (len == size_buffer + 1) {
            return 1;
        }
    }        
    destructor();
    return 0;
}
