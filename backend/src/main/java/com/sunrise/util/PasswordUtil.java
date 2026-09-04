package com.sunrise.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtil {
    private PasswordUtil() {}

    public static String hash(String plain) {
        return BCrypt.withDefaults().hashToString(10, plain.toCharArray());
    }

    public static boolean matches(String plain, String hash) {
        if (plain == null || hash == null) {
            return false;
        }
        return BCrypt.verifyer().verify(plain.toCharArray(), hash).verified;
    }
}
