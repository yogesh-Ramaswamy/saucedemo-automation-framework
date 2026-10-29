package saucedemo.steps.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import saucedemo.driver.SharedDriver;

public class ScenarioHooks {
    private final SharedDriver sharedDriver;

    public ScenarioHooks(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @Before(order = 0)
    public void startDriver() {
        sharedDriver.start();
    }

    @After(order = 0)
    public void captureFailureAndQuit(Scenario scenario) {
        try {
            if (scenario.isFailed() && sharedDriver.isStarted()) {
                scenario.attach(
                        sharedDriver.screenshotBytes(),
                        "image/png",
                        scenario.getName());
            }
        } finally {
            sharedDriver.quit();
        }
    }
}
