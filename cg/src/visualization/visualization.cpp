#include "stdafx.h"

#include "cg/visualization/viewer.h"
#include "main_window.h"

namespace cg {
namespace visualization
{
   void run_viewer(viewer_type * viewer, const char * title)
   {
      main_window_t * wnd = new main_window_t(viewer);
      viewer->set_window(wnd);
      wnd->setWindowTitle(title);
      wnd->show();
      qApp->exec();
   }
}}
