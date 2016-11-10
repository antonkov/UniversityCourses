#pragma once

#include "cg/visualization/viewer.h"

#include "drawer_impl.h"

using cg::point_2f;
using cg::point_2i;

using namespace cg::visualization;

struct main_window_t : QGLWidget
{
   main_window_t(viewer_type * viewer);

private:
   void initializeGL();
   void resizeGL(int, int);
   void paintGL();

   void wheelEvent(QWheelEvent *);
   void mousePressEvent(QMouseEvent *);
   void mouseDoubleClickEvent(QMouseEvent *);
   void mouseMoveEvent(QMouseEvent *);
   void mouseReleaseEvent(QMouseEvent *);
   void keyReleaseEvent(QKeyEvent *);

   void resize_impl(int, int);
   point_2f screen_to_global(QPoint const & screen_pos) const;

private:
   void draw_string(point_2i const & pos, const char * s);
   void draw_string_global(point_2f const & pos, const char * s);

private:
   viewer_type * viewer_;

   point_2f center_;
   point_2f size_;
   point_2f current_pos_;
   double  zoom_;
   optional<point_2f> start_point_;
   drawer_impl drawer_;
};
