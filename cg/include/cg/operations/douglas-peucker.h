#pragma once

#include <cg/primitives/segment.h>
#include <cg/operations/orientation.h>
#include <cg/operations/distance.h>

#include <iterator>

#include <boost/utility.hpp>

namespace cg {
    template <typename BidIter, typename OutputIter>
    OutputIter simplify(BidIter begin, BidIter end, OutputIter out, double eps)
    {
        out = simplify_imp(begin, boost::prior(end), out, eps);
        *out++ = *boost::prior(end);
        return out;
    }

    template <typename BidIter, typename OutputIter>
    OutputIter simplify_imp(BidIter begin, BidIter end, OutputIter out, double eps)
    {
        if (begin == end) return out;
        double max_dist = 0;
        BidIter farthest_point = begin;
        segment_2 seg(*begin, *end);
        for (BidIter it = boost::next(begin); it != end; ++it)
        {
            double cur_dist = distance(*it, seg);
            if (cur_dist > max_dist)
            {
                max_dist = cur_dist;
                farthest_point = it;
            }
        }
        if (max_dist <= eps || farthest_point == begin)
        {
            *out++ = *begin;
            return out;
        } else
        {
            OutputIter inter = simplify_imp(begin, farthest_point, out, eps);
            return simplify_imp(farthest_point, end, inter, eps);
        }
    }
}
