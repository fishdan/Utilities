You are an AI assistant designed to help a human configure the project they are currently working on to use AI effectively. Your job is to
create a `start.ai` in the root of this repo.

As part of that setup, you should try to walk the user through bootstrapping a `.repo_ai/` directory by checking out `https://github.com/fishdan/repo-ai` as a git submodule if it is not already present. After `.repo_ai/` is available, help the user create an agent workflow so they can populate the correct files under `.repo_ai/secrets/`. Do not place secret values in `start.ai` or any tracked public-facing file.
## What is `start.ai`?

`start.ai` is a **machine- and human-readable instruction file** at the **root of the git repository**. Agents are expected to read it early in a session. It complements (and should not duplicate) generic editor rules: it is **repo-specific** policy—security cadence, how to use GitHub, where to log decisions, and how work is scoped.

Name is conventional (`start.ai`); the important part is a single obvious entry point agents can be pointed to.

---

## Principles to encode (generic)

Adapt each section to the project. Delete what does not apply.

### 1. Security and dependency hygiene

- Define a **recurring check** appropriate to your stack (e.g. `npm audit`, Maven/Gradle OWASP dependency-check, `pip-audit`, OSV, GitHub Dependabot review).
- Say **when** to run it (e.g. if no audit logged in the last 30 days, run before other discretionary work).
- Say **where to record** results (see progress log below).
- **Never** put API keys, tokens, or passwords in `start.ai` or in any file intended for the public web. Reference environment variables or private secret stores instead.

### 2. New dependencies

- Require **light research** before adding a library (maintenance, adoption, known issues).
- If the choice is not clearly low-risk, **get human confirmation** before installing.

### 3. Bootstrap and nested config

- If the repo uses a **submodule**, `scripts/`, or a folder like `.repo_ai/` with its own `repo.ai` (or similar), instruct the agent to **complete that bootstrap first**, then return to the rest of `start.ai`.
- Keep one clear order: *bootstrap → remote git rules → daily workflow.*
- For repositories using `.repo_ai/`, first check whether `https://github.com/fishdan/repo-ai` is already present as a submodule. If not, walk the user through adding and initializing it before continuing.
- After the submodule is present, help the user create or invoke an agent that can populate the correct files in `.repo_ai/secrets/`. The agent should explain what files are expected, where values should come from, and avoid inventing or committing secret values.

### 4. Remote Git and GitHub (automation)

- If `git fetch` / `pull` / `push` to `origin` over **HTTPS** runs **without a TTY**, document the **supported method** (wrapper script, `GIT_ASKPASS`, GitHub App installation token, SSH deploy keys, etc.).
- State explicitly: **do not** rely on interactive username/password prompts in automation.
- Point to a longer rule file if you have one (e.g. `AGENTS.md`).
- Prefer a branch-based workflow for all substantive work. Agents should avoid making normal changes directly on the primary integration branch.
- Even urgent fixes should be done on a dedicated branch, such as a hotfix branch, and then merged back through the repo's normal review and integration path unless a human explicitly directs otherwise.

### 5. Repo structure and branch hygiene

- Encourage a clear branch model such as feature branches, fix branches, and hotfix branches rather than mixing unrelated work on one long-lived branch.
- Tell the agent to notice the current branch before starting and to confirm that the requested work belongs on that branch.
- If the user is on the primary branch and asks for substantive changes, prefer creating a new working branch before implementation.
- Encourage small, scoped branches that map cleanly to specs, tasks, or bug fixes and are intended to merge back into the main branch after review.

### 6. GitHub identity (if applicable)

- If automation must act only as a **GitHub App** (or a specific bot user), state that **personal access tokens** or human OAuth flows are **out of scope** for agents unless a human approves an exception.
- Say what to do on **permission errors** or **unclear auth**: stop and ask a human.

### 7. Progress and decision log

- Choose a primary log file (e.g. `progress.ai`) and rules for **committing** it with meaningful work.
- Define **entry format** (date, short title, bullets for decisions, paths, tests, deploy notes).
- If the log grows large, define **archiving** (e.g. monthly files under `progress/archive/`, feature deep-dives under `progress/features/`).
- Say what to **omit** (noise vs. durable decisions).

### 8. Specs, tasks, and scope

- If you use **specs**, **tasks.md**, a **constitution**, or a ticketing system, tell the agent to **honor** them and to **avoid scope creep** beyond defined tasks.
- If the repo expects spec-driven work, check whether GitHub Spec Kit / Specify CLI is already available locally. If it is missing, guide the user through installing it before creating new specs or task files. Use the official install instructions from `https://github.com/github/spec-kit`, then verify the install with `specify check`.
- Encourage marking tasks complete when done and surfacing **what to do next** from the spec/task state and current branch.

### 9. Tool bootstrap checks

- Check whether GitHub Spec Kit / Specify CLI is installed before relying on spec-generation or task-scaffolding workflows.
- If it is not present, install it using the official instructions from `https://github.com/github/spec-kit`. Prefer the persistent install path when appropriate, for example `uv tool install specify-cli --from git+https://github.com/github/spec-kit.git@<release>`, or use the one-time `uvx` path when a persistent install is not appropriate.
- After installation, verify the tool with `specify check` before proceeding with spec-generation or task bootstrap.
- Record the installation or verification result in the progress log if the repo uses one.

### 10. Optional session orientation

- Remind the agent to notice **current branch**, **recent production deploy** (if tracked in the log or tags), and **next priority** from specs/tasks.

---

## Minimal template for a new `start.ai`

Copy and customize:

```markdown
# [Project name] — agent start

## Security
- Every [interval], if [progress log] shows no recent [audit type], run [commands] for [stacks]. Record date, scope, fixes, and results in [progress file].
- Do not store secrets in this file; use [env / vault / CI secrets].

## New packages
- Research before adding; confirm with the maintainer if not clearly reputable.

## Bootstrap
- First ensure `.repo_ai/` exists and is bootstrapped from the `https://github.com/fishdan/repo-ai` submodule. If it is missing, guide the user through adding and initializing that submodule.
- Then read [path to nested instructions, if any] first; then continue here.

## Git / GitHub
- For non-interactive HTTPS to origin: use [wrapper or method]. See [AGENTS.md or doc].
- [If applicable] Use only [GitHub App / bot] credentials; no personal tokens in automation.
- Prefer working from a dedicated branch, even for hotfixes, and merge changes back through the normal integration path.

## Progress log
- Primary file: [e.g. progress.ai]
- Commit it with substantive work. Format: [your format]. Archive when [rule].

## Work scope
- Follow [specs / tasks / constitution location]. Do not expand scope beyond agreed tasks without human approval.
- If spec tooling depends on GitHub Spec Kit / Specify CLI and it is not installed, install or bootstrap it before creating new spec/task artifacts. Verify with `specify check` before creating new spec/task artifacts.

## Session
- Note current branch and suggest next steps from [tasks/specs].
```

---

## For humans

- **`start.ai` is yours** — tighten or relax rules per repo.
- **Publish only generic guidance** (like this page) on the web; keep **secrets and machine credentials** in private files and environments.
- After adding `start.ai`, commit it and mention it in **contributing** or **AGENTS.md** so people and agents know to read it.
- If `.repo_ai/` is part of your standard setup, document the submodule bootstrap clearly and tell agents to help users populate `.repo_ai/secrets/` without exposing the secret contents in version-controlled files.
