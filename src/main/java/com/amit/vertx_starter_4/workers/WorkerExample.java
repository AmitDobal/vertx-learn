package com.amit.vertx_starter_4.workers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {

    startPromise.complete();

    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions().setWorker(true)
        .setWorkerPoolName("custom-worker-pool")
        .setWorkerPoolSize(1));

    executeBlockingCode();

  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      log.debug("Starting a blocking code");
      try {
        Thread.sleep(5000);
        event.complete("");
      } catch (InterruptedException e) {
        log.error("Failed to execute blocking code", e);
        event.fail(e);
      }
    }, res -> {
      if (res.succeeded()) {
        log.debug("Blocking code finished");
      } else {
        log.error("Blocking code failed", res.cause());
      }
    });
  }
}
