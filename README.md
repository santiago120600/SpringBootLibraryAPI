# Library API

## Setup Instructions

### Development Environment

- **Start the Environment**:
   ```bash
   docker-compose -f docker/dev/docker-compose.yaml up --build
   ```
   - Builds the JAR automatically using Maven.
   - Starts the API on `http://localhost:8080` and MySQL on `localhost:3306`.
   - Enables debugging on port `5005`.


### Production Environment
The production environment is optimized for deployment, with no debugging or live reloading.

- **Start the Environment**:
   ```bash
   docker-compose -f docker/prod/docker-compose.yaml up --build
   ```