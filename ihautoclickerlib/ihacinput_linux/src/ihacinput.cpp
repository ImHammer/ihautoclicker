
// [STANDARD]
#include <iostream>

#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

#include <vector>
#include <thread>
#include <mutex>
#include <regex>
#include <filesystem>
#include <condition_variable>
#include <chrono>

// [LINUX]
#include <linux/input.h>
#include <linux/input-event-codes.h>

// [ME]
#include <jni_ihautoclicker.h>
#include <ihacinput.h>
#include <types.h>

// [EVDEV]
#include <libevdev-1.0/libevdev/libevdev.h>
#include <libevdev-1.0/libevdev/libevdev-uinput.h>

/// CONSTANTES
#define DEVICE_INPUT_PATH "/dev/input"

/// ERRORS
#define INIT_ERROR_REQSUDO 0

#define ERROR_DEVICE_INPUT_PATH_NOTFOUND 1000
#define ERROR_DEVICE_CANNOT_STARTED      1001
#define ERROR_DEVICE_CREATE_UINPUT       1002
#define ERROR_DEVICE_MERGE_UINPUT        1003

const std::vector<uint16> requiredKeys({
    BTN_LEFT,
    BTN_RIGHT,
    BTN_MIDDLE,
    BTN_SIDE,
    BTN_EXTRA,
    BTN_FORWARD,
    BTN_BACK,
    BTN_TASK
});

void running_real_inputs(struct libevdev* const evdev, const struct libevdev_uinput* evdevUInput)
{
    struct input_event ev;

    int rc = 0;
    do
    {
        rc = libevdev_next_event(evdev, LIBEVDEV_READ_FLAG_NORMAL, &ev);
        if (rc == 0)
        {
            libevdev_uinput_write_event(evdevUInput, ev.type, ev.code, ev.value);
        }
    } while (rc == 1 || rc == 0 || rc == -EAGAIN);
}

void running_fake_inputs(struct libevdev* const evdev, const struct libevdev_uinput* evdevUInput)
{
    while(true)
    {
        if (ihacinput::clicks > 0) {
            libevdev_uinput_write_event(evdevUInput, EV_SYN, SYN_REPORT, 0);
            libevdev_uinput_write_event(evdevUInput, EV_KEY, ihacinput::buttonType, 1);
            libevdev_uinput_write_event(evdevUInput, EV_SYN, SYN_REPORT, 0);
            libevdev_uinput_write_event(evdevUInput, EV_KEY, ihacinput::buttonType, 0);
            libevdev_uinput_write_event(evdevUInput, EV_SYN, SYN_REPORT, 0);

            std::this_thread::sleep_for(std::chrono::milliseconds(20));

            ihacinput::clicks--;
        }
    }
}

// Usada para iniciar algumas partes necessárias para o funcionament do AC
// Ex: Encontrar o Mouse, Mesclar, etc...
bool ihacinput_init()
{
    if (geteuid()) {
        ihacinput_set_error(INIT_ERROR_REQSUDO, "Super user permission is required to start IHACINPUTLIB.");
        return false;
    }

    /// Obtendo o device id do mouse principal ///
    if (!std::filesystem::is_directory(DEVICE_INPUT_PATH))
    {
        ihacinput_set_error(ERROR_DEVICE_INPUT_PATH_NOTFOUND, "Unable to find the recurring directory for the device entries.");
        return false;
    }

    struct libevdev* evdev;
    struct libevdev_uinput* evdevUInput;

    bool founded = false;

    for (const auto& entry : std::filesystem::directory_iterator(DEVICE_INPUT_PATH))
    {

        if (((std::string)entry.path()).find("event") == -1) continue;
        if (libevdev_new_from_fd(open(((std::string)entry.path()).c_str(), O_RDONLY | O_NONBLOCK), &evdev))
        {
            ihacinput_set_error(ERROR_DEVICE_CANNOT_STARTED, "Unable to start libevdev.");
            return false;
        }

        uint8 isValid = true;
        for (uint16 key : requiredKeys)
        {
            if (!libevdev_has_event_code(evdev, EV_KEY, key))
            {
                isValid = false;
                break;
            }
        }

        if (!isValid) continue;

        if (libevdev_uinput_create_from_device(evdev, LIBEVDEV_UINPUT_OPEN_MANAGED, &evdevUInput))
        {
            ihacinput_set_error(ERROR_DEVICE_CREATE_UINPUT, "Unable to create uinput");
            return false;
        }

        if (libevdev_grab(evdev, LIBEVDEV_GRAB))
        {
            ihacinput_set_error(ERROR_DEVICE_MERGE_UINPUT, "Unable to merge your device to uinput instance");
            return false;
        }

        founded = true;
        break;
    }

    if (founded) {
        std::thread runningReal(running_real_inputs, evdev, evdevUInput);
        runningReal.detach();
        std::thread runningFake(running_fake_inputs, evdev, evdevUInput);
        runningFake.detach();
    } else {
        return false;
    }

    return true;
} 

// Usada para executar a ação no mouse (Clique Direito, Esquerdo ou Central)
void ihacinput_mouse_input(__attribute_maybe_unused__ uint16 mouseButton)
{
    switch(mouseButton)
    {
        case MOUSE_BTN_MIDDLE:        ihacinput::buttonType = BTN_MIDDLE; break;
        case MOUSE_BTN_RIGHT:         ihacinput::buttonType = BTN_RIGHT;  break;
        default: case MOUSE_BTN_LEFT: ihacinput::buttonType = BTN_LEFT;   break;
    }
    ihacinput::clicks++;
}

/// Metódos usados na integração JNI (Java Native Interface)

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