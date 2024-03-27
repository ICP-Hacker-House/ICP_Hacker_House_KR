#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH) # health.sh 경로
source ${ABSDIR}/profile.sh # import profile.sh
source ${ABSDIR}/switch.sh # import switch.sh

IDLE_PORT=$(find_idle_port) # idle port 값을 잡아온다.

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l) # "real"이 포함된 라인 수

  if [ ${UP_COUNT} -ge 1 ] # "real"이 존재한다면
  then # $up_count >= 1 ("real" 문자열이 있는지 검증)
    echo "> Health check 성공"
    switch_proxy # IDLE_PORT 로 포트 변경
    break
  else
    echo "> Health check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
    echo "> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ] # RETRY_COUNT 내에서 성공하지 못한 경우
  then
    echo "> Health check 실패"
    echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done