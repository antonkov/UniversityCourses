#pragma once

#include <cg/primitives/point.h>
#include <cg/primitives/segment.h>
#include <cmath>

namespace cg {
    inline double distance_sqr(point_2 const & p, point_2 const & q)
    {
        return (p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y);
    }

    inline double distance(point_2 const & p, point_2 const & q)
    {
        return sqrt(distance_sqr(p, q));
    }

    inline double distance(point_2 const & p, segment_2 const & seg)
    {
        double len = distance(seg[0], seg[1]);
        double projection = (seg[1].x - seg[0].x) * (p.x - seg[0].x) + (seg[1].y - seg[0].y) * (p.y - seg[0].y);
        if (projection < 0) return distance(p, seg[0]);
        else if (projection > len) return distance(p, seg[1]);
        else
        {
            double res = std::abs((seg[1].x - seg[0].x) * (p.y - seg[0].y) - (p.x - seg[0].x) * (seg[1].y - seg[0].y));
            res /= len;
            return res;
        }
    }
}
