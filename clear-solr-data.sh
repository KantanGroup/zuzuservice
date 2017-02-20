#!/usr/bin/env bash

curl "http://localhost:8983/solr/app-index/update?commit=true&stream.body=%3Cdelete%3E%3Cquery%3E*:*%3C/query%3E%3C/delete%3E"
curl "http://localhost:8983/solr/app-trend-index/update?commit=true&stream.body=%3Cdelete%3E%3Cquery%3E*:*%3C/query%3E%3C/delete%3E"
curl "http://localhost:8983/solr/app-information-index/update?commit=true&stream.body=%3Cdelete%3E%3Cquery%3E*:*%3C/query%3E%3C/delete%3E"
curl "http://localhost:8983/solr/app-screenshot-index/update?commit=true&stream.body=%3Cdelete%3E%3Cquery%3E*:*%3C/query%3E%3C/delete%3E"