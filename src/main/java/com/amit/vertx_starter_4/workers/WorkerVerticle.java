package com.amit.vertx_starter_4.workers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(WorkerVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.debug("Starting Worker Verticle");
    startPromise.complete();

    Thread.sleep(5000); // Simulate blocking operation
    log.debug("Worker Verticle finished blocking operation");
  }
}
