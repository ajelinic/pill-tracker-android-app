#!/usr/bin/env sh
# Minimal Gradle wrapper script (uses the custom gradle-wrapper.jar in this repo)

APP_HOME="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ -n "$JAVA_HOME" ] ; then
  JAVA_CMD="$JAVA_HOME/bin/java"
else
  JAVA_CMD="java"
fi

exec "$JAVA_CMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
