@echo off

@rem Criando o aplicativo portavel para windows

set this=%~dp0

echo " ---> Gerando o runtime..."
call %this%\\scripts\\genruntime.bat

set this=%~dp0

set portable_dir=%this%\\windows_portable

set runtime_dir=%this%\\app\\build\\ihautoclicker
set target_dir=%portable_dir%\\app
set exec_file=%target_dir%\\bin\\com.github.imhammer.ihautoclicker.bat

echo " ---> Criando o aplicativo portavel"

if not exist "%portable_dir%" (
    mkdir %portable_dir%
)

move %runtime_dir% %target_dir%
cd %portable_dir%

mklink /h run.bat .\\app\\bin\\com.github.imhammer.ihautoclicker.bat

