package ar.edu.unq.wheatherMetrics;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetLastWeekAverageTemperatureSimulation extends Simulation {

    private final HttpProtocolBuilder httpConnection = http
            .baseUrl("http://localhost:8080/weather-metrics-component")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "1000"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "5"));

    @Override
    public void before() {
        System.out.printf("Running test with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
    }

    private static final ChainBuilder getCurrentTemperature = exec(http("GET last week average temperature").get("/last-week/temperature/average"));

    private final ScenarioBuilder scenarioBuilder = scenario("Weather-Metrics-App: Get Last Week Average Temperature Endpoint Stress Test")
            .exec(getCurrentTemperature);

    // Load Simulation
    {
        setUp(
                scenarioBuilder.injectOpen(
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                )
        ).protocols(httpConnection);
    }
}
