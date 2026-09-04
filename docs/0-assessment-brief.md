# CIS6003 Assessment brief (source of truth)

**Do not treat any external GitHub hotel/vehicle demo as the case study.**  
This file is the WRIT1 specification copied from the School of Technologies brief. The software and the other `docs/` files must follow **this** scenario.

| Field | Value |
|---|---|
| Module | CIS6003 Advanced Programming |
| Assessment | WRIT1 — 100% |
| Academic year / semester | 2024 / 1 |
| Module leader | priyanga@icbtcampus.edu.lk |
| Pass mark | 40% undergraduate / 50% postgraduate |
| Word count (report) | 4000 (appendices and reference list excluded) |
| Submit | PDF via Moodle/Turnitin by 2.00pm on the deadline day |
| File name example | `st12345678 CIS6003 WRIT1.pdf` |
| Referencing | Harvard |

The cover title on the PDF says “Online vehicle reservation System”. **The scenario text is Sunrise Dental Clinic.** This project implements the dental scenario.

---

## Scenario

Sunrise Dental Clinic is a busy private dental centre in Colombo that treats many patients every week. Patient appointments and treatment records are handled manually using paper files and notebooks. This has caused double bookings, lost patient records, long waiting times, and billing errors.

The clinic will use a computerised appointment and patient management system. Each patient visit is assigned a **unique appointment number**. Registration collects:

- appointment number  
- patient name  
- address  
- contact number  
- dentist name  
- treatment type  
- appointment date  
- appointment time  

---

## Required functions

1. **User authentication (login)** — username and password; only authorised staff.  
2. **Register new appointment** — store all fields listed above.  
3. **Display appointment details** — search by appointment number; show complete information.  
4. **Calculate and print bill** — total from **treatment type** and **consultation fee**; print receipt.  
5. **Help section** — step-by-step instructions for new staff.  
6. **Exit system** — close / leave the application safely.  

Additional functions are allowed. Assumptions about design and access permissions are allowed if they are **explained with reasons**.

The brief also mentions Java, user-friendly messages, menu-driven use, and that text files *may* be used. **Task B overrides storage:** the marked solution must be a **distributed application with web services** and a **proper database**. This project uses Jakarta JSON APIs + MySQL (not notebooks or a hotel reservation clone).

---

## Report format (for the Word/PDF you submit)

- Paper A4; margins 1.5" left, 1" right/top/bottom  
- Page numbers bottom right; line spacing 1.5  
- Headings 14pt Bold; body 12pt; Times New Roman  
- Harvard for external sources  

---

## Tasks and marks

| Task | LO | Marks | What to deliver |
|---|---|---|---|
| **A** | I | 20 | Use case, class, sequence UML + design decisions and assumptions |
| **B** | II | 40 | Interactive UI, validation, useful reports; web services; design patterns; database |
| **C** | II | 20 | Test rationale, TDD, test plan, test data, automation, apply the plan |
| **D** | III | 20 | Public GitHub, several versions over time, documented workflow/CI |

### Learning outcomes

- Fluency in contemporary languages, tools and environments.  
- Evaluate theory of industry-standard programming and design in the SDLC.  
- Awareness of professional/ethical software development (carpentry and codemanship).  

### Cardiff Met EDGE (mention in the report)

- **Ethical** — protect user data; privacy; secure coding.  
- **Digital** — decompose the problem; clear interfaces; how a server runs the services.  
- **Global** — data-handling standards; adaptable design.  
- **Entrepreneurial** — computerising the clinic process.

---

## Marking snapshot (aim at Good / Excellent)

**Task A (UML):** correct actors and the six use cases; `<<include>>` / `<<extend>>`; ~3 sequence diagrams; class diagram with private/public members, relationships, multiplicity, navigability, aggregation/composition; justified assumptions; critical evaluation.

**Task B (build):** 3-tier architecture; patterns named **and** shown in code; MySQL (queries plus procedures/functions/triggers where useful); session cookies; separate screens for data entry vs reports; validation; management reports.

**Task C (test):** TDD story, test data, JUnit test classes, automation (`mvn test`), screenshots in the PDF, traceability to the six functions, lessons learned.

**Task D (Git):** public repo URL in the report; many commits (not one upload); branches/tags; optional CI workflow; latest version documented.

---

## Unfair practice (do not)

No copying unacknowledged code or text, no collusion presented as solo work, no fabricated test results. Do not submit a hotel or vehicle system and claim it is this brief.
