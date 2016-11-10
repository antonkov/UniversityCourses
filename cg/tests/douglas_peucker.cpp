#include <gtest/gtest.h>

#include <boost/assign/list_of.hpp>

#include <cg/primitives/point.h>
#include <cg/primitives/contour.h>
#include <cg/operations/convex.h>
#include <cg/convex_hull/graham.h>
#include <cg/operations/douglas-peucker.h>

#include "random_utils.h"

#include <vector>
#include <iterator>

TEST(simplify, test1)
{
    std::vector<cg::point_2> pts;
    pts.push_back(cg::point_2(0, 0));
    pts.push_back(cg::point_2(1, 0));
//    pts.push_back(cg::point_2(1, 1));
//    pts.push_back(cg::point_2(2, 1));
//    pts.push_back(cg::point_2(2, 0));
//    pts.push_back(cg::point_2(3, 0));

    std::vector<cg::point_2> result;
    simplify(pts.begin(), pts.end(), std::back_inserter(result), 1e-8);
    EXPECT_TRUE(pts.size() == result.size());
}
