def buildNumber = env.BUILD_NUMBER as int
if (buildNumber > 1) milestone(buildNumber - 1)
milestone(buildNumber)
// stops running containers of previous builds

pipeline {
    agent any

    stages {
        stage('clone code') {
            steps {
                // Get some code from a GitHub repository
                sh "docker volume prune -f"
                git (url: 'https://github.com/revature-rev-tech2/project-3-back-end.git', branch:'main')
                
                echo 'clone step'
            }
        }
        stage('build code') {
            steps {
                // Run Maven on a Unix agent.
                sh "mvn clean package -DskipTests"
                echo 'build step'
            }
        }
        stage('staging') {
            steps {
            	echo 'deploy step'
               sh "docker-compose down"
               
               sh "docker image rm -f revtech-backend"
                   
               sh "docker-compose up -d"
            }
        }
    }
}
