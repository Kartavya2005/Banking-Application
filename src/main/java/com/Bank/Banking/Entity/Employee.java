package com.Bank.Banking.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    private double salary;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private String address;

}
