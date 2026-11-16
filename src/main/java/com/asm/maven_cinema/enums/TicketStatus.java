package com.asm.maven_cinema.enums;

public enum TicketStatus {
    PENDING("Chờ thanh toán"),
    PAID("Đã thanh toán"),
    CANCELLED("Đã hủy"),
    CHECKED_IN("Đã check-in");

    private final String display;
    TicketStatus(String display) { this.display = display; }
    public String getDisplay() { return display; }
}