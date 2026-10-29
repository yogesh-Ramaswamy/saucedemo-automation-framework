# SauceDemo Automation Testing Framework

AI-assisted UI test automation framework for the SauceDemo e-commerce application, built with Java 21, Selenium WebDriver, Cucumber BDD, PicoContainer, JUnit Platform, and Maven.

This repository demonstrates an AI-assisted approach to building, executing, reviewing, and documenting a structured automation testing framework using IntelliJ IDEA.

## Table of Contents

- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Framework Architecture](#framework-architecture)
- [Automated Test Coverage](#automated-test-coverage)
- [Project Structure](#project-structure)
- [AI Agent Pack v4](#ai-agent-pack-v4)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Running Tests](#running-tests)
- [Reports and Test Evidence](#reports-and-test-evidence)
- [Known Test Failures](#known-test-failures)
- [Development Approach](#development-approach)
- [Future Improvements](#future-improvements)

## Project Overview

This project automates key user journeys in the SauceDemo e-commerce application.

The framework is designed to validate login behaviour, product interactions, cart operations, checkout workflows, access control, browser compatibility, and responsive layouts.

An AI coding agent was used within IntelliJ IDEA to assist with framework implementation, test development, code organization, execution analysis, and reporting.

The repository also includes reusable AI agent definitions, instructions, prompts, and reference documentation to support a repeatable AI-assisted automation workflow.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Selenium WebDriver | Browser automation |
| Cucumber BDD | Behaviour-driven test scenarios |
| JUnit Platform | Test execution |
| PicoContainer | Dependency injection and scenario context |
| Maven | Dependency and build management |
| IntelliJ IDEA | Development environment |
| Git and GitHub | Version control and project hosting |
| AI coding agent | Assisted implementation and review |

## Framework Architecture

The framework follows a structured approach that separates browser interactions, test steps, feature definitions, and supporting utilities.

- **Page Objects:** Encapsulate page-level interactions and element operations.
- **Step Definitions:** Implement the actions associated with Cucumber steps.
- **Hooks:** Manage scenario setup, teardown, and related test lifecycle behaviour.
- **Feature Files:** Describe test scenarios in Gherkin syntax.
- **Configuration:** Keeps test settings in `config.properties`, with supported command-line property overrides.
- **Support Utilities:** Provide reusable actions, product identifiers, price parsing, and explicit wait functionality.
- **Browser Driver:** Centralizes browser setup and driver-related behaviour.
- **Cucumber Runner:** Provides the JUnit Platform entry point for executing scenarios.
- **AI Agent Resources:** Store agent definitions, prompts, instructions, and reference profiles under `.github/`.

### Framework Design Principles

- Classic Page Object Model; PageFactory is not used.
- Reusable page and test-support components.
- Separation of feature scenarios and step definitions.
- Explicit waits for browser interactions.
- Screenshot evidence attached to Cucumber scenarios where supported.
- Honest documentation of actual test outcomes and unresolved failures.

## Automated Test Coverage

| Test area | Coverage |
|---|---|
| Login | Authentication and login scenarios |
| Products | Product listing and sorting |
| Shopping cart | Cart operations and validation |
| Checkout | Checkout form validation and order flow |
| Access control | Restricted-access scenarios |
| Browser compatibility | Cross-browser behaviour |
| Responsive UI | Desktop, tablet, and mobile viewport checks |

Refer to the Cucumber feature files under `src/test/resources/features/` for the implemented scenarios and their detailed steps.

## Project Structure

```text
saucedemo-automation-framework/
├── .github/
│   ├── agents/
│   ├── instructions/
│   ├── prompts/
│   └── reference/
├── .mvn/
├── reports/
│   └── saucedemo/
├── src/
│   ├── main/
│   └── test/
│       ├── java/
│       │   └── saucedemo/
│       │       ├── config/
│       │       ├── driver/
│       │       ├── pages/
│       │       │   └── pom/
│       │       ├── steps/
│       │       │   └── hooks/
│       │       ├── support/
│       │       └── RunCucumberTest.java
│       └── resources/
│           ├── config.properties
│           └── features/
├── .gitignore
├── pom.xml
└── README.md
```

Generated test reports are written to `target/cucumber-reports/` when the corresponding test execution completes.

## AI Agent Pack v4

This repository contains an AI Agent Pack v4 workflow intended to guide an AI coding agent through the automation development process.

### Agent resources

- `.github/agents/` — Agent definitions.
- `.github/instructions/` — Reusable coding and project instructions.
- `.github/prompts/` — Task-specific prompts, including `automate-saucedemo.prompt.md`.
- `.github/reference/` — Reference documentation, including `saucedemo-legacy-reference-profile.md`.

### Embedded legacy conventions

The reference profile documents the legacy conventions used to guide framework adaptation:

- `pages.pom` package organization.
- Static page methods accepting `WebDriver sharedDriver`.
- Descriptive element method names such as `field_UserName` and `button_PreLogin`.
- Separate Cucumber step and `steps.hooks` packages.
- Tag-scoped `@Before` setup and `@After` behaviour.
- Shared-driver abstraction and browser system-property patterns.
- `Scenario.attach` screenshot evidence and Cucumber JUnit Platform runner/configuration.
- JUnit assertions and Selenium explicit waits.

### Adaptation constraints

- Use the classic Page Object Model; do not use PageFactory.
- Do not copy legacy application-specific methods or environment-specific browser configuration blindly.
- Capture and attach failure screenshots before browser teardown.
- Do not automatically log in during negative login or logout tests.
- Document implementation changes, test results, and limitations accurately.

The legacy reference profile is embedded in the repository, so the old `src.zip` does not need to be copied separately to the machine.

## Prerequisites

Install the following tools before executing the tests:

- Java Development Kit (JDK) 21.
- Maven.
- Git.
- IntelliJ IDEA or another Java IDE.
- A supported browser and internet access to reach the SauceDemo test application.

Verify your Java and Maven installations:

```powershell
java -version
mvn -version
```

Ensure `JAVA_HOME` points to your installed Java 21 JDK.

## Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/yogesh-Ramaswamy/saucedemo-automation-framework.git
cd saucedemo-automation-framework
```

### 2. Open the project

Open the project root containing `pom.xml` in IntelliJ IDEA and allow Maven to resolve the dependencies.

### 3. Check configuration

Review:

`src/test/resources/config.properties`

Configuration values can be overridden with supported `-D` system properties.

### 4. Review the agent instructions

Explore `.github/agents/`, `.github/instructions/`, `.github/prompts/`, and `.github/reference/` to understand the AI-assisted development workflow.

The automation prompt can be run from the current project when further agent-assisted work is required.

## Running Tests

Run the commands below from the project root in PowerShell.

### Run the full test suite

```powershell
mvn test -Dbrowser.headless=true
```

### Run tests excluding TC16 and TC20

```powershell
mvn test '-Dcucumber.filter.tags=not @TC16 and not @TC20' -Dbrowser.headless=true
```

### Run TC26 in Firefox

```powershell
mvn test '-Dcucumber.filter.tags=@TC26' -Dbrowser.selection=firefox -Dbrowser.headless=true
```

These commands reflect the project's documented execution configuration. Confirm the current configuration and latest execution results before relying on any test outcome.

## Reports and Test Evidence

The project maintains two main report locations.

### Review and execution reports

`reports/saucedemo/`

Contains the project's preserved review and execution documentation.

### Generated Cucumber reports

`target/cucumber-reports/`

Cucumber HTML and JSON reports are generated here when the corresponding test execution completes.

Open the generated HTML report in a browser to review the scenario results and available execution details.

The `target/` directory contains generated build output and is generally excluded from source control. The preserved documentation under `reports/saucedemo/` is the appropriate location for selected report evidence that should remain in the public repository.

## Known Test Failures

The existing assessment documents two unresolved test failures.

| Test case | Documented observation |
|---|---|
| TC16 | Whitespace-only checkout values were accepted. |
| TC20 | An empty cart reached order confirmation. |

The assessment also documents successful responsive viewport checks and cross-browser checks for TC23 and TC26.

These observations belong to the recorded execution described in the preserved report. They are not a guarantee of the latest test results.

See:

`reports/saucedemo/saucedemo-execution-report.md`

Review the detailed evidence before making changes to the corresponding tests or reporting either case as resolved.

## AI-Assisted Development Approach

An AI coding agent was used within IntelliJ IDEA to assist with:

- Organizing the automation framework.
- Implementing Java automation components and Cucumber scenarios.
- Following the repository's framework conventions.
- Reviewing test execution outcomes.
- Investigating failed scenarios and maintaining supporting reports.
- Maintaining reusable agent instructions and prompts.

The project is presented as an AI-assisted development effort. Generated code, assertions, and test results must be reviewed and validated; AI assistance does not replace understanding the framework or verifying its behaviour.

## Future Improvements

- Resolve and revalidate the documented TC16 and TC20 failures.
- Add a GitHub Actions workflow for automated test execution.
- Improve test failure diagnostics and reporting.
- Publish a verified sample HTML report or screenshots.
- Expand negative and boundary-value scenarios.
- Document browser setup and configuration options in greater detail.

## Disclaimer

This is a learning and portfolio project for software test automation practice. Test results may vary depending on application behaviour, browser versions, configuration, and execution environment.