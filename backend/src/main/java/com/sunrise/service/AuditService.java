package com.sunrise.service;

import java.util.List;

import com.sunrise.dao.AuditLogDao;
import com.sunrise.model.AuditLog;
import com.sunrise.model.User;

public class AuditService {
    private final AuditLogDao auditLogDao = new AuditLogDao();

    public void record(User actor, String action, String details) {
        Integer userId = actor == null ? null : actor.getId();
        String username = actor == null ? "system" : actor.getUsername();
        auditLogDao.insert(new AuditLog(userId, username, action, details));
    }

    public List<AuditLog> recent() {
        return auditLogDao.findRecent(100);
    }
}
