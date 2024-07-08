
#include <iostream>

#include <ihacinput.h>
#include <jni_ihautoclicker.h>

#include <windows.h>
#include <winuser.h>

bool ihacinput_init()
{

    return true;
}

void ihacinput_mouse_input(uint16 mouseButton)
{
    uint btnUp = -1, btnDown = -1;

    INPUT input = {0};
    input.type = INPUT_MOUSE;

    switch(mouseButton)
    {
        case MOUSE_BTN_MIDDLE:        btnUp = MOUSEEVENTF_MIDDLEUP, btnDown = MOUSEEVENTF_MIDDLEDOWN; break;
        case MOUSE_BTN_RIGHT:         btnUp = MOUSEEVENTF_RIGHTUP,  btnDown = MOUSEEVENTF_RIGHTDOWN;  break;
        default: case MOUSE_BTN_LEFT: btnUp = MOUSEEVENTF_LEFTUP,   btnDown = MOUSEEVENTF_LEFTDOWN;   break;
    }

    if (btnUp == -1 || btnDown == -1) {
        std::cout << "Error, btnUp or btnDown is undefined!" << std::endl;
        return;
    }

    // Mouse Down
    input.mi.dwFlags = btnDown;
    SendInput(1, &input, sizeof(INPUT));

    // Atraso necessario
    Sleep(10);

    // Mouse Up
    input.mi.dwFlags = btnUp;
    SendInput(1, &input, sizeof(INPUT));
}

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    if (ihacinput_init()) {
        std::cout << "Library loaded successfly!" << std::endl;
    } else {
        std::cout << "Error on loading ihacinput library: " << ihacinput_get_error() << ihacinput_get_error_message() << std::endl;
    }

    return JNI_VERSION_21;
}

//Dispara o ihacinput_mouse_input
JNIEXPORT void JNICALL Java_com_github_imhammer_ihautoclicker_App_nativeMouseClick(JNIEnv* env, jobject object, jint button)
{
    ihacinput_mouse_input((uint16)button);
}