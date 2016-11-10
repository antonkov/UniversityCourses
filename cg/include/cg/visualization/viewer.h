#pragma once

#include <QColor>

#include "cg/primitives/point.h"
#include "cg/primitives/segment.h"

namespace cg {
namespace visualization
{
   using cg::point_2f;
   using cg::point_2i;
   using cg::segment_2f;

   struct stream_type
   {
      typedef void (* manipulator_type)(stream_type&);

#define PRINT(type) virtual stream_type & operator << (type) = 0;
      PRINT(const char *)
      PRINT(std::string const &)
      PRINT(size_t)
      PRINT(point_2f const &)
      PRINT(segment_2f const &)
      PRINT(manipulator_type)
#undef PRINT

      virtual void end_line() = 0;

      virtual ~stream_type() {};
   };

   void endl(stream_type& s);

   struct drawer_type
   {
      virtual void set_color  (QColor const &)                                       = 0;
      virtual void draw_line  (segment_2f const &,                 float width  = 1) = 0;
      virtual void draw_line  (point_2f const &, point_2f const &, float width  = 1) = 0;
      virtual void draw_point (point_2f const &,                   float radius = 1) = 0;

      virtual ~drawer_type() {}
   };

   struct printer_type
   {
      virtual stream_type& corner_stream()                      = 0;
      virtual stream_type& global_stream(point_2f const & pos)  = 0;

      virtual ~printer_type() {}
   };

   struct viewer_type
   {
      virtual void draw   (drawer_type &  )           const   = 0;
      virtual void print  (printer_type & )           const   = 0;

      virtual void set_window(QWidget *)                      = 0;

      virtual bool on_key         (int key)                   = 0;
      virtual bool on_double_click(point_2f const & pos)      = 0;
      virtual bool on_move        (point_2f const & pos)      = 0;
      virtual bool on_press       (point_2f const & pos)      = 0;
      virtual bool on_release     (point_2f const & pos)      = 0;

      virtual ~viewer_type() {}
   };

   void run_viewer(viewer_type * viewer, const char * title);
}}
