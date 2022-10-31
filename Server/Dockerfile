FROM golang:latest

LABEL version = "1.1" description="rtc_demo server"

COPY VolcEngineRTC_Solution_Demo/ /go/src/github.com/volcengine/VolcEngineRTC_Solution_Demo/
COPY docker-setup.sh /go/src/github.com/volcengine/VolcEngineRTC_Solution_Demo/setup.sh

WORKDIR /go/src/github.com/volcengine/VolcEngineRTC_Solution_Demo/

CMD ["sh","setup.sh"]