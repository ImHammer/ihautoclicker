#! /bin/sh

########################################
### Criando o app image para o linux ###
########################################

ARCH=x86_64
THIS=$(cd "$(dirname "$0")" && pwd)

APPIMAGE_PLUGIN=$THIS/plugins/linuxdeploy-x86_64.AppImage
REQUIRED_LIB_FILE=$THIS/req_lib/linux/$ARCH/libJNativeHook.so
RUNTIME_DIR=$THIS/app/build/ihautoclicker
APPIMAGE_DIR=$THIS/linux_appimage/IHAutoClicker_v1.0.0.AppDir

if [ ! -f $APPIMAGE_PLUGIN ]; then
    echo "Plugin AppImage não encontrado em: $APPIMAGE_PLUGIN"
    exit 1
fi

#########################
### Gerando o runtime ###
#########################

echo "---> Gerando o runtime image"
./gradlew build jlink

if [ ! "$?" -eq "0" ]; then
    echo "!!! Ocorreu um error ao tentar gerar o runtime"
    exit 1
fi

######################################
### Copiando a livraria necessaria ###
######################################

echo "---> Copiando a livraria necessaria para libs do runtime"

if [ ! -d $RUNTIME_DIR ]; then
    echo "!!! Diretorio do runtime image não encontrado! esperado: $RUNTIME_DIR"
    exit 1
fi

if [ ! -f $REQUIRED_LIB_FILE ]; then
    echo "!!! Livraria necessária não encontrada! esperado: $REQUIRED_LIB_FILE"
    exit 1
fi

cp $REQUIRED_LIB_FILE "$RUNTIME_DIR/lib"

#############################################
### Movendo o runtime para o appimage dir ###
#############################################

echo "---> Movendo o runtime para a pasta do AppDir (Onde será criado o AppImage)"

if [ ! -d $APPIMAGE_DIR ]; then
    echo "Pasta do AppDir necessaria para a criação do AppImage não foi encontrada! esperado: $APPIMAGE_DIR"
    exit 1
fi

APP_RUN_DIR=$APPIMAGE_DIR/opt/app

mv $RUNTIME_DIR $APP_RUN_DIR

###################################################
### Criando o appimage com o plugin linuxdeploy ###
###################################################

echo "---> Construindo o AppImage"

if [ ! -f $APPIMAGE_PLUGIN ]; then
    echo "!!! Plugin para a criação do appimage não encontrado, esperado: $APPIAMGE_PLUGIN"
    exit 1 
fi

# COMANDO PARA A CRIAÇÃO DO APPIMAGE
ARCH=$ARCH $APPIMAGE_PLUGIN --appdir $APPIMAGE_DIR -o appimage --exclude-library=jvm

if [ ! "$?" -eq "0" ]; then
    echo "!!! Ocorreu um error ao tentar gerar o AppImage"
    exit 1
fi

clear
echo " >>>> Sucesso, seu appimage foi criado $THIS/IHAutoClicker-x86_64.AppImage"
echo "---> Limpando as pastas"

./gradlew clean
rm -rf $APP_RUN_DIR

exit 1

# TODO: Copiar a livraria para o runtime
# TODO: Copiar o runtime para o linux_appimage
# TODO: Executar o build de criação do app image
















# this=$(cd "$(dirname "$0")" && pwd)
# command=$this/scripts/linuxdeploy-x86_64.AppImage

# if [ ! -f $APPIMAGE_COMMAND ]; then
#     echo "Comando não encontrado em: $command"
#     exit 1
# fi

# echo " ---> Gerando o runtime..."
# sh $this/scripts/genruntime.sh

# runtime_dir=$this/app/build/ihautoclicker

# appimage_dir=$this/linux_appimage/IHAutoClicker_v1.0.0.AppDir
# target_dir=$appimage_dir/opt/app

# if [ ! -d $runtime_dir ]; then
#     echo " ! Runtime $runtime_dir not found !"
#     exit 1
# fi

# if [ -d $target_dir ]; then
#     echo " -> Removendo a pasta app que já existe na AppDir..>"
#     rm -rf $target_dir
# fi

# echo " -> Movendo $runtime_dir para $target_dir ..."
# mv $runtime_dir $target_dir

# echo " -> Criando o appimage... "

# ARCH=x86_64 $command --appdir $appimage_dir -o appimage --exclude-library=jvm

# clear

# echo " ---> Sucesso <--- "
# echo " ---> Limpando tudo... <--- "

# $this/gradlew clean
# rm -rf $target_dir