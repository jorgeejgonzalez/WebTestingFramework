#!/usr/bin/env bash
echo "OVERWRITING THE TESTCONFIG PROPERTIES FILE WITH THE LOCAL HTMLUNIT WEBDRIVER"
cp -f src/test/resources/properties/TestConfig-LocalHtmlUnit.properties src/test/resources/properties/TestConfig.properties
