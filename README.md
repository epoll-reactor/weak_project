# weak_project

## Setup environment

One thing you need is installed PostgreSQL server.

Also, you should to have PostgreSQL user with opts (for example) `CREATEDB`, `LOGIN`, `PASSWORD NULL`.

Next, take a look into `src/main/resources/application.conf`, which may look like that:
```
ktor {
    deployment {
        port = 8080
    }
    application {
        modules = [ com.weak_project.config.ApplicationConfigKt.module ]
    }
    database {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "..."
        password = "..."
        driver = "org.postgresql.Driver"
    }
}
```
There you should to pass your PostgreSQL username, password.

