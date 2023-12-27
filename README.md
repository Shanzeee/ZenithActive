
# ZenithActive - Ver.0.0.1 Release(2023.12)

ZenithActive is a comprehensive fitness management application designed to streamline various aspects of fitness centers and membership services. Explore the key features below.







## 🛠Tech Stack

- Spring Boot 3.1.6
- Postgres
- Docker
- JUnit 5
- Mockito
- Zxing
- Twilio
- Prometheus
- Jenkins


## Features:

1. Add users with roles (member and instructor).

2. Create new Facility objects.

3. Create courses and assign them to a specific facility and time range.

4. Allow members to enroll in courses.

5. Add reviews for courses.

6. Enable sign-ups for various types of memberships.

7. Provide a newsletter feature for updates and announcements.

8. Send emails and SMS with promotions and reminders.

9. Generate PDFs with personalized training plans.

10. Generate QR codes for access to specific facilities.

11. Loyalty points system.

### TODO:

1. Allow exchanging loyalty points for rewards.

2. Enable sign-ups for individual training sessions.

3.  Live chat feature for direct communication with mentors or instructors.
## Installing and Running

### Prerequisites:
- Docker installed on your machine.

### Steps:
1. Clone the repository:

    ```bash
    git clone https://github.com/Shanzeee/ZenithActive.git
    cd ZenithActive
    ```

2. Build the Docker image:

    ```bash
    docker build -t zenith-active .
    ```

3. Run Docker Compose:

    ```bash
    docker-compose up -d
    ```

   This command will start containers with the ZenithActive application, PostgreSQL, pgAdmin, Jenkins, Prometheus, and Grafana as defined in the docker-compose.yml file.

4. Create PostgreSQL Database:

Make sure to create a PostgreSQL database named "zenith" before running the application. You can use the following SQL commands as a reference:

   ```sql
   CREATE DATABASE zenith;
   ```

Or use pgAdmin available at: http://localhost:5050

### Access the Application:
Once the containers are up and running, you can access the ZenithActive application at http://localhost:8080.

### Access Other Services:

- PostgreSQL: http://localhost:5432
- pgAdmin: http://localhost:5050 (Default credentials: Email: pgadmin4@pgadmin.org, Password: admin)
- Jenkins: http://localhost:8081
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (Default credentials: Username: admin, Password: admin)

### Stop the Containers:
```bash
docker-compose down
```

This command will stop and remove the running containers.

### Notes:
- Ensure that ports 8080, 5432, 5050, 8081, 9090, and 3000 are not in use by other applications on your machine.
- Customize environment variables and configurations in the docker-compose.yml file as needed.
- You can modify the Dockerfile and other configuration files based on your application's requirements.

## API Documentation

Application exposes a robust API with over 40 endpoints to cater to various functionalities. For detailed information on each endpoint, you can explore the API documentation using Swagger UI. After launching the application, visit http://localhost:8080/swagger-ui/index.html#/ to interactively explore and understand the available endpoints, request parameters, and response structures.



## License

ZenithActive is released under the [MIT License](https://choosealicense.com/licenses/mit/).

The MIT License is a permissive open-source license that allows for the free use, modification, and distribution of ZenithActive. Whether you are a fitness center looking to integrate ZenithActive into your operations or a developer interested in contributing to its enhancement, you are welcome to do so within the terms of the MIT License.

I believe in the collaborative spirit of the open-source community, and the MIT License reflects our commitment to fostering a transparent and accessible platform for the benefit of fitness enthusiasts, administrators, and developers alike.

Feel free to explore, modify, and contribute to ZenithActive within the bounds of the MIT License. Let's build a healthier and more connected future together! :)
