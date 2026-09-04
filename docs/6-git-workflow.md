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

`.github/workflows/backend-tests.yml` runs `mvn -B test` in `backend/` on push and pull request (Java 17, Temurin). After the repo is public, paste a screenshot of a green run into the PDF. Deploy of the WAR to a public host is optional; markers mainly want the workflow file plus several dated commits.

## `.gitignore`

Assignment PDF, `docs/ai/`, `.cursor/`, `node_modules`, `target`, local `db.properties` stay out of the public repo.
