# Test strategy

How I decide what to test, at which level, and how to keep the suite trustworthy. The examples come from the four public frameworks in this portfolio: [Selenium](https://github.com/dilshana-dasthakeer/selenium-java-testng-framework), [Playwright](https://github.com/dilshana-dasthakeer/playwright-typescript-framework), [Cucumber BDD](https://github.com/dilshana-dasthakeer/cucumber-bdd-framework) and [REST-assured](https://github.com/dilshana-dasthakeer/restassured-api-framework).

## 1. Test where it is cheapest to catch the problem

| Level | What belongs here | Example in this portfolio |
|---|---|---|
| API | Business rules, validation, permissions, data changes | REST-assured: create, update and delete a booking, and who may do it |
| UI | The few journeys a user cannot live without, and anything only the browser can break | Sign in, add to cart, check out |
| Exploratory (manual) | New features, usability, odd combinations, anything still changing | Every sprint, before automating |

A rule that can be checked through the API is not checked again through the browser. UI tests are slower and more fragile, so they are kept for journeys and for behaviour that lives in the page.

## 2. What to automate, and what not to

Automate when a check is repeated every release, has a clear expected result, and is stable. Do not automate a feature that is still changing shape, a one-off check, or something a person judges better by looking (layout, wording, feel).

Order of priority: money and data loss first, then sign-in and access, then the main user journey, then everything else.

## 3. Two suites, two jobs

- **Smoke** is a small set that answers "is this build worth testing further?" It runs first in CI and stops the pipeline when it fails. It stays under a few minutes.
- **Regression** is the full suite. It runs on every push, and nightly against the shared environment.

Every test is tagged, so a developer can run the smoke set before pushing and a release can run everything.

## 4. Keeping tests reliable

A suite nobody trusts is worse than no suite. The rules I follow:

- **No fixed sleeps.** Every wait is for a condition, such as an element being clickable.
- **Independent tests.** Each test creates what it needs and removes it afterwards, so tests can run in any order and in parallel.
- **Unique test data.** Names carry a random suffix, so a test never finds another test's data.
- **One place for locators.** They live in page objects, so a UI change means one edit.
- **Retries are a signal, not a fix.** A retry keeps one flaky failure from blocking a release, but a test that needs retries is logged and repaired. Retries never hide a test that fails every time.
- **Evidence on failure.** Screenshots, traces and logs are attached, so a failure can be diagnosed without re-running it.

## 5. Test data and environments

Configuration (URLs, credentials, browser) is held outside the code and overridden per run, so the same suite runs against local, test and staging. Data is created through the API where possible, because it is faster and more reliable than building it through the UI. Real customer data is never used.

## 6. Reporting and decisions

Each run produces a report (Allure for the Selenium repo, HTML and JUnit for the others) kept as a CI artifact. Results should answer three questions quickly: what failed, is it a product bug or a test problem, and is the build safe to release? A failure is triaged the same day. A product bug gets a defect with steps, evidence and severity. A test problem is fixed or quarantined with a ticket, never left red.

## 7. Where AI helps

I use AI to find gaps in existing coverage, to draft test cases from requirements, and to speed up writing automation scripts. Everything it produces is reviewed like any other code: the expected results are checked against the requirement, and the test is run and made to fail on purpose once to prove it can catch the problem.

## 8. Risks I watch for

- Tests that pass because they assert too little. Fix: check the business outcome, not only that a page loaded.
- Slow suites that people stop running. Fix: keep smoke small, run in parallel, move checks down to the API.
- Shared environments that are down or changing. Fix: tell an environment failure from a product failure before raising a defect.
- Automation drifting behind the product. Fix: update tests in the same change as the feature.
