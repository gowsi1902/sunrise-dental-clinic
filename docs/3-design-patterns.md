# 3. Design patterns (Task B)

## Singleton — `DbConnection`

One instance loads `db.properties` and opens JDBC connections. Credentials are not hardcoded in Java.

## DAO

`AppointmentDao`, `DentistDao`, `TreatmentDao`, `PaymentDao`, `UserDao`, `AuditLogDao` contain SQL only.

## Service / business layer

`AppointmentService` and `BillingService` enforce validation, slot clash, and fee totals. Servlets stay thin (MVC controllers).

## MVC

| Part | Implementation |
|---|---|
| Model | `com.sunrise.model` |
| View | React pages |
| Controller | `com.sunrise.web.*Servlet` |

## Filter

`CorsFilter` then `AuthFilter` (order in `web.xml`). Public: login/logout. Admin prefixes: `/api/users`, `/api/admin`, `/api/audit`.

## DTO + Factory

`ApiResponse` / `BillDto` shape JSON. `JsonFactory.gson()` is a single Gson with Java-time adapters.

## Database rules (not a GoF pattern, but marked with Task B)

Fee totals and double-booking are also enforced in MySQL (`fn_bill_total`, insert/update triggers). That is a second line of defence if someone inserts rows outside the servlet layer.

## Critical evaluation (for the 4000-word report)

Singleton keeps configuration in one place but is harder to mock; DAOs are testable if JDBC is abstracted later. Filters keep security out of every servlet. MVC matches a distributed UI + API. Patterns add classes, which is acceptable for coursework clarity and marking Task B. Defence in depth (service + trigger) reduces billing and clash errors that the scenario describes, at the cost of keeping the Java calculator and the SQL function in sync — documented as an assumption.
