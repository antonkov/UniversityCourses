set(VISUALIZATION_SOURCES 
   visualization/drawer_impl.cpp
   visualization/printer_impl.cpp
   visualization/main_window.cpp
   visualization/visualization.cpp
   visualization/draw_util.cpp
)

include_directories(.)

add_library(visualization
   ${VISUALIZATION_SOURCES}
)

target_link_libraries(visualization ${QT_LIBRARIES} ${OPENGL_LIBRARIES})
