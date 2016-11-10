#pragma once

#include <gmpxx.h>

#include <cg/primitives/segment.h>
#include <cg/operations/distance.h>
#include <cg/convex_hull/graham.h>
#include <cg/common/common.h>

#include <iterator>
#include <algorithm>

#include <boost/utility.hpp>
#include <boost/numeric/interval.hpp>

typedef boost::numeric::interval_lib::unprotect<boost::numeric::interval<double> >::type interval;

namespace cg {
    template <typename T>
    T vec_mul(point_2t<T> const & p, point_2t<T> const & q, point_2t<T> const & r)
    {
        return (q.x - p.x) * (r.y - p.y) - (r.x - p.x) * (q.y - p.y);
    }

    bool nearer(point_2 const & p, point_2 const & q, point_2 const & r1, point_2 const & r2)
    {
        double vec1 = abs(vec_mul(p, q, r1));
        double vec2 = abs(vec_mul(p, q, r2));
        double eps = (vec1 + vec2) * std::numeric_limits<double>::epsilon() * 8;
        if (abs(vec1 - vec2) <= eps) return vec1 < vec2;

        interval vec1i(abs(vec_mul<interval>(p, q, r1)));
        interval vec2i(abs(vec_mul<interval>(p, q, r2)));
        if (vec1i.lower() >= vec2i.upper()) return false;
        if (vec1i.upper() < vec2i.lower()) return true;

        mpq_class vec1q(abs(vec_mul<mpq_class>(p, q, r1)));
        mpq_class vec2q(abs(vec_mul<mpq_class>(p, q, r2)));
        return vec1q < vec2q;
    }

    template <typename BidIter>
    std::pair<BidIter, BidIter> diametr_of_set(BidIter begin, BidIter end)
    {
        if (end - begin <= 1) return std::make_pair(begin, begin);
        typedef typename BidIter::value_type point_type;
        std::vector<point_type> v(begin, end);
        v.erase(graham_hull(v.begin(), v.end()), v.end());
        std::pair<point_type, point_type> result;
        double best_dist = -1;

        auto p = v.begin(), q = std::next(v.begin());
        auto r = q;
        do
        {
            while (nearer(*p, *q, *r, *(r == std::prev(v.end()) ? v.begin() : std::next(r))))
            {
                r++;
                if (r == v.end()) r = v.begin();
            }
            if (cg::make_max(best_dist, distance(*r, *p)))
            {
                result.first = *r; result.second = *p;
            }
            if (cg::make_max(best_dist, distance(*r, *q)))
            {
                result.first = *r; result.second = *q;
            }
            p++; q++;
            if (p == v.end()) p = v.begin();
            if (q == v.end()) q = v.begin();
        } while (p != v.begin());
        return std::make_pair(std::find(begin, end, result.first), std::find(begin, end, result.second));
    }
}
