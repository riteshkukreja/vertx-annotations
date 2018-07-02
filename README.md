[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.riteshkukreja/vertx-annotations/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.riteshkukreja/vertx-annotations)
[![Build Status](https://travis-ci.com/riteshkukreja/vertx-annotations.svg?branch=master)](https://travis-ci.com/riteshkukreja/vertx-annotations)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/riteshkukreja/vertx-annotations/issues)
[![Issues](https://img.shields.io/github/issues-raw/riteshkukreja/vertx-annotations.svg?maxAge=2592000)](https://github.com/riteshkukreja/vertx-annotations/issues)


# vertx-annotations
Boilerplate code generator for Vert.x applications.

## Introduction
Vert.x applications are great way to build scalable and reactive applications. But you tends to create more boilerplate code with each vertical.
This library aims to reduce those boilerplate code by automatically generating it for your application based on couple of annotations.

## Annotations
### @HttpVertical
It's a class based annotation which generates a HttpVertical for all paths targeted inside the class. Paths are targeted using @Get and @Post annotations on  the class methods.

### @Get
It's a method based annotation which specifies a Get method route for the parent vertical.

### @Post
It's a method based annotation which specifies a Post method route for the parent vertical.

### @MessageHandler
It's a method based annotation which generates a EventBusVertical for specified path.

## Sample App
1. Add dependency to your `pom.xml`
```xml
<dependency>
  <groupId>com.github.riteshkukreja</groupId>
  <artifactId>vertx-annotations</artifactId>
  <version>1.0.0</version>
</dependency>
```

2. Create a file named `UserHandler.java` and insert following code
```java

@HttpVertical("/user")
public class UserHandler {

    @Get("/")
    public void sayHello(RoutingContext context) {
        context.response().end("Hello");
    }

}

```

3. Set generated vertical as your starting vertical.
```xml
<properties>
    <main.verticle>gen.verticals.MainVerticalGen</main.verticle>
</properties>

<plugins>
    <!--  Add Maven shade plugin to generate fat-jar -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.3</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
                <configuration>
                    <transformers>
                        <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                            <manifestEntries>
                                <Main-Class>io.vertx.core.Launcher</Main-Class>
                                <Main-Verticle>${main.verticle}</Main-Verticle>
                            </manifestEntries>
                        </transformer>
                    </transformers>
                    <outputFile>${project.build.directory}/${project.artifactId}-${project.version}-fat.jar</outputFile>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

4. Compile and run your fat-jar
```bash
java -jar <fat-jar>.jar
```

# Contributing
Pull requests for new features, bug fixes, and suggestions are welcome!

# License
This project is licensed under [MIT](https://github.com/riteshkukreja/vertx-annotations/blob/master/LICENSE)