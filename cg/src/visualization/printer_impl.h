#pragma once

#include "cg/visualization/viewer.h"

namespace cg {
namespace visualization
{
   struct printer_impl : printer_type
   {
      stream_type& corner_stream();
      stream_type& global_stream(point_2f const & pt);

      printer_impl(   boost::function<void (point_2i const &, const char *)>    const & draw_string_corner,
                      boost::function<void (point_2f const &, const char *)>    const & draw_string_global);

   private:
      boost::function<void (point_2f const &, const char *)>    draw_string_global_;
      int corner_stream_height_indent_;

      boost::scoped_ptr<stream_type> corner_stream_;
      boost::scoped_ptr<stream_type> global_stream_;
   };
}}
