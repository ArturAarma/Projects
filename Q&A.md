# Spring Boot Project FAQs

## REST API and HTTP Methods

### Structure of a REST API URL
A REST API URL is typically divided into three main components:

1. **Base URL**: The foundational part of the URL that identifies the domain and the root endpoint of the API. Example: `https://api.example.com`.
2. **Resource**: Specifies the particular entity or collection being accessed, such as `/movies`, `/actors`, or `/genres`.
3. **Query Parameters**: Provide additional filters or options to refine the request. These are appended to the URL after a `?` and can include key-value pairs like `?name=John` or `?year=2023`.

### Four Main HTTP Methods and Their Purposes

1. **GET**:
   - **Purpose**: Retrieves data from the server without making any modifications.
   - **Example**: Fetching a list of all movies or details of a specific actor.
   - **Usage in Project**: Used for endpoints like `/api/movies` or `/api/actors/{id}`.

2. **POST**:
   - **Purpose**: Sends data to the server to create a new resource.
   - **Example**: Adding a new actor or movie.
   - **Usage in Project**: Used for creating resources like `/api/actors` or `/api/movies`.

3. **PATCH**:
   - **Purpose**: Updates specific fields of an existing resource.
   - **Example**: Modifying an actor's information or updating the actors list for a movie.
   - **Usage in Project**: Used for endpoints like `/api/movies/{id}` for updating actor lists.

4. **DELETE**:
   - **Purpose**: Deletes a resource from the server.
   - **Example**: Removing a genre or deleting an actor.
   - **Usage in Project**: Used for endpoints like `/api/genres/{id}` or `/api/actors/{id}`.

## CRUD Operations

### CRUD Operations and Their Importance

1. **CRUD**:
   - CRUD stands for **Create**, **Read**, **Update**, and **Delete**. These are the four basic operations performed on a database.
   - They represent the fundamental ways to interact with and manage data in a database.

2. **Operations**:
   - **Create**: Adds new records to the database (e.g., adding a new movie or actor).
   - **Read**: Retrieves data from the database (e.g., fetching details of all movies or a specific actor).
   - **Update**: Modifies existing records in the database (e.g., updating a movie’s title or adding an actor to a movie).
   - **Delete**: Removes records from the database (e.g., deleting a genre or actor).

3. **Importance in Database Management**:
   - Ensures that data is consistently managed and maintained.
   - Allows for efficient interaction with the database through standardized operations.
   - Facilitates the implementation of business logic in applications that rely on data storage.

## Dependency Injection

### What is Dependency Injection and How It's Used in the Project

1. **Definition**:
   - Dependency Injection (DI) is a design pattern in which an object receives its dependencies from an external source rather than creating them itself. It promotes loose coupling and improves testability.

2. **How It Works**:
   - Dependencies are provided by a framework or container, such as the Spring Framework.
   - In Spring, DI can be implemented using:
     - **Constructor Injection**: Dependencies are passed via the constructor.
     - **Field Injection**: Dependencies are injected directly into fields using `@Autowired`.
     - **Setter Injection**: Dependencies are set via public setter methods.

3. **Usage in This Project**:
   - Service classes such as `MovieService`, `ActorService`, and `GenreService` are injected into controllers using `@Autowired`.
   - Repository interfaces like `MovieRepository` are injected into services to interact with the database.
   - Example:
     ```java
     @RestController
     public class MovieController {
         private final MovieService movieService;

         public MovieController(MovieService movieService) {
             this.movieService = movieService;
         }

         @GetMapping("/api/movies")
         public List<Movie> getAllMovies() {
             return movieService.getAllMovies();
         }
     }
     ```

## Relationship Scenarios

### Demonstrating Various Relationship Scenarios

1. **Movies with Multiple Genres**:
   - A movie can belong to multiple genres (e.g., "Action" and "Adventure").
   - This is implemented using a many-to-many relationship between `Movie` and `Genre` entities, with a join table to manage associations.

2. **Actors in Multiple Movies**:
   - An actor can appear in multiple movies, and a movie can have multiple actors.
   - This is also represented as a many-to-many relationship between `Actor` and `Movie` entities.

3. **Movies with Varying Numbers of Actors**:
   - Each movie can have a unique set of actors, from none to several.
   - The relationship allows for flexibility in the number of actors associated with a movie.

4. **Implementation in the Project**:
   - **Entity Annotations**:
     ```java
     @Entity
     public class Movie {
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long id;

         @ManyToMany
         @JoinTable(
             name = "movie_genre",
             joinColumns = @JoinColumn(name = "movie_id"),
             inverseJoinColumns = @JoinColumn(name = "genre_id")
         )
         private Set<Genre> genres;

         @ManyToMany
         @JoinTable(
             name = "movie_actor",
             joinColumns = @JoinColumn(name = "movie_id"),
             inverseJoinColumns = @JoinColumn(name = "actor_id")
         )
         private Set<Actor> actors;
     }
     ```

   - **Data Example**:
     - Movie: "The Avengers"
       - Genres: "Action", "Adventure"
       - Actors: "Robert Downey Jr.", "Chris Evans"

   - **Query Example**:
     - Retrieve all movies for a specific actor or genre using repository methods.

   - **Controller Example**:
     ```java
     @GetMapping("/api/movies/{id}/actors")
     public List<Actor> getActorsByMovie(@PathVariable Long id) {
         return movieService.getActorsByMovieId(id);
     }
     ```

## JPA Repository

### What is a JpaRepository and Its Provided Methods

1. **Definition**:
   - `JpaRepository` is a Spring Data interface that extends `CrudRepository` and `PagingAndSortingRepository` to provide additional JPA-specific functionality for database operations.

2. **Key Features**:
   - Out-of-the-box methods for common operations.
   - Allows custom query methods by defining method signatures in the repository interface.

3. **Provided Methods**:
   - **`findById(ID id)`**: Retrieves an entity by its primary key.
   - **`save(S entity)`**: Saves a given entity (either inserts or updates).
   - **`deleteById(ID id)`**: Deletes an entity by its primary key.
   - **`findAll()`**: Retrieves all entities.
   - **`count()`**: Returns the total number of entities.

4. **Usage in This Project**:
   - Example repository for movies:
     ```java
     public interface MovieRepository extends JpaRepository<Movie, Long> {
         List<Movie> findByGenreId(Long genreId);
     }
     ```
   - This repository includes default methods and also custom query methods like `findByGenreId` to fetch movies for a specific genre.

## Spring Boot Annotations

### Purpose of the `@SpringBootApplication` Annotation

1. **Definition**:
   - The `@SpringBootApplication` annotation is a meta-annotation in Spring Boot that combines three key annotations:
     - `@Configuration`: Marks the class as a source of bean definitions.
     - `@EnableAutoConfiguration`: Automatically configures the application based on its dependencies.
     - `@ComponentScan`: Scans the package and sub-packages for components, configurations, and services.

2. **Purpose**:
   - Simplifies Spring Boot application setup by reducing boilerplate code.
   - Acts as the entry point for the Spring Boot application.

3. **Usage in This Project**:
   - It is placed on the main application class:
     ```java
     @SpringBootApplication
     public class MovieDatabaseApplication {
         public static void main(String[] args) {
             SpringApplication.run(MovieDatabaseApplication.class, args);
         }
     }
     ```
   - This setup enables the application to configure and run with minimal manual configuration.

### Purpose of the `@Entity` Annotation

1. **Definition**:
   - The `@Entity` annotation marks a class as a JPA entity, meaning it is mapped to a database table.

2. **Purpose**:
   - Indicates that the class represents a table in the database.
   - Automatically maps the class fields to table columns, simplifying ORM (Object-Relational Mapping).

3. **Usage in This Project**:
   - Example:
     ```java
     @Entity
     public class Actor {
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long id;

         private String name;
         private LocalDate birthDate;
     }
     ```
   - The `@Entity` annotation ensures that the `Actor` class corresponds to a table named `actor` (or as defined by a custom table name if `@Table` annotation is used).

### Eager vs. Lazy Loading in JPA

1. **Definition**:
   - **Eager Loading**: Automatically fetches the associated data when the parent entity is loaded. It ensures all related data is retrieved in a single query or a series of queries.
   - **Lazy Loading**: Delays fetching the associated data until it is explicitly accessed. This improves performance by only loading data when needed.

2. **Default Behavior**:
   - In JPA, the default fetch type for `@ManyToMany` relationships is **LAZY**.

3. **Advantages and Disadvantages**:
   - **Eager Loading**:
     - **Advantage**: Ensures related data is readily available, avoiding additional queries later.
     - **Disadvantage**: Can lead to unnecessary data being loaded, increasing memory usage.
   - **Lazy Loading**:
     - **Advantage**: Reduces memory usage and initial loading time by loading only what is needed.
     - **Disadvantage**: May cause additional queries at runtime (N+1 query issue).

4. **Usage in This Project**:
   - Example:
     ```java
     @ManyToMany(fetch = FetchType.LAZY)
     private Set<Actor> actors;

     @ManyToMany(fetch = FetchType.EAGER)
     private Set<Genre> genres;
     ```
   - The project uses `LAZY` loading for most relationships to optimize performance while allowing `EAGER` loading when necessary.

## Service Layer

### Role of the Service Layer in a Spring Boot Application

1. **Definition**:
   - The service layer is a layer in the application that contains the business logic. It acts as an intermediary between the controller layer and the data access layer (repository).

2. **Responsibilities**:
   - Encapsulates and centralizes business logic.
   - Provides reusable methods for performing operations on data.
   - Coordinates between different parts of the application (e.g., controllers, repositories).
   - Ensures that controllers focus only on request handling and responses, maintaining separation of concerns.

3. **Usage in This Project**:
   - Example service class for movies:
     ```java
     @Service
     public class MovieService {
         private final MovieRepository movieRepository;

         public MovieService(MovieRepository movieRepository) {
             this.movieRepository = movieRepository;
         }

         public List<Movie> getAllMovies() {
             return movieRepository.findAll();
         }

         public Movie createMovie(Movie movie) {
             return movieRepository.save(movie);
         }
     }
     ```
   - The `MovieService` class ensures that the controller does not directly interact with the repository, maintaining a clean and modular structure.

## Application Properties

### Purpose of the `application.properties` File

1. **Definition**:
   - The `application.properties` file is a configuration file used in Spring Boot projects to specify application-level properties.

2. **Purpose**:
   - Centralizes application configuration.
   - Allows customization of the application without changing the source code.
   - Used to configure properties such as database connections, logging levels, server ports, and more.

3. **SQLite Configuration Example**:
   - Example configuration for connecting to an SQLite database:
     ```properties
     spring.datasource.url=jdbc:sqlite:movie_database.db
     spring.datasource.driver-class-name=org.sqlite.JDBC
     spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
     spring.jpa.show-sql=true
     spring.jpa.hibernate.ddl-auto=update
     ```
   - **Explanation**:
     - `spring.datasource.url`: Specifies the database URL.
     - `spring.datasource.driver-class-name`: Defines the JDBC driver for SQLite.
     - `spring.jpa.database-platform`: Sets the dialect for Hibernate to communicate with SQLite.
     - `spring.jpa.show-sql`: Enables logging of SQL queries executed by Hibernate.
     - `spring.jpa.hibernate.ddl-auto`: Configures how the database schema is initialized or updated (e.g., `update`, `create-drop`).

## Input Validation

### Importance of Input Validation in API Development

1. **Definition**:
   - Input validation ensures that the data provided by users meets the required format, constraints, and business rules before being processed or stored.

2. **Importance**:
   - Prevents invalid or malformed data from causing application errors or database corruption.
   - Enhances security by mitigating risks like SQL injection, XSS attacks, and buffer overflows.
   - Improves user experience by providing immediate feedback on incorrect inputs.
   - Ensures consistency and integrity of data within the system.

3. **Implementation in This Project**:
   - **Using Bean Validation Annotations**:
     - Example:
       ```java
       @Entity
       public class Actor {
           @Id
           @GeneratedValue(strategy = GenerationType.IDENTITY)
           private Long id;

           @NotNull
           @Size(min = 2, max = 50)
           private String name;

           @Past
           private LocalDate birthDate;
       }
       ```
       - `@NotNull`: Ensures the field is not null.
       - `@Size`: Ensures the length of the field is within the specified range.
       - `@Past`: Ensures the date is in the past.

   - **Validating Inputs in Controllers**:
     ```java
     @RestController
     public class ActorController {
         @PostMapping("/api/actors")
         public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
             Actor savedActor = actorService.save(actor);
             return new ResponseEntity<>(savedActor, HttpStatus.CREATED);
         }
     }
     ```
       - `@Valid`: Triggers validation on the input object.
       - Provides clear error messages for invalid inputs.

   - **Custom Error Handling**:
     - Example:
       ```java
       @ControllerAdvice
       public class GlobalExceptionHandler {
           @ExceptionHandler(MethodArgumentNotValidException.class)
           public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
               Map<String, String> errors = new HashMap<>();
               ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
               return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
           }
       }
       ```
       - Captures validation errors and returns a structured error response to the client.

## HTTP Status Codes

### Explanation of HTTP Status Codes in API Responses

1. **Definition**:
   - HTTP status codes are standardized responses that indicate the outcome of an HTTP request.
   - These codes are part of the response header and help clients understand whether their request was successful, resulted in an error, or requires further action.

2. **Purpose**:
   - **Inform Clients**: Provides feedback on the status of their requests.
   - **Enable Debugging**: Helps developers identify and troubleshoot issues.
   - **Improve User Experience**: Ensures users receive appropriate error messages or success confirmations.
   - **Facilitate Automation**: Enables automated tools to handle responses programmatically based on status codes.

3. **Commonly Used HTTP Status Codes in This Project**:
   - **200 OK**: Indicates a successful request (e.g., retrieving a list of movies).
   - **201 Created**: Indicates a resource was successfully created (e.g., adding a new actor).
   - **400 Bad Request**: Indicates a malformed or invalid request (e.g., invalid input validation).
   - **404 Not Found**: Indicates the requested resource does not exist (e.g., fetching a non-existent movie by ID).
   - **500 Internal Server Error**: Indicates a server-side issue.

4. **Example Implementation**:
   - Example: Returning `404 Not Found` for a missing resource:
     ```java
     @GetMapping("/api/movies/{id}")
     public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
         return movieService.findById(id)
             .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))

