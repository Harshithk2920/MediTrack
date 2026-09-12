package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

/**
 * Abstract class representing a Person in the medical system.
 * Extends MedicalEntity. Demonstrates Inheritance, Constructor Chaining, and Encapsulation.
 */
public abstract class Person extends MedicalEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String email;

    /**
     * Primary constructor demonstrating constructor chaining.
     */
    public Person(String id, String name, int age, String gender, String contactNumber, String email) {
        super(id);
        setName(name);
        setAge(age);
        this.gender = gender;
        setContactNumber(contactNumber);
        setEmail(email);
    }

    /**
     * Overloaded constructor demonstrating constructor chaining with 'this(...)'.
     */
    public Person(String id, String name, int age, String contactNumber) {
        this(id, name, age, "Unspecified", contactNumber, "unknown@meditrack.com");
    }

    // Getters and Setters with Validation
    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.validateNonEmptyString(name, "Name");
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        Validator.validateAge(age);
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        if (contactNumber != null && !contactNumber.isEmpty()) {
            Validator.validatePhone(contactNumber);
        }
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty()) {
            Validator.validateEmail(email);
        }
        this.email = email;
    }
}
