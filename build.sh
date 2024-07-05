#! /bin/sh

### Criando o app image para o linux
this=$(cd "$(dirname "$0")" && pwd)
command=$this/scripts/linuxdeploy-x86_64.AppImage

if [ ! -f $APPIMAGE_COMMAND ]; then
    echo "Comando não encontrado em: $command"
    exit 1
fi

echo " ---> Gerando o runtime..."
sh $this/scripts/genruntime.sh

runtime_dir=$this/app/build/ihautoclicker

appimage_dir=$this/linux_appimage/IHAutoClicker_v1.0.0.AppDir
target_dir=$appimage_dir/opt/app

if [ ! -d $runtime_dir ]; then
    echo " ! Runtime $runtime_dir not found !"
    exit 1
fi

if [ -d $target_dir ]; then
    echo " -> Removendo a pasta app que já existe na AppDir..>"
    rm -rf $target_dir
fi

echo " -> Movendo $runtime_dir para $target_dir ..."
mv $runtime_dir $target_dir

echo " -> Criando o appimage... "

ARCH=x86_64 $command --appdir $appimage_dir -o appimage --exclude-library=jvm

clear

echo " ---> Sucesso <--- "
echo " ---> Limpando tudo... <--- "

$this/gradlew clean
rm -rf $target_dir