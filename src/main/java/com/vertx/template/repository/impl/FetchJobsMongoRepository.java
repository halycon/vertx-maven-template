package com.vertx.template.repository.impl;

import com.vertx.template.repository.IFetchJobsRepository;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;


public class FetchJobsMongoRepository extends AbstractVerticle implements IFetchJobsRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    @Override
    public void saveAll(JsonArray jobs, Handler<AsyncResult<JsonArray>> resultHandler) {

        MongoClient mongoClient = MongoClient.createShared(vertx, prepareDBConfig());

        jobs.forEach(job -> {
            ((JsonObject) job).put("_id",(((JsonObject) job).getString("id")));
            mongoClient.save("jobs", (JsonObject) job, id -> {
                logger.info("Inserted id: "+((JsonObject) job).getString("id"));
            });
        });
        resultHandler.handle(Future.succeededFuture(jobs));

    }

    public JsonObject prepareDBConfig(){
        JsonObject mongoconfig = new JsonObject()
            .put("connection_string", "mongodb://localhost:27017")
            .put("db_name", "prod");
        return mongoconfig;
    }
}
