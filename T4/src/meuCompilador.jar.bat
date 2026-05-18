#!/bin/sh
DIR=$(cd "$(dirname "$0")" && pwd)
java -cp "$DIR/../out" Main "$@"
