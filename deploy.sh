CURRENT_PID=$(pgrep -f java-board)

if [[ $CURRENT_PID == "" ]]
then
  echo java-board.jar is not running
else
  kill -9 "$CURRENT_PID"
  echo java-board.jar process killed forcefully, process id "$CURRENT_PID"
  sleep 5
fi

nohup java -jar java-board-0.0.1-SNAPSHOT.jar 2>> /dev/null >> /dev/null &

echo new process is $!