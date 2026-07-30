package com.Bank.Banking.Entity;

import com.Bank.Banking.Enum.RecipientScope;
import com.Bank.Banking.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipientScope recipientScope;

    @Enumerated(EnumType.STRING)
    private Role recipientRole;

    private Long recipientUserId;

    @Column(nullable = false)
    private boolean isRead;

    private Instant readAt;

    private String sourceType;

    private String sourceId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}

