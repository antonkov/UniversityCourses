#include <vector>
#include <stack>

#include <QColor>
#include <QApplication>

#include <boost/optional.hpp>

#include "cg/visualization/viewer_adapter.h"
#include "cg/visualization/draw_util.h"

#include "cg/io/point.h"

using cg::point_2f;
using cg::vector_2f;

struct sample_viewer : cg::visualization::viewer_adapter
{
   void draw(cg::visualization::drawer_type & drawer) const
   {
      drawer.set_color(Qt::white);
      for (size_t l = 0, lp = points_.size() - 1; l != points_.size(); lp = l++)
         drawer.draw_line(points_[lp], points_[l]);

      if (!points_.empty() && current_point_)
      {
         drawer.set_color(Qt::green);
         drawer.draw_line(points_.back(), *current_point_);
         drawer.draw_line(points_.front(), *current_point_);
      }
   }

   void print(cg::visualization::printer_type & p) const
   {
      p.corner_stream() << "double-click to add point" << cg::visualization::endl
                        << "or press mouse lbutton with CTRL key" << cg::visualization::endl
                        << "points count: " << points_.size() << cg::visualization::endl;

      foreach (point_2f const & pt, points_)
         p.global_stream(pt + vector_2f(5, 0)) << pt;
   }

   bool on_double_click(const point_2f & p)
   {
      points_.push_back(p);
      return true;
   }

   bool on_press(const point_2f & p)
   {
      if (current_point_)
         return false;

      current_point_ = p;
      return true;
   }

   bool on_release(const point_2f & p)
   {
      if (!current_point_)
         return false;


      points_.push_back(*current_point_);
      current_point_.reset();
      return true;
   }

   bool on_move(const point_2f & p)
   {
      if (!current_point_)
         return false;

      current_point_ = p;
      return true;
   }

private:
   std::vector<point_2f> points_;
   boost::optional<point_2f> current_point_;
};

int main(int argc, char ** argv)
{
   QApplication app(argc, argv);
   sample_viewer viewer;
   cg::visualization::run_viewer(&viewer, "test viewer");
}
