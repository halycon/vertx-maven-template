package com.vertx.template;

import com.vertx.template.service.IFetchJobsService;
import com.vertx.template.service.impl.FetchGitHubJobsService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class MainVerticle extends AbstractVerticle {

    private IFetchJobsService fetchJobsService;

    private JsonArray emptyJsonArray;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        emptyJsonArray = new JsonArray();
        fetchJobsService = new FetchGitHubJobsService();
        vertx.deployVerticle((Verticle) fetchJobsService);
    }


    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/getJobs/:description/:cityName").handler(this::getJobs);
        router.get("/getJobsAnsSortByCategory/:description/:cityName").handler(this::getJobsAndSortByCategory);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void getJobsAndSortByCategory(RoutingContext routingContext) {
        String description = routingContext.request().getParam("description");
        String cityName = routingContext.request().getParam("cityName");

        HttpServerResponse response = routingContext.response();
        if (description == null || cityName==null) {
            response.setStatusCode(400).end();
        } else {
            fetchJobsService.getJobsAndSortByCompanyName(cityName,description,res->{
                if(res.succeeded()){
                    routingContext.response().putHeader("content-type", "application/json").end(res.result().encodePrettily());
                }else{
                    routingContext.response().putHeader("content-type", "application/json").end(emptyJsonArray.encodePrettily());
                }
            });
        }

    }

    private void getJobs(RoutingContext routingContext) {
        String description = routingContext.request().getParam("description");
        String cityName = routingContext.request().getParam("cityName");

        HttpServerResponse response = routingContext.response();
        if (description == null || cityName==null) {
            response.setStatusCode(400).end();
        } else {
            fetchJobsService.getJobs(cityName,description,res->{
                if(res.succeeded()){
                    routingContext.response().putHeader("content-type", "application/json").end(res.result().encodePrettily());
                }else{
                    routingContext.response().putHeader("content-type", "application/json").end(emptyJsonArray.encodePrettily());
                }
            });
        }

    }
}