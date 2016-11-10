#pragma once

#include <functional>
#include <list>
#include <memory>
#include <map>
#include <iostream>
#include <vector>

#include "iom.h"

template <typename T>
class variable_data;

template <typename T>
class variable {
    public:
        variable(iom &iomanager, T value) : data(new variable_data<T>(iomanager, value)) {
            any_change = [this](T new_val) { return new_val != data->value; };
        }
        
        variable(iom &iomanager) : variable(iomanager, T()) {}

        const variable& operator= (T new_value) const {
            if (new_value != data->value) {
                for (auto it = data->conts.begin(); it != data->conts.end();) {
                    if (it->first(new_value)) {
                        data->iomanager.add_cont(it->second);
                        it = data->conts.erase(it);
                    } else it++;
                }
                data->value = new_value;
                data->iomanager.cycle();
            }
            return *this;
        }

        void subscribe(std::function<bool(const T&)> predicate, const std::function<void()> &cont) const {
            if (predicate(data->value)) {
                data->iomanager.add_cont(cont);
                data->iomanager.cycle();
            } else data->conts.push_back(make_pair(predicate, cont));
        } 

        T operator*() const {
            return data->value;
        }

        static variable<T> unaryop (const variable<T> &var1, const std::function<T(const T&)> &oper) {
            variable<T> res(var1.data->iomanager, oper(var1.data->value));
            res.data->our_subscribes.push_back([](){});
            std::function<void()> &f = res.data->our_subscribes[0];
            f = [res, var1, &f, oper]() {
                res = oper(var1.data->value);
                var1.subscribe(var1.any_change, f);
            };
            var1.subscribe(var1.any_change, f);
            return res;
        }

        friend variable operator! (const variable &var1) {
            return unaryop(var1, [](const T &v1) { return !v1; });
        }

        static variable<T> binop (const variable<T> &var1, const variable<T> &var2, const std::function<T(const T&, const T&)> &oper) {
            variable<T> res(var1.data->iomanager, oper(var1.data->value, var2.data->value));
            res.data->our_subscribes.push_back([](){});
            res.data->our_subscribes.push_back([](){});
            std::function<void()> &f1 = res.data->our_subscribes[0], &f2 = res.data->our_subscribes[1];
            f1 = [res, var1, var2, &f1, oper]() {
                res = oper(var1.data->value, var2.data->value);
                var1.subscribe(var1.any_change, f1);
            };
            f2 = [res, var1, var2, &f2, oper]() {
                res = oper(var1.data->value, var2.data->value);
                var2.subscribe(var2.any_change, f2);
            };
            var1.subscribe(var1.any_change, f1);
            var2.subscribe(var2.any_change, f2);
            return res;
        }

        friend variable operator& (const variable &var1, const variable &var2) {
            return binop(var1, var2, [](const T &v1, const T &v2) { return v1 & v2; });
        }

        friend variable operator| (const variable &var1, const variable &var2) {
            return binop(var1, var2, [](const T &v1, const T &v2) { return v1 | v2; });
        }

        friend variable operator^ (const variable &var1, const variable &var2) {
            return binop(var1, var2, [](const T &v1, const T &v2) { return v1 ^ v2; });
        }


        std::function<bool(const T&)> any_change; 
    private:
        std::shared_ptr<variable_data<T>> data;
};

template <typename T>
struct variable_data {
    variable_data(iom &iomanager, T value) : value(value), iomanager(iomanager) {}

    T value;
    iom &iomanager;
    std::vector<std::function<void()>> our_subscribes;
    std::list<std::pair<std::function<bool(const T&)>, std::function<void()>>> conts;
};
