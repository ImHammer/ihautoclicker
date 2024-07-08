
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

#include <iostream>
#include <thread>
#include <chrono>

#include <ihacinput.h>

int main(int argc, char const *argv[])
{
    fprintf(stdout, "Testando a lib no linux!\n");

    if (!ihacinput_init()) {
        fprintf(stdout, "Não foi possivel iniciar o ihacinput, error: %s\n", ihacinput_get_error_message());
        return EXIT_FAILURE;
    }

    std::cout << "Saiu aqui" << std::endl;
    while(true) {
        std::cout << "Clicou" << std::endl;
        ihacinput_mouse_input(MOUSE_BTN_LEFT);
        std::this_thread::sleep_for(std::chrono::milliseconds(60));
    }

    return EXIT_SUCCESS;
}
