#!/bin/bash

# 修改APP_NAME为云效上的应用名

#mkdir -p /home/admin/chat-0.01

#tar zxvf /home/admin/app/package.tgz -C /home/admin/chat-0.01/
#sh /home/admin/chat-0.01/deploy.sh restart

PROG_NAME=$0
APP_NAME=$1
JAR_NAME=$2
ACTION=$3
APP_HOME=/root/project/${APP_NAME} # 从package.tgz中解压出来的jar包放到这个目录下
JAR_NAME=${APP_HOME}/${JAR_NAME}.jar # jar包的名字
JAVA_OUT=${APP_HOME}/logs/start.log  #应用的启动日志

# 创建出相关目录
mkdir -p ${APP_HOME}/logs

usage() {
    echo "Usage: $PROG_NAME {start|stop|restart}"
    exit 2
}

start_application() {
    echo "starting java process"
    echo "${JAR_NAME}"
    nohup java -javaagent:/root/project/xrebel.jar -jar ${JAR_NAME} > ${JAVA_OUT} 2>&1 &
    echo "started java process"
}

stop_application() {
   checkjavapid=`ps -ef | grep java | grep ${APP_NAME} | grep -v grep |grep -v 'deploy.sh'| awk '{print$2}'`
   
   if [[ ! $checkjavapid ]];then
      echo -e "\rno java process"
      return
   fi

   echo "stop java process"
   times=60
   for e in $(seq 60)
   do
        sleep 1
        COSTTIME=$(($times - $e ))
        checkjavapid=`ps -ef | grep java | grep ${APP_NAME} | grep -v grep |grep -v 'deploy.sh'| awk '{print$2}'`
        if [[ $checkjavapid ]];then
            kill -9 $checkjavapid
            echo -e  "\r        -- stopping java lasts `expr $COSTTIME` seconds."
        else
            echo -e "\rjava process has exited"
            break;
        fi
   done
   echo ""
}
start() {
    start_application
}

stop() {
    stop_application
}
case "$ACTION" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    *)
        usage
    ;;
esac