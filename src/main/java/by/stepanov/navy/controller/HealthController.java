package by.stepanov.navy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/health", tags = {"Checking service statement"})
public class HealthController {

    @GetMapping("/health")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal service error")
    })
    public HealthResponse getHealth() {
        final HealthResponse healthResponse = new HealthResponse();
        healthResponse.setStarted(true);
        return healthResponse;
    }

    private class HealthResponse {
        private boolean started;

        public boolean isStarted() {
            return started;
        }

        public void setStarted(boolean started) {
            this.started = started;
        }
    }
}
