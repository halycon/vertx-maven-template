package com.vertx.template.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

public interface IFetchJobsService {
    void getJobs(String cityName, String description, Handler<AsyncResult<JsonArray>> resultHandler);

    void getJobsAndSortByCompanyName(String cityName, String description, Handler<AsyncResult<JsonArray>> resultHandler);
}
