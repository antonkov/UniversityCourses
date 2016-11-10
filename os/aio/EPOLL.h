#include <list>
#include <functional>

#include "AOperation.h"
#include "ARead.h"
#include "AWrite.h"
#include "AAccept.h"
#include "epollfd.h"

class EPOLL {
    public:
        EPOLL() : epoll_fd(10) {
        }

        EPOLL(epollfd &epoll_fd) : epoll_fd(epoll_fd) {}

        template <typename buffer>
        void aread(int fd, buffer &buf, std::function<void()> cont, std::function<void()> cont_err) {
            auto place = last_pos();
            *place = ARead(epoll_fd, fd, buf, [this, place, cont]() {
                        this->l.erase(place);
                        cont();
                    }, 
                    [this, place, cont_err]() {
                        this->l.erase(place);
                        cont_err();
                    });
        };

        template <typename buffer>
        void awrite(int fd, buffer &buf, std::function<void()> cont, std::function<void()> cont_err) {
            auto place = last_pos();;
            *place = AWrite(epoll_fd, fd, buf, [this, place, cont]() {
                        this->l.erase(place);
                        cont();
                    }, 
                    [this, place, cont_err]() {
                        this->l.erase(place);
                        cont_err();
                    });
        };

        void aaccept(int fd, sockaddr *addr, socklen_t *addrlen, std::function<void(int)> cont, std::function<void()> cont_err) {
            auto place = last_pos();;
            *place = AAccept(epoll_fd, fd, addr, addrlen, [this, place, cont](int new_client_fd) {
                        this->l.erase(place);
                        cont(new_client_fd);
                    }, 
                    [this, place, cont_err]() {
                        this->l.erase(place);
                        cont_err();
                    });
           
        };

        void cycle() {
            epoll_fd.cycle();
        }

    private:
        std::list<AOperation>::iterator last_pos() {
            AOperation oper;
            l.push_back(std::move(oper));
            return --l.end();
        }

        epollfd epoll_fd;
        std::list<AOperation> l;
};
