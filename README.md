# Selenium Java TestNG Framework

[![CI](https://github.com/dilshana-dasthakeer/selenium-java-testng-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/dilshana-dasthakeer/selenium-java-testng-framework/actions/workflows/ci.yml)
![Java 17](https://img.shields.io/badge/Java-17-orange)
![Selenium 4](https://img.shields.io/badge/Selenium-4.35-43B02A)
![TestNG](https://img.shields.io/badge/TestNG-7.11-red)
![License: MIT](https://img.shields.io/badge/License-MIT-blue)

A UI test automation framework in Java, built the way I structure automation on enterprise projects: small page objects, tests that read like requirements, configuration outside the code, and a pipeline that runs everything in three browsers.

It tests two public practice sites, [Sauce Demo](https://www.saucedemo.com) (login, products, cart, checkout) and [The Internet](https://the-internet.herokuapp.com) (dropdowns, dynamic loading, web tables).

## What it shows

| Area | How it is done |
|---|---|
| **Design** | Page Object Model. Pages hold locators and actions, tests hold assertions. |
| **Waiting** | Explicit waits only. There are no `Thread.sleep` calls. |
| **Parallel runs** | TestNG runs methods in parallel. Each thread owns its browser through a `ThreadLocal` driver. |
| **Cross-browser** | Chrome, Firefox and Edge, chosen with `-Dbrowser=`. Headless mode for CI. |
| **Selenium Grid** | `docker compose up` starts a hub with all three browser nodes. Point the tests at it with `-Dgrid.url=`. |
| **Data-driven tests** | TestNG data providers read JSON files from `src/test/resources/testdata`. |
| **Configuration** | Per-environment `.properties` files. Any value can be overridden by a `-D` flag or an environment variable. |
| **Flaky tests** | A retry analyzer applied to every test. Retries are logged so flakiness stays visible. |
| **Reporting** | Allure report with steps, severity, and a screenshot and URL attached to every failure. |
| **Suites** | `smoke` runs the critical paths. `regression` runs everything. |
| **CI** | GitHub Actions: smoke gate, then regression in a Chrome, Firefox and Edge matrix, then the Allure report published to GitHub Pages. Runs on every push, pull request and nightly. |
| **Modern Selenium** | Selenium 4 with Selenium Manager, so no WebDriverManager dependency. |

## Tech stack

Java 17 · Selenium 4 · TestNG · AssertJ · Allure · Jackson · SLF4J + Logback · Maven · GitHub Actions · Docker

## Project structure

```
src/
├── main/java/com/dilshana/qa/
│   ├── config/        ConfigManager, Settings, Browser
│   ├── driver/        DriverFactory (local and Grid), DriverManager (ThreadLocal)
│   ├── pages/         BasePage and one class per page
│   ├── data/          JSON reader and typed test-data records
│   └── utils/         Screenshot helper
└── test/
    ├── java/com/dilshana/qa/
    │   ├── base/          BaseTest (browser start and stop)
    │   ├── listeners/     TestListener, RetryAnalyzer, AnnotationTransformer
    │   └── tests/         LoginTest, InventoryTest, CheckoutTest, WebElementsTest
    └── resources/
        ├── config/        qa.properties, ci.properties
        ├── suites/        smoke.xml, regression.xml
        └── testdata/      invalid-logins.json, checkout-errors.json
```

## Getting started

You need Java 17 or newer, Maven, and Chrome, Firefox or Edge installed.

```bash
git clone https://github.com/dilshana-dasthakeer/selenium-java-testng-framework.git
cd selenium-java-testng-framework

mvn test                                   # regression suite, Chrome, visible browser
mvn test -Dsuite=smoke                     # critical paths only
mvn test -Dbrowser=firefox                 # chrome | firefox | edge
mvn test -Dheadless=true                   # no browser window
mvn test -Denv=ci                          # use config/ci.properties
```

### Allure report

```bash
mvn allure:serve        # builds the report from target/allure-results and opens it
```

### Selenium Grid

```bash
docker compose up -d                       # hub plus Chrome, Firefox and Edge nodes
mvn test -Dgrid.url=http://localhost:4444 -Dbrowser=firefox
docker compose down
```

The Grid console is at http://localhost:4444.

## Configuration

Values come from `src/test/resources/config/{env}.properties`. The first match wins: a `-D` system property, then an environment variable (`BASE_URL`, `HEADLESS` and so on), then the file.

| Key | Meaning | Default (`qa`) |
|---|---|---|
| `base.url` | Sauce Demo address | `https://www.saucedemo.com` |
| `internet.url` | The Internet address | `https://the-internet.herokuapp.com` |
| `browser` | `chrome`, `firefox` or `edge` | `chrome` |
| `headless` | Run without a window | `false` |
| `grid.url` | Selenium Grid address. Empty means run locally. | empty |
| `timeout.explicit.seconds` | Explicit wait | `10` |
| `retry.max` | Retries for a failed test | `1` |

## Test coverage

| Class | Scenarios |
|---|---|
| `LoginTest` | Valid login, four invalid-login cases from JSON, log out, protected page redirects to login |
| `InventoryTest` | Product count, price and name sorting, cart badge on add and remove, cart contents |
| `CheckoutTest` | End-to-end purchase, subtotal equals item prices, total equals subtotal plus tax, three form-validation cases from JSON |
| `WebElementsTest` | Dropdown, dynamically loaded element, sortable web table |

## Design decisions

- **Pages return pages.** `loginAs()` returns an `InventoryPage`, so the compiler guides the flow and tests read top to bottom.
- **No driver in test fields.** TestNG shares one test-class instance across parallel methods, so a driver field would be overwritten by another thread. Tests call `driver()`, which reads the `ThreadLocal`.
- **Retries are a safety net, not a fix.** The analyzer logs every retry. A test that needs retries often is a bug to chase.
- **Test data lives in JSON.** Adding a case means adding a row, not code.
- **Practice sites, not Google.** Sites that block automation make a poor demo and a flaky pipeline.

## Adding a test

1. Add or extend a page class in `pages/`. Locators are private constants, actions are public methods.
2. Add a test method in the matching class in `tests/`. Add `groups = "regression"`, plus `"smoke"` if it guards a critical path.
3. Run `mvn test -Dsuite=regression`.

## Author

**Dilshana Dasthakeer**, Senior Software QA Engineer, ISTQB CTFL.
[GitHub](https://github.com/dilshana-dasthakeer) · [Linktree](https://linktr.ee/Dilshana6)

Licensed under the MIT License.
