#!/usr/bin/env bash

docker stop ds-postgres
docker rm ds-postgres
docker run --name ds-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -p 5433:5432 -d postgres
