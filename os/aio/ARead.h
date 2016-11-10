#pragma once

#include <functional>

#include "epollfd.h"
#include "buffer.h"
#include "AOperation.h"

class ARead : public AOperation{
    public:
        template <typename buffer>
        ARead(epollfd &_epoll_fd, int fd, buffer &buf, std::function<void()> cont, std::function<void()> cont_err) : AOperation(_epoll_fd, fd, EPOLLIN) {

            bool *_valid(valid);
            epoll_fd->subscribe(fd, EPOLLIN, [&buf, _valid, cont, fd]() {
                        buf.read(fd);
                        *_valid = false;
                        cont();
                    }, [_valid, cont_err]() {
                        *_valid = false;
                        cont_err();
                    });
        }
};
