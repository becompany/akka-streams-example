# akka-file-io

## Install

    sbt assembly
    
## Run

Generate log data:

    ./log-analysis.sh generate > target/test.log
    
Analyze log data:

    ./log-analysis.sh analyze target/test.log