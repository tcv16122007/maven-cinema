package com.asm.maven_cinema.enums;

public enum Role {
    ADMIN("Quản trị"),
    STAFF("Nhân viên"),
    CUSTOMER("Khách hàng");

    private final String display;
    Role(String display) { this.display = display; }
    public String getDisplay() { return display; }
}