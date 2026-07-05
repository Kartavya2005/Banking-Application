package com.Bank.Banking.Entity;


import com.Bank.Banking.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Builder
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String PhoneNumber;

    @Column(nullable = false)
    private String Password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
