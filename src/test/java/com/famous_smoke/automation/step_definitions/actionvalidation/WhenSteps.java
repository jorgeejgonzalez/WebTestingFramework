package com.famous_smoke.automation.step_definitions.actionvalidation;

import com.famous_smoke.automation.Hooks;
import com.famous_smoke.automation.actions.CheckBreadcrumbsAction;
import cucumber.api.java.en.When;


/**
 * Created by jorge on 12-01-2016.
 */
public class WhenSteps {

    @When("^I check all the breadcrumbs$")
    public void i_check_all_the_breadcrumbs() throws Throwable {
        Hooks.testBasePageData = CheckBreadcrumbsAction.execute();
    }

}
