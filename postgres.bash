#!/usr/bin/env bash

docker stop ds-postgres
docker rm ds-postgres
docker run --name ds-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=device-service -p 5432:5432 -d postgres