package dev.chiptune.springboot.entity;

public enum AuthType {
    USER, ADMIN, ALL;

    public String getName() {
        return this.name();
    }
}
