package com.vidara.tradecenter.common.constants;

public final class AppConstants {

    private AppConstants() {} // Prevent instantiation

    // Pagination
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final int MAX_PAGE_SIZE = 50;

    // JWT
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final long JWT_REFRESH_EXPIRATION_MS = 604800000; // 7 days
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    // File Upload
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String UPLOAD_DIR = "uploads";
    public static final String[] ALLOWED_IMAGE_TYPES = {
            "image/jpeg", "image/png", "image/webp"
    };

    // Roles
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_AGENT = "ROLE_AGENT";
}