#pragma once

#include <functional>

#include "epollfd.h"
#include "buffer.h"
#include "AOperation.h"

class AWrite : public AOperation{
    public:
        template <typename buffer>
        AWrite(epollfd &_epoll_fd, int fd, buffer &buf, std::function<void()> cont, std::function<void()> cont_err) : AOperation(_epoll_fd, fd, EPOLLOUT) {

            bool *_valid(valid);
            epoll_fd->subscribe(fd, EPOLLOUT, [&buf, _valid, cont, fd]() {
                        buf.write(fd);
                        *_valid = false;
                        cont();
                    }, [_valid, cont_err]() {
                        *_valid = false;
                        cont_err();
                    });
        }
};
