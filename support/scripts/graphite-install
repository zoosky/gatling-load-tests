#!/bin/bash

echo "starting elasticsearch"
docker kill bbcnews-elasticsearch >/dev/null 2>&1
docker rm bbcnews-elasticsearch >/dev/null 2>&1
docker run -d \
    -p 9200:9200 \
    --name bbcnews-elasticsearch bbcnews/elasticsearch

echo "starting carbon"
docker kill bbcnews-carbon >/dev/null 2>&1
docker rm bbcnews-carbon >/dev/null 2>&1
docker run -d \
    -p 7002:7002 \
    -p 2003:2003 \
    -p 2004:2004 \
    -v /opt/graphite \
    --name bbcnews-carbon bbcnews/carbon

echo "starting graphite"
docker kill bbcnews-graphite >/dev/null 2>&1
docker rm bbcnews-graphite >/dev/null 2>&1
docker run -d \
    --volumes-from bbcnews-carbon \
    --name bbcnews-graphite bbcnews/graphite

echo "starting nginx"
docker kill bbcnews-nginx >/dev/null 2>&1
docker rm bbcnews-nginx >/dev/null 2>&1
docker run -d -p 8080:80 --name bbcnews-nginx --link bbcnews-graphite:bbcnews-graphite-link bbcnews/nginx

echo "starting grafana"
docker kill bbcnews-grafana >/dev/null 2>&1
docker rm bbcnews-grafana >/dev/null 2>&1
docker run -d -P --name bbcnews-grafana bbcnews/grafana

PORT=`docker ps | grep bbcnews-grafana | sed -e 's/.*://' | sed -e 's/\-.*//'`
HOST=`/usr/bin/env | grep DOCKER_HOST | perl -pe 's/.*\/([0-9\.]+):.*/$1/'`
echo "Dashboard running at http://$HOST:$PORT"
