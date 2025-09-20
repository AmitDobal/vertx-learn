package com.amit.vertx_starter_4;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestMainVerticle.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void promise_success(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise = Promise.promise();
    log.debug("Starting test: promise_success");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      log.debug("Success");
      testContext.completeNow();
    });
    log.debug("Ending test: promise_success");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise = Promise.promise();
    log.debug("Starting test: promise_failure");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failure"));
      log.debug("Failure");
      testContext.completeNow();
    });
    log.debug("Ending test: promise_failure");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise = Promise.promise();
    log.debug("Starting test: future_success");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      log.debug("Timer 1 done");
    });
    Future<String> future = promise.future();
    future
      .onSuccess(result -> {
        log.debug("Future succeeded with result: {}", result);
        testContext.completeNow();
      })
      .onFailure(testContext::failNow);


    final Promise<String> promise2 = Promise.promise();
    vertx.setTimer(500, id -> {
      promise2.complete("Success2");
      log.debug("Timer 2 done");
    });
    Future<String> future2 = promise.future();
    future2
      .onSuccess(result -> {
        log.debug("Future succeeded with result2: {}", result);
        testContext.completeNow();
      })
      .onFailure(testContext::failNow);
    log.debug("Ending test: future_success");
  }


  @Test
  void future_map(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise = Promise.promise();
    log.debug("Starting test: future_map");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      log.debug("Timer done");
    });


    Future<JsonArray> jsonArrayFuture = promise.future()
      .map(asString -> {
        log.debug("Mapping String to JSON");
        return new JsonObject().put("message", asString);
      })
      .map(json -> new JsonArray().add(json))
      .onSuccess(result -> {
        log.debug("Future succeeded with result: {}", result);
        testContext.completeNow();
      })
      .onFailure(testContext::failNow);

    jsonArrayFuture.onSuccess(result -> log.debug("Final result: {}", result));
    log.debug("Ending test: future_map");
  }
}
