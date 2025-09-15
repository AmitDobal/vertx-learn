package com.amit.vertx_starter_4.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

public class PointToPointExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new ReceiverVerticle());
    vertx.deployVerticle(new SenderVerticle());
  }

  static class SenderVerticle extends AbstractVerticle {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SenderVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      log.debug("Sending...");
      vertx.setPeriodic(1000, id -> vertx
        .eventBus().send(SenderVerticle.class.getName(), "Sending a message..."));

    }
  }

  static class ReceiverVerticle extends AbstractVerticle {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReceiverVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(SenderVerticle.class.getName(), message -> {
        log.debug("Received message: {}", message.body());
      });
    }
  }
}
