# Akka Streams example

Example project to demonstrate log file processing with Akka Streams.

Check out the corresponding [blog post](https://www.becompany.ch/en/blog/tech/2016/06/22/realtime-log-processing.html) for more information.

## Install

    sbt assembly
    
## Run

Generate log data:

    ./log-analysis.sh generate > target/test.log
    
Analyze log data:

    ./log-analysis.sh analyze target/test.log