
set(JAVAHOME /home/imhammer/Documentos/CUSTOM_JVM/jdk-21-windows)
set(THIS_DIRECTORY ${CMAKE_SOURCE_DIR}/ihacinput_windows)

set(CMAKE_CXX_COMPILER "x86_64-w64-mingw32-g++")
set(CMAKE_C_COMPILER "x86_64-w64-mingw32-gcc")
set(CMAKE_SHARED_LIBRARY_PREFIX "lib")
set(CMAKE_SHARED_LIBRARY_SUFFIX ".dll")

add_library(ihacinput_win SHARED ${THIS_DIRECTORY}/src/ihacinput.cpp)

target_include_directories(ihacinput_win PUBLIC /usr/x86_64-w64-mingw32/include)
target_include_directories(ihacinput_win PUBLIC ${CMAKE_SOURCE_DIR}/public)
target_include_directories(ihacinput_win PUBLIC ${JAVAHOME}/include)
target_include_directories(ihacinput_win PUBLIC ${JAVAHOME}/include/win32)