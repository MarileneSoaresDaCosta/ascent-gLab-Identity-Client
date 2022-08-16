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
        ```java
        - name: JWT_SECRET_KEY
          valueFrom:
          secretKeyRef:
            name: jwt-key-secret
            key: JWT_SECRET_KEY
        ```
    - `WebSecurityConfig` - Modify the `configure()` and `corsConfiguration()` methods according to your environment.
