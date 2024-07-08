
#include <cstring>
#include <types.h>

#ifndef _IHACINPUT_
#define _IHACINPUT_
#endif

#define MOUSE_BTN_LEFT   1
#define MOUSE_BTN_RIGHT  2
#define MOUSE_BTN_MIDDLE 3

namespace ihacinput
{
    using error = struct {
        uint16 errorid;
        const char* message;
    };

    uint32 clicks = 0;
    uint16  buttonType = MOUSE_BTN_LEFT;
    error errorim = {0};
} // namespace ihacinput


bool ihacinput_init();
void ihacinput_mouse_input(uint16);

void ihacinput_set_error(uint16 errorid, const char* message)
{
    ihacinput::errorim.errorid = errorid;
    ihacinput::errorim.message = message;
}

uint16 ihacinput_get_error()
{
    return ihacinput::errorim.errorid;
}

const char* ihacinput_get_error_message()
{
    return ihacinput::errorim.message;
}