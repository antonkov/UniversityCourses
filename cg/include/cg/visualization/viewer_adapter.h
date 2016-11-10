#pragma once

#include "viewer.h"

namespace cg {
namespace visualization
{
   struct viewer_adapter : viewer_type
   {
      virtual void print  (printer_type & ) const {}

      virtual void set_window(QWidget * wnd) { wnd_ = wnd; }

      virtual bool on_key         (int)                 { return false; }
      virtual bool on_double_click(point_2f const &)    { return false; }
      virtual bool on_move        (point_2f const &)    { return false; }
      virtual bool on_press       (point_2f const &)    { return false; }
      virtual bool on_release     (point_2f const &)    { return false; }

   protected:
      QWidget * get_wnd() const { return wnd_; }

   private:
      QWidget * wnd_;
   };
}}
