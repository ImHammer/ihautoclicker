
find_library(EVDEVLIB evdev)

if (EVDEVLIB-NOTFOUND)
    message(FATAL_ERROR "Lib EVDEV not found!")
endif()

set(THIS_DIRECTORY ${CMAKE_SOURCE_DIR}/ihacinput_linux)

add_library(ihacinput_lin SHARED ${THIS_DIRECTORY}/src/ihacinput.cpp)

target_link_libraries(ihacinput_lin PUBLIC evdev)

target_include_directories(ihacinput_lin PUBLIC ${CMAKE_SOURCE_DIR}/public)
target_include_directories(ihacinput_lin PUBLIC ${JAVAHOME}/include)
target_include_directories(ihacinput_lin PUBLIC ${JAVAHOME}/include/linux)

target_include_directories(ihacinput_lin PUBLIC /usr/include/libevdev-1.0)
target_include_directories(ihacinput_lin PUBLIC /usr/include/libevdev-1.0/libevdev)