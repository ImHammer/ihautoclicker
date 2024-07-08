cmake_minimum_required(VERSION 3.29)
project(ihautoclickerlib_project VERSION 1.0 DESCRIPTION "library for IHAutoClicker")

set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_FLAGS "-static-libgcc -static-libstdc++")

set(JAVAHOME $ENV{JAVA_HOME})
set(BINARIES_OUTPUT ${CMAKE_SOURCE_DIR}/dist/x86_64)

if (NOT DEFINED JAVAHOME)
    message(FATAL_ERROR "Variavel de ambiente $JAVA_HOME não encontrada")
endif()

option(X32 "Make in 32 Bits" OFF)

if (X32)
    set(BINARIES_OUTPUT ${CMAKE_SOURCE_DIR}/dist/x32)
    
    set(CMAKE_C_FLAGS "-m32")
    set(CMAKE_CXX_FLAGS "-m32")

    message("Compilando para x32")
endif()

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${BINARIES_OUTPUT}/libs)
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${BINARIES_OUTPUT})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${BINARIES_OUTPUT})

add_subdirectory(ihacinput_linux)
add_subdirectory(ihacinput_windows)