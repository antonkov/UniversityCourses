cmake_minimum_required(VERSION 2.6 FATAL_ERROR)

project(viewer-example)

add_executable(viewer-example viewer.cpp)
target_link_libraries(viewer-example visualization)
