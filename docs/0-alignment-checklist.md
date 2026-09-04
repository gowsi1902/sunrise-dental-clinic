# Alignment checklist (brief vs this codebase)

Every row is traced to **[docs/0-assessment-brief.md](0-assessment-brief.md)**, not to an unrelated GitHub demo.

## Six mandatory functions

| # | Requirement | Where it lives |
|---|---|---|
| 1 | Login; authorised staff only | `Login.jsx`, `LoginServlet`, `AuthFilter`, BCrypt in `PasswordUtil`, HTTP session cookie |
| 2 | Register appointment (number, patient, address, phone, dentist, treatment, date, time) | `AddAppointment.jsx`, `AppointmentService.create`, unique `appointment_no` (`SDC-0001`) |
| 3 | Search by appointment number; full details | Dashboard search → `GET /api/appointments?number=`, `AppointmentDetails.jsx` |
| 4 | Bill = treatment fee + consultation fee; print | `BillingService` + `ClinicCalculator.total`; Print bill on `Billing.jsx` |
| 5 | Help for new staff | `Help.jsx` numbered steps matching the six functions |
| 6 | Exit system | Layout **Exit system** → logout + session invalidate (`LogoutServlet`) |

## Scenario problems → extra design (allowed)

| Problem in the brief | Design choice | Why |
|---|---|---|
| Double bookings | Same dentist + date + time cannot both be `SCHEDULED` (service + MySQL trigger) | Paper diaries caused clashes; DB enforces the rule even if the API is bypassed |
| Lost records | MySQL, not a text file | Task B requires a proper database |
| Billing errors | Total computed from treatment row; JUnit on `ClinicCalculator` | Fees are not typed in by hand |
| Waiting / paper | Web UI + JSON services | Distributed: browser and Tomcat can run on different machines |

## Task B technical rules

| Rule | Implementation |
|---|---|
| Distributed + web services | React on :5173, JSON servlets on `:8080/sunrise` |
| Design patterns | Singleton, DAO, MVC, Filter, DTO, Factory — [3-design-patterns.md](3-design-patterns.md) |
| Database | `sunrise_dental` + function/trigger in `backend/database/schema.sql` |
| Validation | Formik/Yup + server checks (required fields, phone pattern, no past dates, known dentist/treatment) |
| Reports | Appointment list, receipt, admin counts/revenue |
| Sessions | `HttpSession` + HttpOnly cookie path `/sunrise` |

## Task A / C / D artefacts

| Task | Artefact |
|---|---|
| A | [4.3-uml-diagrams.md](4.3-uml-diagrams.md) |
| C | [5-test-cases.md](5-test-cases.md), `ClinicCalculatorTest` |
| D | [6-git-workflow.md](6-git-workflow.md), `.github/workflows/backend-tests.yml` (JUnit + WAR + frontend build) |

## Deliberate non-goals (stated assumptions)

- Patients do not self-register online (staff-only, as “authorised staff”).  
- Cover title “vehicle reservation” is ignored.  
- Email/SMS not implemented; receipts are print-from-browser (enough for the six functions; can be described as a future extension in the report).
