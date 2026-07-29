package com.Bank.Banking.DTO.NotificationDTO;

import com.Bank.Banking.Enum.RecipientScope;
import com.Bank.Banking.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long notificationId;
    private String title;
    private String message;
    private RecipientScope recipientScope;
    private Role recipientRole;
    private Long recipientUserId;
    private boolean read;
    private Instant readAt;
    private String sourceType;
    private String sourceId;
    private Instant createdAt;
}

