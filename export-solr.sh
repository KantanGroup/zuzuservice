#!/usr/bin/env bash

size=100
#size=1240
#size=3070
for page in {0..1}
do
    file_name=backups/app-trend-$page.json
    #link="http://api.zuzuapps.com/solr/app-information-index/select?indent=on&q=*:*&rows=$size&start=$((size*page))&wt=json"
    #link="http://api.zuzuapps.com/solr/app-index/select?indent=on&q=*:*&rows=$size&start=$((size*page))&wt=json"
    link="http://demo.zuzuapps.com/solr/app-trend-index/select?indent=on&q=*:*&rows=$size&start=$((size*page))&wt=json"
    #link="http://localhost:8983/solr/app-trend-index/select?indent=on&q=*:*&rows=$size&start=$((size*page))&wt=json"
    #echo $link
    curl $link | jq ".response.docs" | jq "map(del (._version_) | del (.icon) | del (.title) | del (.developer_id) )" > $file_name
    mv $file_name backups/trend/queue/
    #curl "http://api.zuzuapps.com/solr/app-information-index/select?indent=on&q=*:*&rows=100&start=$page&wt=json" | jq ".response.docs" | jq "map(del (._version_))" > $file_name
    #curl "http://192.168.195.243:8983/solr/app-information-index/select?indent=on&q=*:*&rows=100&start=$page&wt=json" | jq ".response.docs" | jq "map(del (._version_))" > $file_name
    #curl "http://192.168.195.243:8983/solr/app-information-index/select?indent=on&q=*:*&rows=5000&start=0&wt=json" | jq ".response.docs" | jq "map(del (._version_) | del (.create_at) | del (.development_url) | del (.reviews))"> $file_name
    #curl 'http://192.168.157.111:8983/solr/demo/update?commit=true' --data-binary @$file_name -H 'Content-type:application/json'
    #rm -f $file_name
done

