@seo
Feature: The Brand Pages have a visible Logo

  Scenario Outline: I will check the Brand Logo
    Given I want to the check the content of the url "<URL>"
    And the url is from a brand page
    When I navigate to the page
    Then the brand logo should be visible
    Examples:
      | URL |
  <LOAD_BRAND_URLS>