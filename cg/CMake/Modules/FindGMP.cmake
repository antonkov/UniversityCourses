# - Try to find the GMP library
# Once done this will define
#
#  GMP_FOUND - system has GMP
#  GMP_INCLUDE_DIR - the GMP include directory
#  GMP_LIBRARIES - The libraries needed to use GMP

if(GMP_INCLUDE_DIR AND GMP_LIBRARIES)
   set(GMP_FOUND TRUE)
else(GMP_INCLUDE_DIR AND GMP_LIBRARIES)

find_path(GMP_INCLUDE_DIR gmp.h
   /usr/include
   /usr/local/include
)

find_library(GMP_LIBRARIES names gmp 
   PATHS
   /usr/lib
   /usr/lib64
   /usr/local/lib
   /usr/local/lib64
)

if(GMP_INCLUDE_DIR AND GMP_LIBRARIES)
   set(GMP_FOUND TRUE)
endif(GMP_INCLUDE_DIR AND GMP_LIBRARIES)


if(GMP_FOUND)
   if(NOT GMP_FIND_QUIETLY)
      message(STATUS "Found GMP: ${GMP_LIBRARIES}")
   endif(NOT GMP_FIND_QUIETLY)
else(GMP_FOUND)
   if(GMP_FIND_REQUIRED)
      message(FATAL_ERROR "could NOT find gmp")
   endif(GMP_FIND_REQUIRED)
endif(GMP_FOUND)

MARK_AS_ADVANCED(GMP_INCLUDE_DIR GMP_LIBRARIES)

endif(GMP_INCLUDE_DIR AND GMP_LIBRARIES)
