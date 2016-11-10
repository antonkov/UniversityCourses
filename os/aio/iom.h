#pragma once

#include <deque>
#include <vector>
#include <functional>

class iom {
    public:
        iom() : cycle_is_running(false) {};

        void add_cont(std::function<void()> f) {
        //    std::cerr << f << std::endl;
            q.push_back(f);
        }
        
        void cycle() {
            if (cycle_is_running) return;
            cycle_is_running = true;
            while(!q.empty()) {
                std::cerr << q.size() << std::endl;
                auto f = q.back();
                q.pop_back();
                f();
            }
            cycle_is_running = false;
        }

       
    private:
        bool cycle_is_running;
        std::deque<std::function<void()>> q;
};
