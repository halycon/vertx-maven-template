package com.vertx.template.service.impl;

import com.vertx.template.repository.IFetchJobsRepository;
import com.vertx.template.repository.impl.FetchJobsMongoRepository;
import com.vertx.template.service.IFetchJobsService;
import io.vertx.core.*;
import io.vertx.core.impl.ConversionHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import java.util.Comparator;
import java.util.stream.Collectors;

public class FetchGitHubJobsService extends AbstractVerticle implements IFetchJobsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private WebClient client;

    private IFetchJobsRepository fetchJobsMongoRepository;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        client = WebClient.create(vertx, new WebClientOptions()
            .setSsl(true).setTrustAll(true));
        fetchJobsMongoRepository = new FetchJobsMongoRepository();
        vertx.deployVerticle((Verticle) fetchJobsMongoRepository);
    }

    @Override
    public void getJobs(String cityName, String description, Handler<AsyncResult<JsonArray>> resultHandler) {
        client = WebClient.create(vertx, new WebClientOptions()
            .setSsl(true).setTrustAll(true));
        client.get(443, "jobs.github.com", "/positions.json?location=param1_value&description=param2_value")
            .ssl(true)
            .addQueryParam("location", cityName)
            .addQueryParam("description", description)
            .send(ar -> {
                if (ar.succeeded()) {
                    JsonArray response = ar.result().bodyAsJsonArray();
                    fetchJobsMongoRepository.saveAll(response, res2 -> {
                        if (res2.succeeded()) {
                            resultHandler.handle(Future.succeededFuture(response));
                        } else {
                            resultHandler.handle(Future.succeededFuture());
                        }
                    });


                } else {
                    resultHandler.handle(Future.succeededFuture());
                }
            });
    }

    @Override
    public void getJobsAndSortByCompanyName(String cityName, String description, Handler<AsyncResult<JsonArray>> resultHandler) {

        client.get(443, "jobs.github.com", "/positions.json?location=param1_value&description=param2_value")
            .ssl(true)
            .addQueryParam("location", cityName)
            .addQueryParam("description", description)
            .send(ar -> {
                if (ar.succeeded()) {
                    JsonArray response = ar.result().bodyAsJsonArray();


                    response = ConversionHelper.toJsonArray(response.stream().sorted(
                        Comparator.comparing(a ->  ((JsonObject) a).getString("company"))
                    ).collect(Collectors.toList()));


                    JsonArray finalResponse = response;
                    fetchJobsMongoRepository.saveAll(response, res2 -> {
                        if (res2.succeeded()) {
                            resultHandler.handle(Future.succeededFuture(finalResponse));
                        } else {
                            resultHandler.handle(Future.succeededFuture());
                        }
                    });


                } else {
                    resultHandler.handle(Future.succeededFuture());
                }
            });

    }

}
