#/bin/bash
# Run train depot in dev mode
export JAVA_OPTS="--add-exports java.base/jdk.internal.misc=ALL-UNNAMED"
quarkus dev