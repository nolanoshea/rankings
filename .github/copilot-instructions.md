# AI Agent Instructions for Rankings Project

## Project Overview
This is a Spring Boot 3.5.4 application that uses Spring AI and Redis for implementing a rankings system with vector-based search capabilities. The project uses reactive programming with WebFlux and integrates with OpenAI for AI features.

## Key Technologies
- Java 21
- Spring Boot 3.5.4
- Spring WebFlux (reactive web)
- Spring AI (1.0.0) with OpenAI integration
- Redis for reactive data storage and vector search
- Maven for build management

## Project Structure
```
src/
  main/
    java/com/nolan/rankings/      # Main application code
    resources/
      application.properties      # Spring Boot configuration
  test/
    java/com/nolan/rankings/      # Test classes
```

## Development Workflow

### Building
```bash
./mvnw clean install
```

### Running Tests
Tests use JUnit 5 with Mockito. Note the custom Mockito configuration in Maven Surefire plugin.

### Configuration Patterns
1. AI Model configuration in `application.properties`:
   - All AI models (audio, chat, embedding, etc.) are currently disabled
   - Configure by setting appropriate `spring.ai.model.*` properties

2. Redis Integration:
   - Uses Spring Data Redis Reactive for data operations
   - Vector store capabilities through Spring AI Redis integration
   - Supports Redis Search for vector similarity queries

   Local Development Setup:
   ```bash
   # Start Redis using Docker (requires Docker installed)
   ./scripts/start-redis.sh
   ```

   Configuration in application.properties:
   ```properties
   # Redis connection
   spring.data.redis.host=localhost
   spring.data.redis.port=6379
   spring.data.redis.database=0

   # Vector store settings
   spring.ai.vectorstore.redis.index-name=rankings
   spring.ai.vectorstore.redis.dimension=1536
   spring.ai.vectorstore.redis.prefix=doc
   ```

   Troubleshooting:
   - View Redis logs: `docker logs rankings-redis`
   - Stop Redis: `docker stop rankings-redis`
   - Redis CLI: `docker exec -it rankings-redis redis-cli`

## Integration Points
1. OpenAI Integration:
   - Configured through Spring AI starter
   - Refer to `spring-ai-starter-model-openai` dependency

2. Redis Vector Store:
   - Used for vector-based search and storage
   - Configured through `spring-ai-starter-vector-store-redis`

## Common Patterns
1. Reactive Programming:
   - Use `Mono` and `Flux` types from Project Reactor
   - Follow reactive patterns for all data operations

2. Test Configuration:
   - Tests are annotated with `@SpringBootTest`
   - Use reactive test utilities from `reactor-test`

## Important Notes
- Parent POM inheritance overrides are used to prevent unwanted element inheritance
- gRPC support is available but not currently configured
- Security Setup and Configuration:
  1. Spring Security is available with WebFlux support
  2. Required configuration:
     - Create a SecurityConfig class with @EnableWebFluxSecurity
     - Configure SecurityWebFilterChain bean for route security
     - Example security patterns:
       ```java
       @Configuration
       @EnableWebFluxSecurity
       public class SecurityConfig {
           @Bean
           public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
               return http
                   .csrf(csrf -> csrf.disable())  // For API endpoints
                   .authorizeExchange(auth -> auth
                       .pathMatchers("/api/public/**").permitAll()
                       .anyExchange().authenticated()
                   )
                   .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                   .build();
           }
       }
       ```
  3. OAuth2/JWT support is available - see application.properties setup:
     ```properties
     spring.security.oauth2.resourceserver.jwt.issuer-uri=
     spring.security.oauth2.resourceserver.jwt.jwk-set-uri=
     ```