#!make

mysql_container = "project-name-default-mysql-1"
service = project_name_default

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

down: ## Down all containers
	@echo "Down all containers"
	@docker-compose down

up: ## Up docker-composer
	@docker-compose up -d

set-env:
	@echo "Set env"
	-cp -v -n $(shell pwd)/.local.env.sample  $(shell pwd)/src/main/resources/application.properties
	export $(shell sed 's/=.*//' $(pwd)/src/main/resources/application.properties) &>/dev/null


run: up set-env run-migrations ## Run application
	@echo "Run application"
	sbt app/run

sbt: up run-migrations ## Run sbt
	@echo "Run sbt"
	sbt

tests: ## Run all tests
	@sbt clean compile test

tests-coverage: ## Run tests with coverage report
	@sbt clean coverage test
	@sbt coverageAggregate

status: ## check containers state.
	docker-compose ps

logs: ## tail and follow logs.
	docker-compose logs --tail="all" --follow

stop: ## stop all running containers.
	docker-compose stop

clean: stop ## stop all running containers, then purge all volumes, networks and containers.
	docker-compose down -v --remove-orphans

run-migrations:
	@echo "Run flyway migrate"
	@sbt flywayMigrate

run-sql-seeds:
	@echo "Execute scripts/seed.sql"
	@docker exec -i $(mysql_container) /usr/bin/mysql -u root -proot < scripts/seed.sql

drop-db:
	-docker exec -i $(mysql_container) /usr/bin/mysql -u root -proot  -e 'drop database `${service}`'

create-db:
	-docker exec -i $(mysql_container) /usr/bin/mysql -u root -proot  -e 'create database `${service}`'

reset-db: ## Drop an recreate db, with seed
	@$(MAKE) drop-db
	@$(MAKE) create-db
	@$(MAKE) run-migrations

dependency-tree: ## Generate dependencies tree
	@sbt dependencyBrowseTree

dependency-graph: ## Generate dependencies graph
	@sbt dependencyBrowseGraph

set-all: ## Set the environment, but do not start the API
	@$(MAKE) clean
	@docker-compose up -d --build --force-recreate
	@$(MAKE) set-env
	@echo "Waiting for MySQL to start"
	@while [ "`docker inspect -f {{.State.Health.Status}} $(mysql_container)`" != "healthy" ]; do \
		printf "." && sleep 0.2; \
	done ;
	@echo "\nReseting all database"
	@$(MAKE) reset-db

dd: ## Reset and relaunch everything
	@$(MAKE) set-all
	@$(MAKE) run

open-mockserver-dashboard: ## open-mockserver-dashboard
	open http://localhost:9123/mockserver/dashboard
