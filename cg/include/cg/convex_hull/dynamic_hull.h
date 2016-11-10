#pragma once

#include <iostream>
#include <vector>
#include <set>
#include <gmpxx.h>

#include "cg/primitives/point.h"
#include "cg/operations/orientation.h"

namespace cg {

    typedef std::vector<point_2>::iterator vect_it;

    enum node_type{LEAF, INTERNAL};
    enum rb_color{RED, BLACK};
    typedef std::pair<point_2, point_2> bridge_t;

    class rbnode {
    public:
        rbnode() {};

        rbnode(point_2 x, rb_color color = BLACK, node_type type = LEAF, rbnode *left = NULL, rbnode *right = NULL)
            : x(x), color(color), type(type), parent(NULL), left(left), right(right)  {
                if (left) left->parent = this;
                if (right) right->parent = this;
            }

        point_2 x;
        rb_color color;
        node_type type;
        rbnode *parent, *left, *right;
        bridge_t bridge;
    };

    class rbtree {
    public:
        rbtree() : root(NULL), size(0) {}

        void left_rotate(rbnode *p){
            rbnode *n = p->right, *g = p->parent;
            p->right = n->left;
            p->right->parent = p;
            n->left = p;
            n->parent = p->parent;
            p->parent = n;
            if (g) {
                if (g->left == p) g->left = n;
                else g->right = n;
            } else root = n;
        }

        void right_rotate(rbnode *p){
            rbnode *n = p->left, *g = p->parent;
            p->left = n->right;
            p->left->parent = p;
            n->right = p;
            n->parent = p->parent;
            p->parent = n;
            if (g) {
                if (g->left == p) g->left = n;
                else g->right = n;
            } else root = n;
        } 

        void red_brothers(rbnode *p, rbnode *u){
            p->color = u->color = BLACK;
            p->parent->color = RED;
        }

        void find_bridge(rbnode *n) {
            if (n->type == LEAF) return;
            n->x = n->right->x;
            rbnode *l = n->left, *r = n->right;
            while (l->type != LEAF || r->type != LEAF) {
                if (l->type != LEAF) {
                    if (orientation(l->bridge.first, l->bridge.second, 
                                r->type == LEAF ? r->x : r->bridge.first) != CG_RIGHT) {
                        l = l->left;
                        continue;
                    }
                }
                if (r->type != LEAF) {
                    if (orientation(l->type == LEAF ? l->x : l->bridge.second,
                                r->bridge.first, r->bridge.second) != CG_RIGHT) {
                        r = r->right;
                        continue;
                    }
                }
                if (l->type == LEAF) { r = r->left; continue; }
                if (r->type == LEAF) { l = l->right; continue; }
                point_2 &p0 = l->bridge.first, &p1 = l->bridge.second, &q0 = r->bridge.first, &q1 = r->bridge.second;
                const double &sx = l->x.x;
                const double &a = p1.y - p0.y, &b = q1.x - q0.x, &c = q0.y - q1.y,
                       &d = p1.x - p0.x, &e = q1.y - p0.y, &f = sx - p0.x, &g = q1.x - sx;
                const double l1 = a * b * f, l2 = d * c * g, l3 = d * e * b;
                const double res = l1 - l2 - l3;
                const double eps = (fabs(l1) + fabs(l2) + fabs(l3)) * 32 * std::numeric_limits<double>::epsilon(); 
                if (fabs(res) > eps) {
                    if (res < 0) r = r->left;
                    else l = l->right;
                    continue;
                }

                const mpq_class msx(sx);
                const point_2t<mpq_class> mp0(p0.x, p0.y), mp1(p1.x, p1.y), mq0(q0.x, q0.y), mq1(q1.x, q1.y);
                const mpq_class &ma(mp1.y - mp0.y), &mb(mq1.x - mq0.x), &mc(mq0.y - mq1.y),
                       &md(mp1.x - mp0.x), &me(mq1.y - mp0.y), &mf(msx - mp0.x), &mg(mq1.x - msx);
                const mpq_class mres(ma * mb * mf - md * mc * mg - md * me * mb);
                if (mres < 0) r = r->left;
                else l = l->right;
            }
            n->bridge = std::make_pair(l->x, r->x);
        }

        void replace(rbnode *dest, rbnode *src) {
            rbnode *p = dest->parent;
            src->parent = p;
            if (p) {
                if (p->left == dest) p->left = src;
                else p->right = src;
            }
        }

        bool red(rbnode *v) { return v->color == RED; }
        bool black(rbnode *v) { return v->color == BLACK; }

        void insert(point_2 x){
            size++;
            rbnode* tn = new rbnode(x), *n, *t, *p;
            if (size == 1) { 
                root = tn;
                return;
            } 
            t = root;
            while (t->type != LEAF) {
                if (x > t->left->x) t = t->right;
                else t = t->left;
            }
            p = t->parent; bool was_left = p && p->left == t;
            if (t->x < tn->x) std::swap(t, tn);
            n = new rbnode(t->x, RED, INTERNAL, tn, t);
            n->parent = p;
            if (size == 2) {
                root = n;
                root->color = BLACK;
                find_bridge(root);
                return;
            }
            if (was_left) p->left = n;
            else p->right = n;

            while (n != root && !black(n->parent)) {
                p = n->parent;
                rbnode *g = p->parent, *u;
                if (g->left == p) {
                    u = g->right;
                    if (red(u)) {
                        red_brothers(p, u);
                        find_bridge(n);
                        find_bridge(p);
                        n = g;
                        continue;
                    }
                    if (n == p->right) {
                        left_rotate(p);
                        std::swap(n, p);
                    }
                    p->color = BLACK;
                    g->color = RED;
                    right_rotate(g);
                    find_bridge(g);
                } else {
                    u = g->left;
                    if (red(u)) {
                        red_brothers(p, u);
                        find_bridge(n);
                        find_bridge(p);
                        n = g;
                        continue;
                    }
                    if (n == p->left) {
                        right_rotate(p);
                        std::swap(n, p);
                    }
                    p->color = BLACK;
                    g->color = RED;
                    left_rotate(g);
                    find_bridge(g);
                }
                break;
            }
            while (n != NULL) {
                find_bridge(n);
                n = n->parent;
            }
            root->color = BLACK;
        }

        void erase(point_2 x) {
            size--;
            rbnode *t, *p, *n, *s;
            if (size == 0) {
                delete root;
                root = NULL;
                return;
            }
            t = root;
            while (t->type != LEAF) {
                if (x > t->left->x) t = t->right;
                else t = t->left;
            }
            p = t->parent;
            bool was_red = red(p);
            if (p->left == t) n = p->right;
            else n = p->left;
            if (p->parent == NULL) root = n;
            replace(p, n);
            delete t;
            delete p;
            if (size == 1) return;
            if (red(n) || was_red) { 
                n->color = BLACK; 
            } else {
                while (n != root) {
                    p = n->parent;
                    if (p->left == n) {
                        s = p->right;
                        if (red(s)) {
                            p->color = RED;
                            s->color = BLACK;
                            left_rotate(p);
                            s = p->right;
                        }
                        if (black(s->left) && black(s->right)) {
                            if (black(p)) {
                                s->color = RED;
                                find_bridge(n);
                                n = p;
                                continue;
                            } else {
                                s->color = RED;
                                p->color = BLACK;
                                break;
                            }
                        }
                        if (black(s->right) && red(s->left)) {
                            s->color = RED;
                            s->left->color = BLACK;
                            right_rotate(s);
                            find_bridge(s);
                            find_bridge(p->right);
                            s = p->right;
                        }
                        s->color = p->color;
                        p->color = BLACK;
                        s->right->color = BLACK;
                        left_rotate(p);
                    } else {
                        s = p->left;
                        if (red(s)) {
                            p->color = RED;
                            s->color = BLACK;
                            right_rotate(p);
                            s = p->left;
                        }
                        if (black(s->left) && black(s->right)) {
                            if (black(p)) {
                                s->color = RED;
                                find_bridge(n);
                                n = p;
                                continue;
                            } else {
                                s->color = RED;
                                p->color = BLACK;
                                break;
                            }
                        }
                        if (black(s->left) && red(s->right)) {
                            s->color = RED;
                            s->right->color = BLACK;
                            left_rotate(s);
                            find_bridge(s);
                            find_bridge(p->left);
                            s = p->left;
                        }
                        s->color = p->color;
                        p->color = BLACK;
                        s->left->color = BLACK;
                        right_rotate(p);
                    }
                    break;
                }
            }
            while (n != NULL) {
                find_bridge(n);
                n = n->parent;
            }
        }

        bool in_convex_hull(point_2 x, double left, double right) {
            return dfs(x, root, left, right);
        }

        bool dfs(point_2 &p, rbnode *v, double left, double right) {
            if (!v || v->type == LEAF) return false;
            auto x1 = v->bridge.first.x, x2 = v->bridge.second.x;
            if (x1 >= left && x2 <= right) {
                if (v->bridge.first == p || v->bridge.second == p) return true;
            }
            if (left < x1 && p.x < x1) return dfs(p, v->left, left, std::min(x1, right));
            if (x2 < right && x2 < p.x) return dfs(p, v->right, std::max(x2, left), right);
            return false;
        }

        void get_hull(std::vector<point_2> &res, double left, double right) {
            get_hull_dfs(res, root, left, right);
        }

        void get_hull_dfs(std::vector<point_2> &res, rbnode *v, double left, double right) {
            if (!v || v->type == LEAF) return;
            auto x1 = v->bridge.first.x, x2 = v->bridge.second.x;
            if (left < x1) get_hull_dfs(res, v->left, left, std::min(x1, right));
            if (x1 >= left && x2 <= right) {
                if (res.size() == 0) res.push_back(v->bridge.first);
                res.push_back(v->bridge.second);
            }
            if (x2 < right) get_hull_dfs(res, v->right, std::max(x2, left), right);
        }
        
        void destructor_helper(rbnode *node){
            if (node == NULL) return;
            destructor_helper(node->left);
            destructor_helper(node->right);
            delete node;
        }

        ~rbtree() {
            destructor_helper(root);
        }

    private:
        rbnode *root;
        int size;
    };

    class dynamic_hull {
    public:
        dynamic_hull() : changed(true) {
        }

        void add_point(const point_2 &p) {
            if (!points_s.count(p)) {
                changed = true;
                points_s.insert(p);
                upper_hull.insert(p);
                lower_hull.insert(point_2(p.x, -p.y));
            }
        }

        bool contains(const point_2 &p) {
            if (points_s.size() <= 2) return false;
            auto left = points_s.begin()->x;
            auto right = points_s.rbegin()->x;
            return upper_hull.in_convex_hull(p, left, right) || 
                lower_hull.in_convex_hull(point_2(p.x, -p.y), left, right);
        }

        void remove_point(const point_2 &p) {
            if (points_s.count(p)) {
                changed = true;
                points_s.erase(p);
                upper_hull.erase(p);
                lower_hull.erase(point_2(p.x, -p.y));
            }
        }

        const std::pair<vect_it, vect_it> get_hull() {
            if (changed) {
                hull_v.clear();
                if (points_s.size() <= 2) return std::make_pair(hull_v.begin(), hull_v.end());
                std::vector<point_2> upper, lower;
                auto left = points_s.begin()->x;
                auto right = points_s.rbegin()->x;
                upper_hull.get_hull(upper, left, right);
                lower_hull.get_hull(lower, left, right);
                std::reverse(upper.begin(), upper.end());
                std::for_each(lower.begin(), lower.end(), [](point_2 &p) { p.y *= -1; });
                if (*lower.rbegin() == *upper.begin()) lower.pop_back();
                if (*lower.begin() == *upper.rbegin()) upper.pop_back();
                for (auto p : upper) hull_v.push_back(p);
                for (auto p : lower) hull_v.push_back(p);
                changed = false;
            }
            return std::make_pair(hull_v.begin(), hull_v.end());
        }

        const std::pair<vect_it, vect_it> get_all_points() {
            if (changed) {
                points_v.clear();
                for (const point_2 &p : points_s) points_v.push_back(p);
            }
            return std::make_pair(points_v.begin(), points_v.end());
        }

    private:
        rbtree upper_hull, lower_hull;
        std::set<point_2> points_s;
        std::vector<point_2> points_v;
        std::vector<point_2> hull_v;
        bool changed;
    };
}
