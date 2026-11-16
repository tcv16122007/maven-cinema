package com.asm.maven_cinema.enums;

public enum UserStatus {
    ACTIVE("Hoạt động"),
    BLOCKED("Đã khóa");

    private final String display;
    UserStatus(String display) { this.display = display; }
    public String getDisplay() { return display; }
}