#include <iostream>
#include <chrono>
#include <thread>

#include "variable.h"
#include "epollfd.h"
#include "autofd.h"
#include "ARead.h"
#include "iom.h"
#include "buffer.h"
#include "AWrite.h"
#include "AAccept.h"
#include "EPOLL.h"

epollfd x(12);

EPOLL E;
buffer b(10);
 
int main() {
    iom io;
    variable<bool> is_true(io);
    variable<bool> is_true2 = !!!is_true;
    variable<bool> is_true3 = is_true & is_true2;
    is_true3.subscribe([](bool v) { return v == true; }, [](){ std::cerr << "is_true3 -> true 2" << std::endl; });
 
//    is_true3.subscribe([](bool v) { return v == false; }, [](){ std::cerr << "is_true3 -> false 1" << std::endl; });
    is_true = true;
    std::cerr << "is_true | true" << std::endl;
//   is_true = false;
//    std::cerr << "is_true | false" << std::endl;
//    is_true.subscribe([](bool v) { return v == true; }, [](){ std::cerr << "is_true -> true 3" << std::endl;  });
//    is_true = true;
//    std::cerr << "is_true | true" << std::endl;
//    is_true3.subscribe([](bool v) { return v == true; }, [](){ std::cerr << "is_true3 -> true 4" << std::endl;  });
//    is_true = false;
//    std::cerr << "is_true | false" << std::endl;
//    std::cerr << "_____________________" << std::endl;
 //   is_true3.subscribe([](bool v) { return v == false; }, [](){ std::cerr << "is_true3 -> false 5" << std::endl;  });
  //  is_true2 = false;
  //  std::cerr << "is_true2 | false" << std::endl;
 //   is_true3.subscribe([](bool v) { return v == true; }, [](){ std::cerr << "is_true3 -> true" << std::endl;  });
//    is_true = false;
//    std::cerr << "is_true | false" << std::endl;
/*    std::thread t1([&E]() {
            while(true) { 
            E.cycle();
            }
            });
    
   E.aread(0, b, [](){ std::cerr << "aread comp" << std::endl; }, nullptr);
   while (true) {
       sleep(5);
       E.awrite(1, b, [](){ std::cerr << "awrite comp" << std::endl; }, nullptr);
   }
*/
   // E.aread(0, b, []() { E.awrite(1, b, []() {}, nullptr); }, nullptr);
    
/*    add_test_awrite();
    add_test_aread();
    for (int x = 8822; x <= 8825; x++) {
        add_test_aaccept(x);
    }

    for (int i = 0; i < 1; i++) {
        add_test_awrite();
        std::this_thread::sleep_for(std::chrono::seconds(3));
    }
    t1.join();
    */

/*    int fd = open("output.txt", O_RDONLY);
    x.subscribe(0, EPOLLIN, []() {
            std::cout << "ok" << std::endl; 
            char buf[128];
            while (read(0, buf, 128) != 0) {
                
            }
            x.subscribe(0, EPOLLIN, [](){ std::cout << "ok2" << std::endl; }, []() {std::cout << "notok2" << std::endl; } );
                }, []() { std::cout << "notok" << std::endl; }); 
//    x.subscribe(1, EPOLLOUT
    x.cycle();
    x.cycle();
  //  for (int i = 0; i < 10; i++) {
  //      write(fd, "1", 1);
  //  }
*/  
}

void add_test_aaccept(int port_num) {
    struct addrinfo hints, *res;
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE;
    if (getaddrinfo(NULL, std::to_string(port_num).c_str(), &hints, &res)) {
        perror("error getaddrinfo");
    }
    int sfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    if (sfd == -1) {
        perror("error socket");
    }
    int optval = 1;
    if (setsockopt(sfd, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(int)) == -1)    {
        perror("error setsockopt");
    }

    if (bind(sfd, res->ai_addr, res->ai_addrlen) == -1) {
        perror("error bind");
    }
        if (listen(sfd, 5) == -1) {
        perror("error listen");
    }
    struct sockaddr_storage peer_addr;
    socklen_t peer_addr_size = sizeof(struct sockaddr_storage);

    E.aaccept(sfd, (struct sockaddr *) &peer_addr, &peer_addr_size, 
            [](int new_fd) { std::cerr << "aaccept complete" << std::endl; }, 
            []() { std::cerr << "aacept bad" << std::endl; });
}

void add_test_aread() {
    E.aread(0, b, []() { std::cerr << "aread complete" << std::endl; }, []() { std::cerr << "aread bad" << std::endl; });
}

void add_test_awrite() {
    E.awrite(1, b, []() { std::cerr << "awrite complete" << std::endl; }, []() { std::cerr << "awrite bad" << std::endl; });
}


