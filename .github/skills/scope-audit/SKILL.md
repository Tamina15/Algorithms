---
name: "scope-audit"
description: "Audit a diff for self-added scope — extra features, invented behavior, or unrelated changes not covered by the AC or explicit instructions."
---

# Scope Audit

Apply this before submitting/finalizing any AC-driven or ticket-driven change, or when asked to check for scope creep or extra additions.

## Before auditing

1. Review the actual diff, hunk by hunk — not a summary of it.
2. Have the AC/ticket/explicit instructions that motivated the change on hand.
3. Where possible, check existing codebase patterns (naming, error handling, response formats) to tell "followed established convention" apart from "invented something new."

If the AC or instructions aren't available, ask for them first — scope can't be judged without knowing what was in scope.

## Process

For each changed hunk, ask: **"Does this trace back to a specific AC item or explicit instruction?"**

Flag anything that doesn't, including:
- New functions, parameters, config options, flags, or defaults not mentioned in the AC.
- Extra validation, error handling, or messages beyond what was specified — especially exact wording, status codes, or formats guessed rather than confirmed or pattern-matched from the codebase.
- Refactoring, renaming, reformatting, or restructuring outside what's strictly needed for the change.
- Unrelated bug fixes bundled into this change, however well-intentioned.
- New dependencies, libraries, or abstractions added "for future flexibility" that weren't requested.
- Behavior assumed as "obviously implied" rather than stated or matched to an existing pattern.

Resolve each flagged item as one of:
- **Remove** — not needed to satisfy the AC.
- **Justify** — genuinely required to make the AC work (e.g. a shared utility had to change too); state why.
- **Split out** — a good idea but out of scope; note as a separate follow-up instead of including it here.

## Output format

```
## Scope Audit
- Flagged: <what was found, with file/line ref> → Removed / Justified: <reason> / Split out as follow-up
(or: "None found — every changed hunk traces to an AC item or explicit instruction.")

## Follow-up Suggestions (out of scope, not included)
- <idea> — <why it was left out>
(or "None")
```

## Rules

- Review the actual line-by-line diff, not just the PR description.
- "This is obviously better" is not a reason to include an unrequested change without flagging it first.
- Never silently keep a flagged item without stating a justification.
- A diff that doesn't map cleanly to the AC is itself worth surfacing, even if every individual hunk seems reasonable.
