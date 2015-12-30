Feature: In every Page, all breadcrumbs go to correct links

  Scenario Outline: I will validate the navigation of the breadcrumbs
    Given I want to check the breadcrumbs of "<URL>"
    When I check all the breadcrumbs
    Then the navigation must be valid

    Examples:
    | URL |
    <LOAD_URLS>