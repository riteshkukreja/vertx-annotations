[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.riteshkukreja/vertx-annotations/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.riteshkukreja/vertx-annotations)
[![Build Status](https://travis-ci.com/riteshkukreja/vertx-annotations.svg?branch=master)](https://travis-ci.com/riteshkukreja/vertx-annotations)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/riteshkukreja/vertx-annotations/issues)

# vertx-annotations
Boilerplate code generator for Vert.x applications.

## Introduction
Vert.x applications are great way to build scalable and reactive applications. But you tends to create more boilerplate code with each vertical.
This library aims to reduce those boilerplate code by automatically generating it for your application based on couple of annotations.

## Annotations
### HttpVertical
It's a class based annotation which generates a HttpVertical for all paths targeted inside the class. Paths are targeted using @Get and @Post annotations on  the class methods.

### Get
It's a method based annotation which specifies a Get method route for the parent vertical.

### Post
It's a method based annotation which specifies a Post method route for the parent vertical.


### MessageHandler
It's a method based annotation which generates a EventBusVertical for specified path.

## Usage
1. Add library to your `pom.xml`
```xml

```

# Contributing
Pull requests for new features, bug fixes, and suggestions are welcome!

# License
[MIT](https://github.com/riteshkukreja/vertx-annotations/blob/master/LICENSE)