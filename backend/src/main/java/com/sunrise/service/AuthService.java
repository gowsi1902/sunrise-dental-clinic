package com.sunrise.service;

import java.util.List;
import java.util.Optional;

import com.sunrise.dao.UserDao;
import com.sunrise.model.Role;
import com.sunrise.model.User;
import com.sunrise.model.dto.CreateUserRequest;
import com.sunrise.util.PasswordUtil;

public class AuthService {
    private final UserDao userDao = new UserDao();
    private final AuditService auditService = new AuditService();

    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new ServiceException(400, "Username and password are required");
        }
        User user = userDao.findByUsername(username.trim())
                .orElseThrow(() -> new ServiceException(401, "Invalid credentials"));
        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new ServiceException(401, "Invalid credentials");
        }
        auditService.record(user, "LOGIN", "Staff signed in");
        return user.withoutSecret();
    }

    public List<User> listUsers() {
        return userDao.findAll().stream().map(User::withoutSecret).toList();
    }

    public User createStaffUser(CreateUserRequest request, User actor) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new ServiceException(400, "Username is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new ServiceException(400, "Password must be at least 6 characters");
        }
        Optional<User> existing = userDao.findByUsername(request.getUsername().trim());
        if (existing.isPresent()) {
            throw new ServiceException(409, "Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPasswordHash(PasswordUtil.hash(request.getPassword()));
        user.setRole(request.getRole() == null ? Role.STAFF : request.getRole());
        user.setId(userDao.insert(user));
        auditService.record(actor, "USER_CREATE", "Created account " + user.getUsername());
        return user.withoutSecret();
    }

    public void ensureDefaultAccounts() {
        if (userDao.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(PasswordUtil.hash("password"));
            admin.setRole(Role.ADMIN);
            userDao.insert(admin);
        }
        if (userDao.findByUsername("staff").isEmpty()) {
            User staff = new User();
            staff.setUsername("staff");
            staff.setPasswordHash(PasswordUtil.hash("staff123"));
            staff.setRole(Role.STAFF);
            userDao.insert(staff);
        }
    }
}
