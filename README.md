# Sunrise Dental Clinic — Appointment System

CIS6003 Advanced Programming WRIT1: a **distributed Java web application** for **Sunrise Dental Clinic, Colombo**. Reception staff register patients, prevent double-booked dentists, search visits by appointment number, and print bills (treatment fee + consultation fee).

Student name: `[Your Name]`  
Student ID: `[Your Student ID]`  
GitHub: `[public repository URL]`

The PDF cover title says “Online vehicle reservation System”; **ignore that.** The scenario, functions, and Tasks A–D are in [docs/0-assessment-brief.md](docs/0-assessment-brief.md). Traceability to this codebase: [docs/0-alignment-checklist.md](docs/0-alignment-checklist.md).

## Tech stack

| Layer | Choice |
|---|---|
| Presentation | React 18, Vite, Tailwind CSS, Formik/Yup, Axios |
| Application | Jakarta Servlet 6 JSON web services, Maven WAR, Tomcat 10 |
| Persistence | JDBC + MySQL 8 |
| Security | BCrypt, HTTP session, role filter |

## Prerequisites

Java 17+, Maven 3.8+, Node.js 18+, MySQL 8.

## 1. Database

```bash
mysql -u root -p < backend/database/schema.sql
```

Copy [backend/src/main/resources/db.properties.example](backend/src/main/resources/db.properties.example) to `db.properties` if needed. Default example login: `root` / `12345678`.

Accounts created on first backend start: `admin` / `password`, `staff` / `staff123`.

## 2. Backend (web services)

```bash
cd backend
mvn test
mvn clean package cargo:run
```

API: [http://localhost:8080/sunrise](http://localhost:8080/sunrise)

## 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

UI: [http://localhost:5173](http://localhost:5173) — Vite proxies `/sunrise` to Tomcat.

## Brief features

1. Login (authorised staff only)  
2. Register appointment (number auto-issued, patient, dentist, treatment, date/time)  
3. Display details (search by appointment number)  
4. Calculate and print bill (treatment + consultation)  
5. Help for new staff  
6. Exit system (sign out)

Extra (allowed by the brief): overlap check, admin stats, audit log, Git-friendly layered design.

## Report notes

Numbered files in [`docs/`](docs/) map to Tasks A–D. Copy into a 4000-word PDF (Times New Roman, Harvard). Keep the assignment brief PDF out of git.

| Task | Marks | Source |
|---|---|---|
| Brief (paste into report intro) | — | [docs/0-assessment-brief.md](docs/0-assessment-brief.md) |
| Alignment | — | [docs/0-alignment-checklist.md](docs/0-alignment-checklist.md) |
| A UML | 20 | [docs/4.3-uml-diagrams.md](docs/4.3-uml-diagrams.md) |
| B App + patterns + DB | 40 | code + [docs/3-design-patterns.md](docs/3-design-patterns.md) |
| C Testing / TDD | 20 | [docs/5-test-cases.md](docs/5-test-cases.md), `ClinicCalculatorTest` |
| D GitHub versions | 20 | [docs/6-git-workflow.md](docs/6-git-workflow.md), `.github/workflows/backend-tests.yml` |
