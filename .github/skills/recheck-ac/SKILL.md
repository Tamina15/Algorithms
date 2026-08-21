---
name: "recheck-ac"
description: "Verify a completed change against its Acceptance Criteria item by item, with a literal checklist rather than a general impression."
---

# Recheck Acceptance Criteria (AC)

Apply this before declaring any AC-driven or ticket-driven task complete, or when asked to verify requirements are met.

## Before checking

1. Re-read the AC/ticket text fresh — from the source, not from memory of it while you were implementing.
2. Look at the actual diff/changed files, not what you intended to build.
3. If the AC isn't available, ask for it. Don't reconstruct it from the code or guess at what it probably said.

## Process

1. Break the AC into discrete, checkable items (split compound statements into individual testable claims).
2. For each item, check the real code/diff and mark one of:
   - ✅ **Implemented and verifiable** — cite the specific code location (and test, if one exists).
   - ⚠️ **Implemented but not verifiable/tested** — explain what's missing to confirm it fully.
   - ❌ **Not implemented / partially implemented** — describe the gap precisely.
3. Surface any ambiguity: if an AC item was unclear and you chose an interpretation while implementing, state that interpretation explicitly so it can be confirmed or corrected.
4. Don't round up. A ⚠️ is not a ✅. If in doubt, mark ⚠️ and explain the doubt rather than defaulting to ✅.

## Output format

```
## AC Checklist
- [✅/⚠️/❌] <AC item> — <code ref / test ref / gap description>

## Assumptions / Ambiguities Surfaced
- <AC item> was ambiguous; implemented as <interpretation> — please confirm
(or "None")

## Verdict
Complete — all items ✅
OR
Not complete — N items need attention: <list>
```

## Rules

- Never mark something ✅ based on "it should work" without checking the actual code.
- Don't skip AC items that seem minor or obvious.
- Don't soften a ❌ into a ⚠️ to make the report look better.
- Don't declare "Complete" when any item is ❌ or an ambiguity is unconfirmed.
