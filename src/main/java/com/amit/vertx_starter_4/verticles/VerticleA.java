package com.amit.vertx_starter_4.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleA extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(VerticleA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.debug("Start: {}", getClass().getName());
    vertx.deployVerticle(new VerticleAA(), res -> {
      if (res.succeeded()) {
        log.debug("Deployed: {}", VerticleAA.class.getName());
        vertx.deployVerticle(new VerticleAB());
      } else {
        log.error("Failed to deploy: {}", VerticleAA.class.getName(), res.cause());
      }
    });

//    startPromise.complete();
    log.debug("End: {}", getClass().getName());
  }
}
