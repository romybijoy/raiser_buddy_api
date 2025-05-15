package com.project.raiserbuddy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Employee {

    @Id
    private int id;
    private String name;
    private int salary;



}
