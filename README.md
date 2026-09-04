# Sunrise Dental Clinic — Appointment System

A **distributed Java web application** for **Sunrise Dental Clinic, Colombo**. Reception staff register patients, prevent double-booked dentists, search visits by appointment number, and print bills (treatment fee + consultation fee).

GitHub: [https://github.com/gowsi1902/sunrise-dental-clinic](https://github.com/gowsi1902/sunrise-dental-clinic)

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

## Features

1. Login (authorised staff only)  
2. Register appointment (number auto-issued, patient, dentist, treatment, date/time)  
3. Display details (search by appointment number)  
4. Calculate and print bill (treatment + consultation)  
5. Help for new staff  
6. Exit system (sign out)

Also included: overlap check, admin stats, and audit log.
