Feature: The Brand Pages have a Header One matching the extracted Data

  Scenario Outline: I will validate the Brand Header One
    Given I want to the check the content of the url "<URL>"
    And the url is from a brand page
    When I navigate to the page
    Then the header one should not be empty
    And the header one should match the TestData
    Examples:
      | URL |
  <LOAD_BRAND_URLS>