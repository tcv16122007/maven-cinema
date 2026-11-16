package com.asm.maven_cinema.enums;

public enum MovieStatus {
    NOW_SHOWING("Đang chiếu"),
    COMING_SOON("Sắp chiếu"),
    STOPPED("Ngừng chiếu");

    private final String display;
    MovieStatus(String display) { this.display = display; }
    public String getDisplay() { return display; }
}