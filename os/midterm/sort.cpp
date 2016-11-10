#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <vector>
#include <string.h>

char *buffer, *s, *hash;
int size_of_buffer = 7;
int st = 0, en = 0;
int hash_size = 100;
std::vector< std::pair<char *, char *> > data;

int length(int st, int en) {
    if (st <= en) {
        return en - st;
    } else {
        return size_of_buffer - st + en;
    }
}

void print_string(char *s, int len, int fd) {
    int was = 0;
    while (was < len) {
        int res = write(fd, s + was, len - was);
        if (res < 0) {
            exit(2);
        }
        was += res;
    }
}

void destructor() {
    if (buffer) free(buffer);
    if (s) free(s);
    if (hash) free(hash);
    for (unsigned i = 0; i < data.size(); i++) {
        if (data[i].first) free(data[i].first);
        if (data[i].second) free(data[i].second);
    }

}

char *safe_malloc(int size) {
    char *res = (char *)malloc(size);
    if (res == NULL) {
        destructor();
        exit(3);
    }
    return res;
}

int main() {
    buffer = safe_malloc(size_of_buffer);
    while (1) {
        int read_result;
        if (en == size_of_buffer) {
            en = 0;
        }
        int len = size_of_buffer - length(st, en);
        if (len == 0) {
            exit(2);
        }
        read_result = read(0, buffer + en, len);

        if (read_result < 0) {
            return 1;
        }

        if (read_result == 0) {
            break;
        }

        if (read_result > 0) {
            read_result += en;
            for (; en < read_result; ) {
                if (buffer[en] == '\n') {
                    int lens = length(st, en);
                    s = safe_malloc(lens);
                    if (st <= en) {
                        memcpy(s, buffer + st, en - st);
                    } else {
                        memcpy(s, buffer + st, size_of_buffer - st);
                        memcpy(s + size_of_buffer - st, buffer, en);
                    }   
                    *(s + lens) = 0;
                    int pipefd[2];
                    pipe(pipefd);
                    print_string(s, lens, pipefd[1]);
                    int pipefdHash[2];
                    pipe(pipefdHash);
                    if (fork()) {
                        close(pipefd[0]);
                        close(pipefd[1]);
                        wait(NULL);
                        close(pipefdHash[1]);
                        hash = safe_malloc(hash_size);
                        int already = 0;
                        int read_result_hash = 0;
                        while ((read_result_hash = read(pipefdHash[0], hash + already,hash_size - already)) > 0) {
                            already += read_result_hash;
                        }
                        close(pipefdHash[0]);
                        data.push_back(std::make_pair(hash, s));
                    } else {
                        dup2(pipefd[0], 0);
                        
                        close(pipefd[0]);
                        close(pipefd[1]);
                        dup2(pipefdHash[1], 1);
                        close(pipefdHash[1]);
                        close(pipefdHash[0]);
                        execlp("sha1sum", "sha1sum", NULL);
                    }
                    st = en + 1;
                    if (st == size_of_buffer) {
                        st = 0;
                        en = 0;
                        break;
                    } else {
                        en = st;
                    }
                } else {
                    en++;
                    if (length(st, en) == size_of_buffer - 1) {
                        exit(19);
                    }
                }
            }
        }
    }
    for (unsigned i = 0; i < data.size(); i++) {
        int best = i;
        for (unsigned j = i; j < data.size(); j++) {
            if (strcmp(data[j].first, data[i].first) < 0) {
                best = j;
            }
        }
        std::pair<char *, char *> tmp = data[i];
        data[i] = data[best];
        data[best] = tmp;
    }
    printf("-------------------\n");
    for (unsigned i = 0; i < data.size(); i++) {
        printf("%s %s\n", data[i].first, data[i].second);
    }
    destructor();
    return 0;
}
