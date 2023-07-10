# Contacts API
> The aim of this project is to create a simple API, where a user can get a quick overview over all contacts resources
like person, skills...


## Requirements
- you should have installed on your machine `docker` and `docker-compose`.

## Usage
- To start the project
    -  clone the repository
    - `cd ./contactsapi`
    - `docker compose up --build`
>The application will automatically build and start two **Docker** containers (Spring Boot App Container & PostgreSQL Container) and will be accessible on `localhost:8000`, make sure that the port is not used by other applications, otherwise modify the `ports` field in `docker-compose.yml`.
> 
> Only the `localhost:8000/register` and `localhost:8000/authenticate` routes are opened for non-authenticated users. For accessing the other routes please firstly **register** and then **authenticate**.
> The `authenticate` route will return a **JWT** authentication token that should be attached as a bearer token to every http request in order to access the protected routes.

## SWAGGER
While the application is running the API Documentation is available at `http://localhost:8000/swagger-ui/index.html`

