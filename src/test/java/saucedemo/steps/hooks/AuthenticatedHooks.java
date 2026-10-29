package saucedemo.steps.hooks;

import io.cucumber.java.Before;
import saucedemo.support.LoginActions;

public class AuthenticatedHooks {
    private final LoginActions loginActions;

    public AuthenticatedHooks(LoginActions loginActions) {
        this.loginActions = loginActions;
    }

    @Before(value = "@authenticated", order = 10)
    public void loginAsStandardUser() {
        loginActions.loginAsStandardUser();
    }
}
