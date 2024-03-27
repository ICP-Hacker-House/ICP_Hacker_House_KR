#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH) # switch.sh 경로
source ${ABSDIR}/profile.sh # import profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port) # idle port 값을 잡아온다.

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    # /etc/nginx/conf.d/service_url.inc에 앞의 문장을 덮어씀
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
    echo "> 엔진엑스 Reload"
    sudo service nginx reload # service-url.inc를 다시 불러옴
}