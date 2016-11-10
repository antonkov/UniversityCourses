#pragma once

#include <iostream>

namespace cg {
namespace io {

   inline std::istream & skip_char(std::istream & in, char ch)
   {
      char c;
      while ((in >> c) && (c != ch));
      return in;
   }

}}
