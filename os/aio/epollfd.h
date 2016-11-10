#pragma once 

#include <iostream>
#include <stdexcept>
#include <functional>
#include <map>
#include <sys/epoll.h>
#include <poll.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <signal.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <pty.h>
#include <netdb.h>
#include <vector>
#include <fcntl.h>

class epollfd {
    public:
        epollfd(int max_count_events) : events(max_count_events), max_count_events(max_count_events) {
            epoll_fd = epoll_create(max_count_events);
            if (epoll_fd == -1) {
                throw std::runtime_error("epoll error");
            }
        }

        void subscribe(int fd, int what, std::function<void()> cont, std::function<void()> cont_err) {
            std::cerr << "subscribing" << std::endl;
            if (conts.count(fd) != 0 && conts[fd].count(what) != 0) throw 1;

            ev.events = what | events_for_fd[fd];
            ev.data.fd = fd;
            if (conts.count(fd) == 0) {
                if (epoll_ctl(epoll_fd, EPOLL_CTL_ADD, fd, &ev) == -1) {
                    throw std::runtime_error("epoll add error 1 ");
                }
            } else {
                if (epoll_ctl(epoll_fd, EPOLL_CTL_MOD, fd, &ev) == -1) {
                    throw std::runtime_error("epoll add error 2");
                }
            }
            events_for_fd[fd] |= what;
            conts[fd][what] = std::make_pair(cont, cont_err);
        }

        void unsubscribe(int fd, int what) {
            if (conts.count(fd) == 0 || conts[fd].count(what) == 0)
                throw std::runtime_error("epoll del error");

            ev.events = what ^ events_for_fd[fd];
            ev.data.fd = fd;
            if (epoll_ctl(epoll_fd, EPOLL_CTL_MOD, fd, &ev) == -1) {
                throw std::runtime_error("epoll mod error");
            }
            events_for_fd[fd] ^= what;
            conts[fd].erase(what);
            if (conts[fd].size() == 0) {
                conts.erase(fd);
                if (epoll_ctl(epoll_fd, EPOLL_CTL_DEL, fd, &ev) == -1) {
                    throw std::runtime_error("del error");
                }
            }
        }
        
        void cycle() {
            std::cerr << "cycling" << std::endl;
            int nfds = epoll_wait(epoll_fd, events.data(), max_count_events, -1);
            if (nfds == -1) {
                throw std::runtime_error("cycle error");
            }
            for (int i = 0; i < nfds; i++) {
                int cur_event = 1;
                for (int j = 0; j < 32; j++) {
                    if (events[i].events & cur_event) {
//                        std::cerr << (events[i].events & ERRORS) << " happend " << std::endl;
                        int fd = events[i].data.fd;
                        std::function<void()> cont;
                        if (conts.count(fd) != 0 && conts[fd].count(cur_event) != 0) {
                            if (events[i].events & ERRORS) {
                                cont = conts[events[i].data.fd][cur_event].second;
                            } else {
                                cont = conts[events[i].data.fd][cur_event].first;
                            }
                            unsubscribe(fd, cur_event);
                            cont();
                        }
                    } 
                    cur_event <<= 1;
                }
            }
        }

        ~epollfd() {
            close(epoll_fd);
        }

    private:
        std::map<int, std::map<int, std::pair<std::function<void()>, std::function<void()>>>> conts;
        std::map<int, int> events_for_fd;
        std::vector<epoll_event> events;
        epoll_event ev;
        int epoll_fd;
        int max_count_events;
        const int ERRORS = EPOLLERR | EPOLLHUP;
};
