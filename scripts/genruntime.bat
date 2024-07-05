@echo off

@rem ### Descrição ###
@rem # Script usado para gerar o runtime, já copiando as livrarias necessarias
@rem # para o funcionamento do programa
@rem #################

cls

set this="%~dp0"

cd %this%\\..\\

echo " -> Gerando as classes..."
call gradlew.bat build

if %ERRORLEVEL% neq 0 goto error

echo " -> Criando o runtime.."
call gradlew.bat jlink

if %ERRORLEVEL% neq 0 goto error

cd %this%

echo " -> Copiando a livraria necessária..."
call %this%\\cpreqtomodulelibs.bat

if %ERRORLEVEL% neq 0 goto error

goto success

:error
echo "Não foi possivel gerar o runtime"
exit 1

:success
echo " -> Sucesso! <- "