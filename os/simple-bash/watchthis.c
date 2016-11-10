#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

const char *previous_output = "prev";
const char *current_output = "cur";

void destructor() {
    remove(current_output);
    remove(previous_output);
    exit(0);
}

int main(int argc, char **argv) {
    if (argc < 3) {
        exit(1);
    }

    close(open(previous_output, O_CREAT,  S_IRWXU | S_IRWXG | S_IRWXO));

    signal(SIGINT, destructor);
    signal(SIGTERM, destructor);
    int timeout = atoi(argv[1]);

    while (1) {
        int cur_out_fd = open(current_output, O_RDWR | O_CREAT,  S_IRWXU | S_IRWXG | S_IRWXO);
        int pid1 = fork();
        if (pid1) {
            waitpid(pid1, NULL, 0);
        } else {
            dup2(cur_out_fd, 1);
            close(cur_out_fd);
            execvp(argv[2], argv + 2);
        }
        int pid_print = fork();
        if (pid_print) {
            waitpid(pid_print, NULL, 0);
        } else {
            execlp("cat", "cat", current_output, NULL);
        }
        int pid2 = fork();
        if (pid2) {
            waitpid(pid2, NULL, 0);
        } else {
            execlp("diff", "diff", "-u", previous_output, current_output, NULL);
        }
        int pid3 = fork();
        if (pid3) {
            waitpid(pid3, NULL, 0);
        } else {
            execlp("mv", "mv", current_output, previous_output, NULL);
        }
        sleep(timeout);
    }
}
