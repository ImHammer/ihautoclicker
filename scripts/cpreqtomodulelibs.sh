#! /bin/bash

### Script usado para copiar a livraria necessaria para o             ###
### runtime do jlink, pois essa lib jNativeHook precisa ser carregada ###
### diretamente na pasta de libs, diferente da nativa do programa     ###

this=$(cd "$(dirname "$0")" && pwd)
req_lib_dir="$this/../req_lib"

arch="x86_64"
target="linux"
lib_name="libJNativeHook.so"
run_img_dir="$this/../app/build/ihautoclicker"
run_img_lib_dir="$run_img_dir/lib"

if [ ! -d "$this" ]; then
    echo "Diretorio atual foi excluido!"
    exit 1
fi

if [ ! -d "$req_lib_dir" ]; then
    echo "Diretorio da livraria necessaria não foi encontrado!"
    exit 1
fi

if [ ! -d "$run_img_dir" ]; then
    echo "Diretorio do runtime ($run_img_dir) não encontrado!"
    echo "Execute este código no diretorio source do programa, se ainda não conseguir:"
    echo "Execute ./gradlew|gradlew.bat jlink para gerar a runtime"
    exit 1 
fi

if [ ! -d "$run_img_lib_dir" ]; then
    echo "Diretorio $run_img_lib_dir não econtrado"
    echo "Exclua a pasta do runtime e execute o comando de build novamente"
    exit 1
fi

if [ $# -ge 1 ]; then
    arch=$1
fi
if [ $# -ge 2 ]; then
    target=$2
fi

req_lib_file="$req_lib_dir/$target/$arch/$lib_name"

if [ ! -f "$req_lib_file" ]; then
    echo "Livraria necessaria para a $target-$arch não encontrada! ($req_lib_file)"
    exit 1
fi

cp $req_lib_file $run_img_lib_dir
echo "Livraria copiada com sucesso"

exit 0