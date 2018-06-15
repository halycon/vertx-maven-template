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

    private MongoClient mongoClient;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        JsonObject mongoconfig = new JsonObject()
            .put("connection_string", context.config().getString("mongo.uri"))
            .put("db_name", context.config().getString("mongo.db"));
        logger.info("mongo.db "+context.config().getString("mongo.db") );
        logger.info("mongo.uri "+context.config().getString("mongo.uri") );

        mongoClient = MongoClient.createShared(vertx, mongoconfig);
    }

    @Override
    public void saveAll(JsonArray jobs, Handler<AsyncResult<JsonArray>> resultHandler) {

        jobs.forEach(job -> {
            ((JsonObject) job).put("_id",(((JsonObject) job).getString("id")));
            mongoClient.save("jobs", (JsonObject) job, id -> {
                logger.info("Inserted id: "+((JsonObject) job).getString("id"));
            });
        });
        resultHandler.handle(Future.succeededFuture(jobs));

    }

}
