#!/bin/sh
DIR=$(cd "$(dirname "$0")" && pwd)
java -jar "$DIR/../target/meuCompilador.jar" "$@"
