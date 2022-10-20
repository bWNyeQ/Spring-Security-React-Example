# Simple Spring Security example
In this example we highlight how to use basic authentication methods for a react app.

First of all, place your built react project inside the `main/resources/static` folder.

To build a react app, use this command `npm run build`, then inside the `build` folder is the *compiled* react project,
copy this to the static folder. 

## Short info about Spring Security
When you add the dependency `spring-boot-starter-security` the whole app will be placed
behind a login form. Since we want to expose some features of our app, we need to add a config file.
In this case we have named it `SecurityConfig`. The `filterChain` is the configuration for access control to the system.

By default, spring security uses `RBAC` as the access control schema.

```java
        http.csrf().disable() // <-- This tells the app to disable some security features not needed for our app.
        .authorizeRequests() // Tells us to begin the spec the AC
        .antMatchers(HttpMethod.GET,"/", "/static/**").permitAll() //A simple grant statement, matches endpoint to this string, and http method. then sets the access
        .antMatchers(HttpMethod.POST ,"/api/signup").permitAll()
        .antMatchers("/api/admin/**").hasRole("ADMIN") // A grant permission statement such that only the ADMIN role can access this endpoint
        .anyRequest().authenticated() // The default or catch, any other request needs to be verified(no specific role, i.e both users and admins can access these)
        .and() // Just to say that we are done with defining the AC
        .formLogin(); // Tells the system we want to use a form
```

Quick note, on the endpoint `/login` there will be an extra form, this can be disabled, but for simplicity you can keep it.
the `.formLogin()` generates it, but we can use it to submit login data just to make the authentication simple.


The followin code snipet tells Spring Security to use JDBC for sotring user info. i.e we can specify the
connection to the DB in the `application.props` file so that we don't have to change any code if we want to use another db etc.
```java
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        return users;
    }
```

We can use this following snipet to create a new user, it will be automatically added to the db if we use the JDBC... as shown above

```java
@Autowired
UserDetailsManager userDetailsManager;
            
...
            
UserDetails user = User.builder()
                    .username(data.getEmail())
                    .password(passwordEncoder.encode(data.getPass()))
                    .roles("USER")
                    .build();

userDetailsManager.createUser(user);
```

You can use this followin snipet to access basic info about the current auth
```java
    public String info(Authentication auth){
        return auth.getName(); // Returns the username of the currently logged in user.
    }
```

## Quick note / Warning!
You can do essentially anything related to auth* with spring security. But it becomes quite complex
fast! So this example is a "hacky" way to use it, it works, it provides security, but is not really optimal for a SPA.
It is "hacky" because I have tried to make it as simple as possible. 

**Warning!!!** Most info on how to use spring security is outdated on the web/stackoverflow! 
Try either to stick to this example or look at the official docs, 
even though they are hard to read. Trying to make something custom and google how to 
_force_ spring security to fit your implementation will be a rabbit hole with outdated blogposts
and filled with solutions which doesn't work. I have spent hours on compiling sources just to 
come up with this simple example.

