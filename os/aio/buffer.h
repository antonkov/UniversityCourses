#pragma once

#include <iostream>
#include <unistd.h>
#include <string.h>

class buffer {
    public:
        buffer(char *s) : buf(s), pos(0) {
            max_len = strlen(s) + 1;
        }

        buffer(int max_len) : max_len(max_len), pos(0) {
            buf = new char[max_len];
        }

        void read(int fd) {
            int cur = 0;
            while ((cur = ::read(fd, buf + pos, max_len - pos)) > 0) {
                pos += cur;
            }
        }

        void write(int fd) {
            int cur = 0;
            while (cur < pos) {
                int x = ::write(fd, buf + cur, pos - cur);
                if (x > 0) cur += x;
            }
            pos = 0;
        }

        ~buffer() {
            delete buf;
        }
    private:
        int max_len;
        char *buf;
        int pos;
};
