#/bin/bash
# Run train depot in dev mode
export JAVA_OPTS="--add-exports java.base/jdk.internal.misc=ALL-UNNAMED"
mvn clean
quarkus build
quarkus run
# Run Quarkus with additional JVM parameter
quarkus run -Dquarkus.jvm.args="--add-exports java.base/jdk.internal.misc=ALL-UNNAMED"