node {
    stage("Checkout") {
        checkout
            changelog: false,
            poll: false,
            scm:
                [
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'Github-Repo', url: 'https://github.com/choi-ys/java-board.git']]
                ]
        sh "echo 'Checkout SCM'"
    }
    stage('Build') {
        sh './gradlew clean build' //run a gradle task
        sh "echo 'Build jar'"
    }
    stage('Deploy') {
        sshPublisher(publishers: [sshPublisherDesc(
            configName: 'cloudm-admin',
            transfers: [sshTransfer(
                cleanRemote: false,
                excludes: '',
                execCommand:
                '''
                CURRENT_PID=$(pgrep -f java-board)
                if [[ $CURRENT_PID == "" ]]
                then
                  echo java-board.jar is not running
                else
                  kill -15 "$CURRENT_PID"
                  echo java-board.jar process killed forcefully, process id "$CURRENT_PID"
                  sleep 5
                fi

                BUILD_ID=dontKillMe nohup java -jar java-board-0.0.1-SNAPSHOT.jar 2>> /dev/null >> /dev/null &

                echo new process is $!
                ''',
                execTimeout: 0,
                flatten: false,
                makeEmptyDirs: false,
                noDefaultExcludes: false,
                patternSeparator: '[, ]+',
                remoteDirectory: '',
                remoteDirectorySDF: false,
                removePrefix: 'build/libs',
                sourceFiles: 'build/libs/*jar')],
            usePromotionTimestamp: false,
            useWorkspaceInPromotion: false,
            verbose: false)])
            sh "echo 'Deploy to AWS'"
    }
    stage('[TODO] Build docker image') {
    }
    stage('[TODO] Push image to ECR') {
    }
}