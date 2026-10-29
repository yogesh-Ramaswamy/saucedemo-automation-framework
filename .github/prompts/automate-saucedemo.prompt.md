Read `.github/agents/saucedemo-automation-agent.agent.md`, `.github/instructions/saucedemo-automation.instructions.md`, and `.github/reference/saucedemo-legacy-reference-profile.md`.

The legacy `src` ZIP exists only in the ChatGPT conversation and is NOT on this machine. Do not ask for it or require it. All relevant coding-style facts extracted from it are embedded in the reference profile. Implement in the current SauceDemo project only.

Inspect the current project and workbook if available, create review/reuse matrix/plan before coding, then implement Java + Selenium + Cucumber with classic POM in the legacy style:
- page classes in `pages.pom`, static page methods accepting `WebDriver sharedDriver`;
- descriptive method names like `field_...`, `button_...`;
- separate Cucumber steps and hooks packages;
- tag-based setup, no automatic login for negative login/logout scenarios;
- failure screenshot attached to `Scenario` before driver quit;
- features in `src/test/resources/features/`;
- compatible JUnit Platform Cucumber runner/configuration;
- no PageFactory under any circumstance.

Use the current project's dependencies/Java version. Add only necessary Maven dependencies. Preserve TC01–TC26 traceability. Create all reports defined by the agent. Be explicit about the embedded profile versus current target findings, and do not claim the original ZIP was searched from this machine.
