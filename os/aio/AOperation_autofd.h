#pragma once

#include <functional>

#include "epollfd.h"
#include "buffer.h"
#include "autofd.h"

class AOperation {
    public:
        AOperation() : valid(new bool(false)) {};
        
        AOperation(epollfd &epoll_fd, int fd, int operation) : epoll_fd(&epoll_fd), fd(fd), valid(new bool(true)), operation(operation) {};

        AOperation(AOperation &&other) : epoll_fd(other.epoll_fd), fd(std::move(other.fd)), valid(other.valid) {
            other.valid = new bool(false);
        }

        AOperation & operator= (AOperation &&other) {
            epoll_fd = other.epoll_fd;
            valid = other.valid;
            other.valid = new bool(false);
            operation = other.operation;
            fd = std::move(other.fd);
            return *this;
        }

        ~AOperation() {
            if (*valid) epoll_fd->unsubscribe(*fd, operation);
            delete valid;
        }
    protected:
        epollfd *epoll_fd;
        int operation;
        autofd fd;
        bool *valid;
};
