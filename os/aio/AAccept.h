#pragma once

#include <functional>
#include <sys/types.h>
#include <sys/socket.h>

#include "epollfd.h"
#include "buffer.h"
#include "AOperation.h"

class AAccept : public AOperation{
    public:
        AAccept(epollfd &_epoll_fd, int fd, sockaddr *addr, socklen_t *addrlen, std::function<void(int)> cont, std::function<void()> cont_err) : AOperation(_epoll_fd, fd, EPOLLIN) {

            bool *_valid(valid);
            epoll_fd->subscribe(fd, EPOLLIN, [_valid, cont, cont_err, fd, addr, addrlen]() {
                        int new_client_fd = accept(fd, addr, addrlen);
                        *_valid = false;
                        if (new_client_fd < 0) cont_err();
                        else cont(new_client_fd);
                    }, [_valid, cont_err]() {
                        *_valid = false;
                        cont_err();
                    });
        }
};
