# gLab Security Client

Demo security client with a starter JWT Authentication Filter.  Works with the gLab Security Provider project

To use this demo, clone the gLab Security Provider from the same org that you got this project.  Start up that service using
accepted methods (`./gradlew bootRun works well`).   There is an endpoint that lists the configured users, `/users`, note
that all password are simply `password` for demonstration purposes.

To make use of this demonstration, make a post request to the provider with the body 
```json
{
  "username": "username",
  "password": "password"
}
```
Upon successful authentication, you should receive a header with a JWT.  Use this Token to authenticate with Bearer authentication to this server 
with the following endpoints...

- `/hello` - Does not require any authentication
- `/user` - Requires the role 'USER'
- `/admin` - Requires the role 'ADMIN'

Examples of Annotation based access control can be found in the `com.galvanize.controller.HelloController` class.  Configuration based access control, 
can be found in `com.galvanize.security.WebSecurityConfig`, among the rest of the security configuration.

## To implement JWT security in your own application if required...
1. Add the following dependencies to your build.gradle
  - `implementation 'org.springframework.boot:spring-boot-starter-security'`
  - `implementation group: 'io.jsonwebtoken', name: 'jjwt', version: '0.9.0'`
  - `implementation group: 'javax.xml.bind', name: 'jaxb-api', version: '2.3.1'`

1. Copy the package `com.galvanize.security` to your project 
2. Modify the following accordingly for your environment.
    - `application.properties` - Modify the properties starting with `security.jwt.*` noting 
       that `security.jwt.secret` must match the secret used in your provider.
      - `application.properties` entry `security.jwt.secret=${JWT_SECRET_KEY}`
      - `deploy.yaml` env entry
        ```yaml
        - name: JWT_SECRET_KEY
          valueFrom:
          secretKeyRef:
            name: jwt-key-secret
            key: JWT_SECRET_KEY
        ```
    - `WebSecurityConfig` - Modify the `configure()` and `corsConfiguration()` methods according to your environment.

## Using Method security

You can further restrict access to your endpoints by adding [Spring's Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html).


## Integration and Unit Tests

After adding Security to your test, you will note that any tests that go through the HTTP layer, will fail
due to an authentication exception.  The test user does not have permission to access the resource. You will 
now be responsible for adding a user for your tests.

1. Add the Spring security test dependency to your `build.gradle`
    ```java 
   `testImplementation 'org.springframework.security:spring-security-test'`
   ```
2. Identify the user on your unit tests with `@WithMockUser` annotation.  See the [Spring documentation](https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html) for all of it's capabilities.

## CORs
Details comming Soon!

- [MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS/Errors)
- [Spring - Enabling Cross Origin Requests for a RESTful Web Service](https://spring.io/guides/gs/rest-service-cors/)

Add the following to your WebSecurityConfig
```java 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("*","Authorization"));
        configuration.setAllowCredentials(true);
        configuration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
```
