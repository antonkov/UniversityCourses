#include <gtest/gtest.h>

#include <boost/assign/list_of.hpp>

#include <cg/primitives/point.h>
#include <cg/operations/diametr_of_set.h>

#include <vector>
#include <iterator>

TEST(diametr, test1)
{
    std::vector<cg::point_2> pts;
    pts.push_back(cg::point_2(0, 0));
    pts.push_back(cg::point_2(1, 0));
    pts.push_back(cg::point_2(0.5, 0.5));

    auto result = cg::diametr_of_set(pts.begin(), pts.end());
    EXPECT_TRUE(1);
}
