# Utilities Constitution

## Purpose

This repository exists to hold practical utilities that make life easier on the web. Changes should preserve that bias toward simple, useful tools and should avoid turning the repo into a dumping ground for unrelated experiments.

## Authority Order

When instructions conflict, follow this order:

1. direct human instruction for the current task
2. this constitution
3. [`start.ai`](/home/dfish/projects/fishdan/Utilities/start.ai)
4. repo-local instructions in the relevant subtree

## Branch And Merge Policy

- Do substantive work on a dedicated branch, not directly on `main`.
- Use small, scoped branches that correspond to one feature, fix, or hotfix.
- Even urgent fixes should go through a dedicated hotfix branch and merge back through the normal integration path unless a human explicitly directs otherwise.
- Keep commits scoped and readable so branch history explains the change.

## GitHub Identity And Authentication

- GitHub automation in this repo should authenticate as the configured GitHub App bot, not as a human account.
- Use the local `.repo_ai` workflow and scripts for GitHub auth, token generation, and bot-authored commits.
- Do not use personal access tokens, OAuth tokens, embedded credentials, or ad hoc auth workarounds when the `.repo_ai` path is available.
- If bot auth fails or repo permissions are unclear, stop and ask for human help instead of bypassing the model.

## Secrets And Sensitive Data

- Never commit private keys, tokens, passwords, or secret config values.
- Keep local GitHub App material under `.repo_ai/secrets/` as documented there.
- Do not copy secret values into `README.md`, `start.ai`, `progress.ai`, issues, PR text, or other tracked files.
- Generated temporary auth material belongs in temporary storage only, not in the repository.

## Scope Control

- Stay within the user’s requested scope.
- Do not broaden a task into unrelated cleanup, refactors, or dependency churn unless the user approves it.
- If a request would benefit from a spec or task breakdown, propose the scope first and then create only the minimum artifacts needed.
- When a constitution, spec, task file, or branch goal defines the work, treat that as the boundary.

## Security And Dependency Hygiene

- Before discretionary work, check whether a dependency audit has been logged recently in `progress.ai`.
- For Java work, use the OWASP dependency check flow already referenced by `start.ai`.
- Research new dependencies before adding them. Prefer stable, maintained packages over novelty or release candidates unless there is a clear reason otherwise.
- If a dependency choice is not obviously low-risk, get explicit human confirmation before adding it.

## Logging And Durable Context

- Record durable setup steps, decisions, checks, and next actions in `progress.ai`.
- Keep the log useful: include decisions and verifications, omit noise and dead-end exploration.
- When repo policy changes, update both the policy file and the log entry that explains why.

## Repo Structure

- Keep shared repo-wide policy at the root in files like `constitution.md` and `start.ai`.
- Keep GitHub App automation under `.repo_ai/` and treat vendored submodule contents as upstream-managed unless the work is specifically about that submodule pointer.
- Keep project-specific changes in the relevant subtree instead of adding loose top-level files without a clear repo-wide purpose.

## Default Agent Behavior

- Start at the repo root, notice the current branch, and check whether `.repo_ai` is initialized before attempting GitHub operations.
- Read `start.ai` early in a session and use it as the operational playbook.
- If this constitution and `start.ai` both apply, use this file for durable policy and `start.ai` for concrete session workflow.
