# Library API

## Setup Instructions

### Development Environment

- **Start the Environment**:
   ```bash
   docker-compose -f docker/dev/docker-compose.yaml up --build -d
   ```
   - Builds the JAR automatically using Maven.
   - Starts the API on `http://localhost:8080` and MySQL on `localhost:3306`.
   - Enables debugging on port `5005`.


### Production Environment
The production environment is optimized for deployment, with no debugging or live reloading.

- **Start the Environment**:
   ```bash
   docker-compose -f docker/prod/docker-compose.yaml up --build -d
   ```
### MYSQL Default Credentials

| Username | Password   |
|----------|------------|
| `santi`  | `password` |

### Swagger UI
`http://localhost/swagger-ui/index.html`

### Swagger Docs
`http://localhost/v3/api-docs`

### Kibana Dashboard
`http://localhost/kibana`

#### Default Credentials

| Username  | Password    |
|-----------|-------------|
| `elastic` | `password1` |

#### Creating the Index Pattern

Application logs are shipped from Logback → Logstash → Elasticsearch into an index named `springboot-library-api` (built from the Logstash output pattern `springboot-%{app}` where `app` is `library-api`). Before creating the pattern, make sure the API is running and has produced at least one log entry so the index exists in Elasticsearch.

1. Open Kibana at `http://localhost/kibana` and log in with the credentials above.
2. In the left sidebar, go to **Management → Index Patterns** (under the Kibana section).
   - Or navigate directly to `http://localhost/kibana/app/management/kibana/indexPatterns`.
3. Click **Create index pattern**.
4. In the **Index pattern name** field, type `springboot-library-api`.
   - The field should show a green checkmark confirming the matching index exists.
5. Click **Next step**.
6. (Optional) From the **Time filter field name** dropdown, select `@timestamp` so logs can be filtered by time in Discover.
7. Click **Create index pattern**.

Once created, go to **Discover** and select the `springboot-library-api` index pattern from the dropdown to view and search the application logs.