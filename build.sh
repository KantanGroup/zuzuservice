#!/usr/bin/env bash

mvn clean install -DskipTests
cp zuzuservice-app-summary-scheduled/target/google-play-app-summary-scheduled-1.0.0.jar ~/appstore/
cp countries.json ~/appstore/