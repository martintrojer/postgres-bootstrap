# Postgres bootstrap

Example of how to migrate/write-models/test with postgres from clojure

## Provisioning

    $ sudo apt-get install postgresql-9.3
    $ sudo -u postgres createuser juxt -P

(password; "juxt_secret")

    $ sudo -u postgres createdb juxt_db -O juxt

## Migrating schemas

Run some code in [migrations.clj](https://github.com/martintrojer/postgres-bootstrap/blob/master/src/migrations.clj#L38)

## Command Line

    $ sudo -u postgres psql juxt_db

## Usage

    $ lein test
