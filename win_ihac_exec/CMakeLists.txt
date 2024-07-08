cmake_minimum_required(VERSION 3.25)

project(win_ihac_exec VERSION 1.0 DESCRIPTION "Usado para executar o IHAutoClicker no windows")

set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_COMPILER "x86_64-w64-mingw32-g++")
set(CMAKE_CXX_FLAGS "-static-libgcc -static-libstdc++")

set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/dist)

add_executable(ihautoclicker ${CMAKE_SOURCE_DIR}/src/main.cpp)
