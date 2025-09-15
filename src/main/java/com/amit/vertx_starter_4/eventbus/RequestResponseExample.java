package com.amit.vertx_starter_4.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {
  static final String ADDRESS = "request.address";
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new ResponseVerticle());
    vertx.deployVerticle(new RequestVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      String message = "Hello from RequestVerticle";
      log.debug("Sending message: {}", message);
      eventBus.<String>request(ADDRESS, message, reply -> {
        if (reply.succeeded()) {
          log.debug("Received reply: {}", reply.result().body());
        } else {
          log.error("Failed to receive reply", reply.cause());
        }
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      eventBus.<String>consumer(ADDRESS, message -> {
        log.debug("Received message: {}", message.body());
        String replyMessage = "Hello from ResponseVerticle";
        message.reply(replyMessage);
        log.debug("Sent reply: {}", replyMessage);
      });
    }
  }
}
