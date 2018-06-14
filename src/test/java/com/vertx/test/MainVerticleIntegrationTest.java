package com.vertx.test;

import com.vertx.template.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(VertxExtension.class)
public class MainVerticleIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void callGetJobsAndFetchResponse(Vertx vertx, VertxTestContext testContext) {
        WebClient webClient = WebClient.create(vertx);
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
            webClient.get(8080, "localhost", "/getJobs/java/new+york")

                .send(testContext.succeeding(resp -> {
                    jsonResponseBodyCheck(testContext, resp);
                }));
        }));
    }

    @Test
    void callgetJobsAndSortByCompanyNameAndFetchResponse(Vertx vertx, VertxTestContext testContext) {
        WebClient webClient = WebClient.create(vertx);
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
            webClient.get(8080, "localhost", "/getJobsAnsSortByCategory/java/new+york")

                .send(testContext.succeeding(resp -> {
                    jsonResponseBodyCheck(testContext, resp);
                }));
        }));
    }

    private void jsonResponseBodyCheck(VertxTestContext testContext, HttpResponse<Buffer> resp) {
        testContext.verify(() -> {
            assertThat(resp.statusCode()).isEqualTo(200);
            logger.info("response body :: "+resp.bodyAsJsonArray());
            assertThat(resp.bodyAsJsonArray()).isNotNull();
            testContext.completeNow();
        });
    }


}
