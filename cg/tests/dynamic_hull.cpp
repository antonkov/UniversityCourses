#include <gtest/gtest.h>

#include <cg/convex_hull/dynamic_hull.h>

using namespace cg;

TEST(dynamic_hull, test1) {
    dynamic_hull dh;
    dh.add_point(point_2(0, 4));
    dh.add_point(point_2(4, 0));
    dh.add_point(point_2(8, 4));
    dh.add_point(point_2(5, 3));
    auto res =dh.get_hull();
    for (auto i = res.first; i != res.second; i++) std::cout << i->x << " " << i->y << std::endl;
    EXPECT_TRUE(res.second - res.first == 3);
}

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
