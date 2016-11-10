#pragma once

#include "cg/primitives/segment.h"
#include "point.h"

namespace cg
{
   template <class Scalar>
   std::istream & operator >> (std::istream & in, segment_2t<Scalar> & seg)
   {
      using io::skip_char;

      return skip_char(skip_char(skip_char(in, '[') >> seg[0], ',') >> seg[1], ']');
   }

   template <class Scalar>
   std::ostream & operator << (std::ostream & out, segment_2t<Scalar> const & seg)
   {
      out << "[" << seg[0] << ", " << seg[1] << "]";
      return out;
   }
}
