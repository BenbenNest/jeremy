cmake_minimum_required(VERSION 3.4.1)

set(SOURCE_SET myjni.c helloworld.cpp)
add_library(myjni SHARED ${SOURCE_SET})

# Include libraries needed for hello-jni lib
target_link_libraries(myjni android log)
