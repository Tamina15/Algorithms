---
name: "write-unit-tests"
description: "Write unit tests for new or changed code, matching repo conventions and actually verifying they pass."
---

# Write Unit Tests

Apply this whenever you have just written or modified code, or when asked to add/improve test coverage.

## Before writing tests

1. Look at the actual diff/changed files — don't work from a description of them.
2. If Acceptance Criteria (AC) or a ticket description is available, use it: tests should map to specified behavior, not just to whatever the code happens to do.
3. Open an existing test file in the repo first and match its framework, file location, naming pattern, and mocking/fixture/assertion style. Do not introduce a new test library unless explicitly instructed.

## What to cover

For each changed function/module, write tests for:
- **Happy path** — expected behavior under normal input.
- **Edge cases** — empty/null input, boundary values, invalid input, error/exception paths.
- **Regression case** (bug fixes only) — a test that would fail against the old code and passes against the fix.

## Rules

- Match existing repo conventions exactly. Don't invent a new testing style.
- Run the tests. Never state that tests pass without executing them.
- If a test fails, fix the implementation or the test — never comment it out, skip it, or loosen the assertion just to force a pass.
- If something genuinely can't be unit tested (pure UI wiring, requires a live external service), say so explicitly and explain why instead of silently skipping it.
- Avoid tests that are trivially always-true, and avoid testing implementation details instead of observable behavior.

## Output

When done, summarize:
- Which test file(s) were added/updated and what each covers.
- Which AC item (if any) each test maps to.
- Confirmation the tests were actually run, with pass/fail results.
- Anything left untested, and why.
