
### Delete all documents in a Solr index using curl.md
```
http://localhost:8983/solr/app-information-index/update?commit=true&stream.body=<delete><query>*:*</query></delete>
```