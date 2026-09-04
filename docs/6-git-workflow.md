# 6. Git / GitHub workflow (Task D — 20 marks)

Replace the placeholder URL with your **public** repository after you push.

**Repository:** `https://github.com/gowsi1902/sunrise-dental-clinic`

## What markers look for

- Public repo they can open without login
- **Several commits over time**, not one dump
- Documented branching / PR workflow (even a simple `main` + feature branches)
- Link to the repo **inside the PDF report**

## Suggested daily versions

| Day | Branch / commit theme |
|---|---|
| 1 | `main`: README, `.gitignore`, schema |
| 2 | Backend models, DAOs, `ClinicCalculator` + JUnit |
| 3 | JSON servlets (login, appointments, billing) |
| 4 | React screens + help + exit |
| 5 | Docs UML / test plan; tag `v1.0.0` |

## Commands (do not skip hooks)

```bash
git add README.md backend frontend docs .gitignore
git commit -m "Add clinic appointment core and report diagrams."
git branch feature/billing-print
git checkout feature/billing-print
# ... work ...
git checkout main
git merge feature/billing-print
git tag v1.0.0
git push -u origin main --tags
```

## Workflow diagram

```
feature/register-appointment
        ↓
feature/billing
        ↓
main (protected by reviews if you use a PR)
        ↓
v1.0.0
```

## CI / CD (excellent band)

The brief marks CI as **optional** for the excellent band. A public live host is **not** required. This repo uses GitHub Actions as the pipeline.

**Workflow file:** `.github/workflows/backend-tests.yml` (workflow name: `CI`)

| Stage | What runs | Why it is in the assignment |
|---|---|---|
| **CI — backend** | `mvn -B test` (JUnit 5, Java 17 Temurin) | Task C automation on every push and pull request |
| **CD — backend** | `mvn -B -DskipTests package` → upload `sunrise.war` | Repeatable build of the distributed web app |
| **CD — frontend** | `npm ci` + `npm run build` → upload `frontend/dist` | Repeatable production UI bundle |

Triggers: push to `main`/`master`, pull requests, and **Run workflow** (manual) on the Actions tab.

```
commit / pull request
        ↓
GitHub Actions (ubuntu-latest)
        ├── backend: JUnit → sunrise.war artefact
        └── frontend: npm build → dist artefact
        ↓
green tick on the commit (screenshot this for the PDF)
```

### Screenshot for the PDF

1. Push this workflow to the **public** GitHub repo.
2. Open **Actions** → latest **CI** run → confirm both jobs are green.
3. Paste that screenshot plus a local `mvn test` Surefire summary into the report.

Deploy of the WAR to a public cloud host is **not** required by the brief. Artifacts on Actions are enough to show continuous delivery of a build.

## `.gitignore`

Assignment PDF, `docs/ai/`, `.cursor/`, `node_modules`, `target`, local `db.properties` stay out of the public repo.
