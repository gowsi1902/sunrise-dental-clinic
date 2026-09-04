# 5. Testing and TDD (Task C — 20 marks)

## Rationale

The brief’s failures were **double bookings** and **billing errors**. Those two rules were isolated in `ClinicCalculator` with **no database**, so they can fail fast in JUnit (test-driven). Servlet and UI tests are then manual against a running MySQL instance.

TDD cycle used:

1. Write `ClinicCalculatorTest` (red if the method is missing).
2. Implement `total()` and `slotClash()`.
3. Call the same methods from `AppointmentService` / `BillingService` (green on real bookings).

Automation: `mvn -f backend test` (Surefire + JUnit 5) locally, and `.github/workflows/backend-tests.yml` on each push. Screenshot the Surefire summary and a green Actions run for the PDF.

## Automated tests

| ID | Assertion |
|---|---|
| UT1 | Filling 12,000 + consult 2,500 = 14,500.00 |
| UT2 | Consultation-only (0 + 2,500) = 2,500.00 |
| UT3 | Negative fee → status 400 |
| UT4 | Same dentist/date/time clashes; different dentist or slot does not |

## Manual plan (trace to brief)

| ID | Function | Data | Expected |
|---|---|---|---|
| TC01 | Login | `admin` / `password` | Dashboard |
| TC02 | Login fail | wrong password | Error, stay on login |
| TC03 | Register | valid patient + slot | `SDC-000n`, SCHEDULED |
| TC04 | Validation | empty name | Client + API reject |
| TC05 | Double book | same dentist, same slot | 409 |
| TC06 | Display | search `SDC-0001` | Full patient/visit fields |
| TC07 | Bill | filling treatment | treatment + consultation lines; print |
| TC08 | Help | open Help | Six brief steps |
| TC09 | Exit | Exit system | Session ended, login screen |
| TC10 | Staff vs admin | staff opens `/admin` | Redirect; API 403 |

## Lessons

Unit tests catch fee arithmetic without Tomcat. Slot tests do not replace a live clash test against MySQL (TC05). Next improvement: servlet-level tests with an in-memory database.
