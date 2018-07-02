package com.github.riteshkukreja.sample;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import com.github.riteshkukreja.annotations.http.Get;
import com.github.riteshkukreja.annotations.http.HttpVertical;
import com.github.riteshkukreja.annotations.http.Post;

@HttpVertical
public class UserVertical {

    private Vertx vertx;

    public UserVertical(Vertx vertx) {
        this.vertx = vertx;
    }

    @Get
    public void sayHello(RoutingContext context) {
        context.response().end("Hello Annotations!");
    }

    @Get("/hi")
    @Post("/hi-there-neighbour")
    public void sayHi(RoutingContext context) {
        context.response().end("Hi Annotations!");
    }

}
