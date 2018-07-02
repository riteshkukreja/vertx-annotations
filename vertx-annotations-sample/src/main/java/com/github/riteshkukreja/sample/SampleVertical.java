package com.github.riteshkukreja.sample;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.RoutingContext;
import com.github.riteshkukreja.annotations.eventbus.MessageHandler;
import com.github.riteshkukreja.annotations.http.Get;
import com.github.riteshkukreja.annotations.http.HttpVertical;
import com.github.riteshkukreja.annotations.http.Post;

@HttpVertical(value = "/abc", port = 9000)
public class SampleVertical {

    private Vertx vertx;

    public SampleVertical(Vertx vertx) {
        this.vertx = vertx;
    }

    @Get("/do")
    public void doSomthing(RoutingContext context) {
        context.response().end("dsfjbjsdfbjdsfbdjf");
    }

    @Post("/do")
    public void doPostSomthing(RoutingContext context) {
        context.response().end("dsfjbjsdfbjdsfbdjf");
    }

    @MessageHandler("my-address")
    public void messageHandler(Message<String> message) {
        System.out.println("hello");
        message.reply("world");
    }

    @Get("/hit-message-handler")
    public void handleMessageFromHttp(RoutingContext context) {
        vertx.eventBus().send("my-address", "hello", messageAsyncResult -> {
            context.response().end(messageAsyncResult.result().body().toString());
        });
    }

}
