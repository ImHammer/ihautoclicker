#! /bin/bash

### Descrição ###
# Script usado para gerar o runtime, já copiando as livrarias necessarias
# para o funcionamento do programa
#################

this=$(cd "$(dirname "$0")" && pwd)

clear

echo " -> Gerando as classes..."
$this/../gradlew build
clear

echo " -> Criando o runtime.."
$this/../gradlew jlink
clear

echo " -> Copiando a livraria necessária..."
sh $this/cpreqtomodulelibs.sh
clear

echo "Sucesso!"
exit 0