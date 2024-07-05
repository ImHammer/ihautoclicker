@echo off

:: Script usado para copiar a livraria necessaria para o
:: runtime do jlink, pois essa lib jNativeHook precisa ser carregada
:: diretamente na pasta de libs, diferente da nativa do programa

set this="%~dp0"
set req_lib_dir=%this%\\..\\req_lib

set arch=x86_64
set target=windows
set lib_name=JNativeHook.dll
set run_img_dir=%this%\\..\\app\\build\\ihautoclicker
set run_img_lib_dir=%run_img_dir%\\bin

if not exist "%this%" (
    echo "Diretorio atual foi excluido!"
    pause
    exit 1
)

if not exist "%req_lib_dir%" (
    echo "Diretorio da livraria necessaria não foi encontrado!"
    pause
    exit 1
)

if not exist "%run_img_dir%" (
    echo "Diretorio do runtime (%run_img_dir%) não encontrado!"
    echo "Execute este código no diretorio source do programa, se ainda não conseguir:"
    echo "Execute ./gradlew|gradlew.bat jlink para gerar a runtime"
    pause
    exit 1 
)

if not exist "%run_img_lib_dir%" (
    echo "Diretorio $run_img_lib_dir não econtrado"
    echo "Exclua a pasta do runtime e execute o comando de build novamente"
    pause
    exit 1
)

if not "%1" == "" (
    arch=%1
)

if not "%2" == "" (
    target=%2
)

set req_lib_file=%req_lib_dir%\%target%\%arch%\%lib_name%

if not exist %req_lib_file% (
    echo "Livraria necessaria para a %target%-%arch% não encontrada! (%req_lib_file%)"
    exit 1
)

Copy %req_lib_file% %run_img_lib_dir%

echo "Livraria copiada com sucesso"