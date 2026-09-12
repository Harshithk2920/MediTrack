package com.airtribe.meditrack.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Abstract Entity for all medical records and people in MediTrack.
 * Demonstrates Abstraction, Encapsulation, Serialization, and Cloneable support.
 */
public abstract class MedicalEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String id;
    private final LocalDateTime createdAt;

    public MedicalEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public MedicalEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    public MedicalEntity(String id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Abstract method enforcing dynamic polymorphism in concrete subclasses.
     *
     * @return Formatted detail representation
     */
    public abstract String getDetails();

    @Override
    public MedicalEntity clone() {
        try {
            return (MedicalEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed for MedicalEntity subclass", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalEntity entity = (MedicalEntity) o;
        return id != null ? id.equals(entity.id) : entity.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
