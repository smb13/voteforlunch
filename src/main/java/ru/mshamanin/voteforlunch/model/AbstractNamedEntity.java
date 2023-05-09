package ru.mshamanin.voteforlunch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "name", nullable = false)
    protected String name;

    protected AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return super.toString() + '(' + name + ')';
    }
}