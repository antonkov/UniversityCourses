#include "stdafx.h"

#include "printer_impl.h"

#include "cg/io/point.h"
#include "cg/io/segment.h"

namespace cg {
namespace visualization
{
   struct stream_impl : stream_type
   {
      stream_impl(boost::function<void (const char *)> const & write)
         : write_(write)
      {}

#define PRINT(type) \
      stream_type & operator << (type t) \
      { \
          ss_ << t; \
          return *this; \
      }
      PRINT(const char *)
      PRINT(std::string const &)
      PRINT(size_t)
      PRINT(point_2f const &)
      PRINT(segment_2f const &)
#undef PRINT
      stream_type & operator << (manipulator_type f)
      {
         f(*this);
         return *this;
      }

      ~stream_impl() { flush(); }

      void end_line() {}

   protected:
      void flush()
      {
         write_(ss_.str().c_str());
         ss_.str("");
      }

   private:
      boost::function<void (const char *)> write_;
      std::ostringstream ss_;
   };

   struct extended_stream_impl : stream_impl
   {
      extended_stream_impl(boost::function<void (const char *)> const & write)
         : stream_impl(write)
      {}

      void end_line() { flush(); }
   };

   void endl(stream_type & out) { out.end_line(); }

   printer_impl::printer_impl(boost::function<void (const point_2i &, const char *)> const & draw_string_corner,
                              boost::function<void (const point_2f &, const char *)> const & draw_string_global)
      : draw_string_global_(draw_string_global)
      , corner_stream_height_indent_(15)
      , corner_stream_(
              new extended_stream_impl(
                  [draw_string_corner, this] (const char * str) {
                      draw_string_corner(point_2i(10, corner_stream_height_indent_), str);
                      corner_stream_height_indent_ += 15;
                  }
              ))
   {}

   stream_type & printer_impl::corner_stream()
   {
      return *corner_stream_;
   }

   stream_type & printer_impl::global_stream(point_2f const & pt)
   {
      global_stream_.reset(
         new stream_impl([pt, this] (const char * str) {
            draw_string_global_(pt, str);
         })
      );

      return *global_stream_;
   }
}}
