package com.renan.webstore.model;

public enum Role {
    ADMIN("ADMIN"), PUBLIC("PUBLIC");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
