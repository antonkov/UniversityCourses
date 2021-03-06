cmake_minimum_required(VERSION 2.8)

project(cg-test)

find_package(GTest REQUIRED)
include_directories(${GTEST_INCLUDE_DIR})

find_package(GMP REQUIRED)
include_directories(${GMP_INCLUDE_DIR})

find_package(Boost COMPONENTS system chrono random REQUIRED)

include_directories(${Boost_INCLUDE_DIRS})
link_directories(${Boost_LIBRARYDIR})

set(SOURCES
    dynamic_hull.cpp
   orientation.cpp
   has_intersection.cpp
   contains.cpp
   convex_hull.cpp
   dynamic_convex_hull.cpp
   convex.cpp
    douglas_peucker.cpp
    diametr_of_set.cpp
)

add_executable(cg-test ${SOURCES})
target_link_libraries(cg-test ${GTEST_BOTH_LIBRARIES} ${GMP_LIBRARIES} ${Boost_LIBRARIES})

file(GLOB_RECURSE HEADERS "*.h")
add_custom_target(cg_test_headers SOURCES ${HEADERS})
