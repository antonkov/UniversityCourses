TEMPLATE = lib 
TARGET = visualization
CONFIG += static

CONFIG += QtGui
QT += opengl

OBJECTS_DIR = bin

DEPENDPATH += src \
              headers/common \
              headers/geom/primitives \
              headers/io \
              headers/visualization \
              src/io \
              src/visualization \

INCLUDEPATH += headers src

QMAKE_CXXFLAGS = -std=c++0x -Wall

CONFIG += precompile_header
PRECOMPILED_HEADER = stdafx.h

# Input
HEADERS += src/stdafx.h \
           headers/common/range.h \
           headers/io/point.h \
           headers/io/range.h \
           headers/io/rectangle.h \
           headers/io/segment.h \
           headers/io/vector.h \
           headers/visualization/draw_util.h \
           headers/visualization/viewer.h \
           src/visualization/drawer_impl.h \
           src/visualization/main_window.h \
           src/visualization/printer_impl.h \
           headers/geom/primitives/contour.h \
           headers/geom/primitives/point.h \
           headers/geom/primitives/range.h \
           headers/geom/primitives/rectangle.h \
           headers/geom/primitives/segment.h \
           headers/geom/primitives/vector.h \

SOURCES += src/io/primitives.cpp \
           src/visualization/drawer_impl.cpp \
           src/visualization/printer_impl.cpp \
           src/visualization/main_window.cpp \
           src/visualization/visualization.cpp \
           src/visualization/draw_util.cpp \

