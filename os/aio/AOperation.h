#pragma once

#include <functional>

#include "epollfd.h"
#include "buffer.h"
#include "autofd.h"

class AOperation {
    public:
        AOperation() : valid(new bool(false)) {};
        
        AOperation(epollfd &epoll_fd, int fd, int operation) : epoll_fd(&epoll_fd), operation(operation), fd(fd), valid(new bool(true)) {};

        AOperation(AOperation &&other) : epoll_fd(other.epoll_fd), fd(other.fd), valid(other.valid) {
            other.valid = new bool(false);
        }

        AOperation & operator= (AOperation &&other) {
            epoll_fd = other.epoll_fd;
            valid = other.valid;
            other.valid = new bool(false);
            operation = other.operation;
            fd = other.fd;
            return *this;
        }

        ~AOperation() {
            if (*valid) epoll_fd->unsubscribe(fd, operation);
            delete valid;
        }
    protected:
        epollfd *epoll_fd;
        int operation;
        int fd;
        bool *valid;
};
