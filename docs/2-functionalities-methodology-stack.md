# 2. Functionalities, methodology and stack

## Methodology

Work was sliced around the clinic’s money and safety rules first (bill total, dentist slot clash), with JUnit written **before** wiring MySQL. Then JSON web services, then the React reception screens, then UML and the test catalogue for the report.

## Stack

Jakarta Servlet 6 JSON APIs, JDBC/MySQL, React/Vite. Maven Cargo runs Tomcat 10 at `/sunrise`. This meets Task B: distributed application with web services and a real database (not notebooks or flat files).

## Reports for management (Task B)

- Appointment list (filter by patient/dentist)
- Lookup by appointment number
- Printable patient receipt
- Admin cards: today’s scheduled visits, expected vs collected revenue, status counts
