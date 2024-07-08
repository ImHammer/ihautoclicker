
#include <iostream>
#include <thread>
#include <vector>
#include <unistd.h>

#include <stdlib.h>
#include <sys/types.h>
#include <string.h>

#include "jni_ihautoclicker.h"
#include "types.h"
#include "nativeinput.h"

#ifndef _IHAutoClicker_
#define _IHAutoClicker_

namespace ihautoclicker
{
    void init();
    void executeMouseInput(uint);
}; // namespace ihautoclicker

#endif
