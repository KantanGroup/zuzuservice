#!/usr/bin/env bash

mvn clean install -DskipTests
cp zuzuservice-app-summary-scheduled/target/google-play-app-summary-scheduled-1.2.0.jar ~/appstore/zuzuservice.jar
cp countries.json ~/appstore/