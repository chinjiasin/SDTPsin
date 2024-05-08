package com.jiasinchin.SDTPsin.model;

public class Employee {
    private String id;
    private String surname;
    private String forename;

    public Employee(String id, String surname, String forename) {
        this.id = id;
        this.surname = surname;
        this.forename = forename;
    }

    public String getId() {
        return id;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return forename + " " + surname;
    }
}
