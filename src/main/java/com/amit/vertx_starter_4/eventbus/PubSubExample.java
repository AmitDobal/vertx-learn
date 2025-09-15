package com.amit.vertx_starter_4.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.time.Duration;

public class PubSubExample {
  public static void main(String[] args) {

    var vertx = io.vertx.core.Vertx.vertx();
    vertx.deployVerticle(new Subscriber1());
    vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
    vertx.deployVerticle(new Publisher());
  }

  static class Publisher extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(Duration.ofSeconds(5).toMillis(), id ->
        vertx.eventBus().publish(Publisher.class.getName(), "A message for all subscribers"));


    }
  }

  public static class Subscriber1 extends AbstractVerticle {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Subscriber1.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publisher.class.getName(), message -> {
        log.debug("Received message1: {}", message.body());
      });
    }
  }

  public static class Subscriber2 extends AbstractVerticle {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Subscriber2.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publisher.class.getName(), message -> {
        log.debug("Received message2: {}", message.body());
        var person = new Person("Amit", 20);
        var json = JsonObject.mapFrom(person);
        log.debug("Person as JSON: {}", json);
      });
    }
  }
}

record Person(String name, int age) {
}
