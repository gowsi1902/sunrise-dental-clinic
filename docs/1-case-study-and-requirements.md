# 1. Case study and requirements

Full Moodle brief (scenario, six functions, Tasks A–D, marking, submission): **[0-assessment-brief.md](0-assessment-brief.md)**.  
Row-by-row mapping to code: **[0-alignment-checklist.md](0-alignment-checklist.md)**.

**Module:** CIS6003 Advanced Programming — WRIT1  
**System:** Sunrise Dental Clinic appointment and patient management  
**Student:** `[Your Name]` · `[Your Student ID]`

## Scenario (from the brief)

Sunrise Dental Clinic is a busy private centre in Colombo. Appointments and treatment records were kept on paper, which caused **double bookings**, lost records, waiting, and billing errors. Management asked for a computerised system.

Each visit gets a **unique appointment number**. New patients are registered with: appointment number, patient name, address, contact number, dentist name, treatment type, appointment date and time.

## Required functions

| # | Brief item | How it is implemented |
|---|---|---|
| 1 | User authentication | Session login, BCrypt, staff/admin roles |
| 2 | Register appointment | Form + `POST /api/appointments`; number `SDC-0001` |
| 3 | Display details | Search by number on the home page |
| 4 | Calculate and print bill | Treatment fee + consultation fee; print receipt |
| 5 | Help | In-app step-by-step staff guide |
| 6 | Exit | Exit system signs the user out |

## Assumptions (must be justified in the Word report)

- Storage is **MySQL**, not text files, because Task B requires a proper database and web services.
- The UI is a **browser app** (React) talking JSON to Jakarta servlets so the system is distributed.
- Double-booking is blocked when the **same dentist** already has a **SCHEDULED** visit on the same date and time slot.
- Bill total = **treatment fee + consultation fee** (no hotel-style night rate).
- Admin may manage extra staff accounts; reception staff cannot.

## Learning outcomes

| LO | Task |
|---|---|
| LO I | UML (use case, class, sequence) |
| LO II | Interactive system, validation, reports, patterns, DB, TDD |
| LO III | Public GitHub, versions, documented workflow |
