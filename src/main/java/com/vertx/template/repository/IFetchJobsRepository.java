package com.vertx.template.repository;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

@ProxyGen
public interface IFetchJobsRepository {

    void saveAll(JsonArray jobs , Handler<AsyncResult<JsonArray>> resultHandler);

}
