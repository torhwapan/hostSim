package com.example.xmlparser.model;

public class Simulator {
    private String name;
    private String connection;
    private String description;

    public Simulator() {
    }

    public Simulator(String name, String connection, String description) {
        this.name = name;
        this.connection = connection;
        this.description = description;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}