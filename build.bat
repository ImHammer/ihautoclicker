@echo off

@rem Criando o aplicativo portavel para windows

set this=%~dp0

set ARCH=x86_64
set req_lib_file=%this%\req_lib\windows\%ARCH%\JNativeHook.dll

@rem GERANDO O RUNTIME IMAGE
echo "---> Gerando o IHAutoClicker Runtime Image"
@REM call gradlew.bat build jlink

if %ERRORLEVEL% neq 0 goto fail

@rem Limpando o console
cls

@rem Movendo o runtime para a pasta destino
set runtime_dir=%this%\app\build\ihautoclicker
set portable_dir=%this%\windows_portable
set app_dir=%portable_dir%\app

echo "---> Movendo o runtime image para o local de destino"

if not exist %runtime_dir% (
    echo "!!! A pasta do runtime (%runtime_dir%) não foi encontrada"
    goto fail
)
if exist %app_dir% (
    rm %app_dir%
)

Move %runtime_dir% %app_dir%

@rem Limpando o console
cls

@rem Copiando a livraria necessaria para o IHAutoClicker
set app_bin_dir=%app_dir%\bin

echo "---> Injetando a livraria JNativeHook"
if not exist %req_lib_file% (
    echo "!!! Arquivo %req_lib_file% não foi encontrado, ou o arquivo foi deletado"
    goto fail
)

Copy %req_lib_file% %app_bin_dir%

goto end

:fail
echo "!!! Ocorreu um error ao construir o aplicativo"
pause
exit 1

:end
echo " === SUCESSO ==="
pause
exit 0