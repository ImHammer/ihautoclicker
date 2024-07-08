
#include <iostream>
#include <string>
#include <filesystem>
#include <stdlib.h>

#define BAT_FILE "app/bin/com.github.imhammer.ihautoclicker.bat"

int main(int argc, char const *argv[])
{
    std::filesystem::path path = std::filesystem::absolute(BAT_FILE);

    if (!std::filesystem::exists(path)) {
        std::cerr << "O arquivo de inicialização foi deletado: " << path << std::endl;
        return EXIT_FAILURE;
    }

    int result = system(path.string().c_str());
    if (result == 1) {
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}
