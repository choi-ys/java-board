node {
    stage("Checkout") {
        checkout changelog: false, poll: false, scm: [
            $class: 'GitSCM',
            branches: [[name: '*/master']],
            extensions: [],
            userRemoteConfigs: [[credentialsId: 'Github-Repo', url: 'https://github.com/choi-ys/java-board.git']]]
        sh "echo 'Checkout SCM'"
    }
    stage('Build') {
        sh './gradlew clean build' //run a gradle task
        sh "echo 'Build jar'"
    }
    stage('Build image') {
        app = docker.build("413809178474.dkr.ecr.ap-northeast-2.amazonaws.com/cloudm-admin-api")
    }
    stage('Push image') {
        sh 'rm  ~/.dockercfg || true'
        sh 'rm ~/.docker/config.json || true'

        docker.withRegistry('https://413809178474.dkr.ecr.ap-northeast-2.amazonaws.com', 'ecr:ap-northeast-2:{credential ID}') {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
    }
}