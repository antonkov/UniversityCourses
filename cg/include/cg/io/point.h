#pragma once

#include "io_utils.h"
#include "cg/primitives/point.h"

namespace cg
{
   template <class Scalar>
   std::istream & operator >> (std::istream & in, point_2t<Scalar> & pt)
   {
      using io::skip_char;

      return skip_char(skip_char(skip_char(in, '(') >> pt.x, ',') >> pt.y, ')');
   }

   template <class Scalar>
   std::ostream & operator << (std::ostream & out, point_2t<Scalar> const & pt)
   {
      out << "(" << pt.x << ", " << pt.y << ")";
      return out;
   }
}
