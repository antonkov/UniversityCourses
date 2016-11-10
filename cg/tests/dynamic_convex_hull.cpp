#include <gtest/gtest.h>
#include <boost/assign/list_of.hpp>
#include <boost/chrono.hpp>

#include <cg/convex_hull/naive_dynamic.h>
#include <cg/convex_hull/dynamic_hull.h>

#include "random_utils.h"

template <class FwdIter>
bool is_convex_hull(FwdIter ab, FwdIter ae, FwdIter hb, FwdIter he)
{
   for (auto i = hb; i != he; i++) {
       if (std::find(ab, ae, *i) == ae) return false;
   }
   for (FwdIter b = ab; b != ae; b++)
   {
      for (FwdIter t = boost::prior(he), s = hb; s != he; t = s++) {
         switch (orientation(*t, *s, *b))
         {
         case cg::CG_RIGHT:
            return false;

         case cg::CG_COLLINEAR:
            if(!collinear_are_ordered_along_line(*t, *b, *s))
            {
               return false;
            }

         case cg::CG_LEFT:
            continue;
         }
      }
   }

   return true;
}

TEST(dynamic_convex_hull, without_deleting1)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(0, 0))
                              (point_2(1, 0))
                              (point_2(0, 1))
                              (point_2(2, 0))
                              (point_2(0, 2))
                              (point_2(3, 0));
   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, without_deleting2)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(0, 0))
                              (point_2(1, 1))
                              (point_2(2, 2))
                              (point_2(3, 3))
                              (point_2(4, 4))
                              (point_2(5, 5));

   cg::dynamic_hull dh;

   
   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, without_deleting3)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(-15.2939, -91.2349))
                              (point_2(73.6514, -84.1031))
                              (point_2(46.0487, -42.9297))
                              (point_2(21.95, 48.3017))
                              (point_2(-6.31702, -43.6781));

   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, without_deleting4)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(92.5535, 63.9831))
                              (point_2(48.2666, -2.07253))
                              (point_2(-87.6796, 74.2083))
                              (point_2(-41.6394, 42.1274))
                              (point_2(4.02735, 3.24309));

   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, without_deleting5)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(-55, -55))
                              (point_2(53, 65))
                              (point_2(67, -27))
                              (point_2(-10, -46))
                              (point_2(12, -30));

   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, without_deleting6)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(17, -19))
                              (point_2(61, 94))
                              (point_2(-29, 38))
                              (point_2(-9, -42))
                              (point_2(-85, 40))
                              (point_2(-24, -7))
                              (point_2(-78, 95));
   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }
   auto st_h = dh.get_hull().first;
   auto en_h = dh.get_hull().second;
   bool res = is_convex_hull(pts.begin(), pts.end(), st_h, en_h);

   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, with_deleting1)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(0, 0))
                              (point_2(3, 0))
                              (point_2(4, 2))
                              (point_2(2, 2))
                              (point_2(2, 4))
                              (point_2(-1, 2))
                              (point_2(1, 1))
                              (point_2(0, 1));

   std::vector<point_2> not_removed = boost::assign::list_of(point_2(0, 0))
                                      (point_2(3, 0))
                                      (point_2(2, 2))
                                      (point_2(1, 1))
                                      (point_2(0, 1));
   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   dh.remove_point(point_2(4, 2));
   dh.remove_point(point_2(-1, 2));
   dh.remove_point(point_2(2, 4));
   EXPECT_TRUE(is_convex_hull(not_removed.begin(), not_removed.end(), dh.get_hull().first, dh.get_hull().second));
}

TEST(dynamic_convex_hull, with_deleting2)
{
   using cg::point_2;

   std::vector<point_2> pts = boost::assign::list_of(point_2(81, 80))
                              (point_2(-80, -41))
                              (point_2(71, 90))
                              (point_2(-64, 17));

   std::vector<point_2> not_removed = boost::assign::list_of(point_2(81, 80))
                              (point_2(71, 90))
                              (point_2(-64, 17));
   cg::dynamic_hull dh;

   for (point_2 p : pts)
   {
      dh.add_point(p);
   }

   dh.remove_point(point_2(-80, -41));
   EXPECT_TRUE(is_convex_hull(not_removed.begin(), not_removed.end(), dh.get_hull().first, dh.get_hull().second));
}


TEST(dynamic_convex_hull, uniform_without_deleting)
{
   using cg::point_2;
   std::vector<point_2> pts = uniform_points(100000);
   cg::dynamic_hull dh;

   boost::chrono::high_resolution_clock::time_point start = boost::chrono::high_resolution_clock::now();
   for (point_2 p : pts)
   {
       dh.add_point(p);
   }
   boost::chrono::nanoseconds ns = boost::chrono::high_resolution_clock::now() - start;
   std::cout << ns / pts.size() << std::endl;

   auto ans = dh.get_hull();
   auto st_h = ans.first;
   auto en_h = ans.second;
   EXPECT_TRUE(is_convex_hull(pts.begin(), pts.end(), st_h, en_h));
}

TEST(dynamic_convex_hull, uniform_with_deleting)
{
   using cg::point_2;

   std::vector<point_2> pts = uniform_points(100000), after_deleting;
   cg::dynamic_hull dh;
   std::set<point_2> added;

   boost::chrono::high_resolution_clock::time_point start = boost::chrono::high_resolution_clock::now();
   for (point_2 p : pts)
   {
      if (rand() % 2 || added.empty())
      {
         dh.add_point(p);
         added.insert(p);
      }
      else
      {
         int num = abs(rand()) % added.size();
         num %= 3;
         auto it = added.begin();
         std::advance(it, num);
         auto q = *it;

         dh.remove_point(q);

         added.erase(q);
      }
   }
   boost::chrono::nanoseconds ns = boost::chrono::high_resolution_clock::now() - start;
   std::cout << ns / pts.size() << std::endl;
   auto hull_it = dh.get_hull();
   for (auto p : pts) EXPECT_TRUE((std::find(hull_it.first, hull_it.second, p) != hull_it.second) == dh.contains(p));
   for (auto p : added) after_deleting.push_back(p);
   EXPECT_TRUE(is_convex_hull(after_deleting.begin(), after_deleting.end(), hull_it.first, hull_it.second));
}
