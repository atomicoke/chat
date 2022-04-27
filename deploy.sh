#!/bin/bash

# 修改Path为云效上的应用名

#mkdir -p /home/admin/chat-0.01

#tar zxvf /home/admin/app/package.tgz -C /home/admin/chat-0.01/
#sh /home/admin/chat-0.01/deploy.sh restart

Path=./chat-0.01.jar
ACTION=$1
JAVA_OUT=./start.log  #应用的启动日志
Config=./application-prod.yml #配置文件位置


usage() {
    echo "Usage: {start|stop|restart}"
    exit 2
}

start_application() {
    echo "starting java process"
    echo "${Path}"
    nohup java -javaagent:/root/project/xrebel.jar -jar ${Path} -Dspring.config.location=${Config} > ${JAVA_OUT} 2>&1 &
    echo "started java process"
}

stop_application() {
   checkjavapid=`ps -ef | grep java | grep ${Path} | grep -v grep |grep -v 'deploy.sh'| awk '{print$2}'`
   
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
        checkjavapid=`ps -ef | grep java | grep ${Path} | grep -v grep |grep -v 'deploy.sh'| awk '{print$2}'`
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