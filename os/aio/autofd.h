#pragma once 

#include <unistd.h>

class autofd {
    public:
        autofd() {
            fd = -1;
        }

        autofd(int _fd) {
            if (_fd < 0) throw 1;
            fd = _fd;
        }

        autofd(autofd &&other) {
            fd = other.fd;
            other.fd = -1;
        }

        autofd & operator= (autofd &&other) {
            fd = other.fd;
            other.fd = -1;
            return *this;
        }

        int operator *() {
            return fd;
        }

        ~autofd() {
            if (fd != -1) {
                close(fd);
            }
        }
    private:
        int fd;
};
