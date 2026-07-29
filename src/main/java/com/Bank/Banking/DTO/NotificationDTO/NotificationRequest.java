package com.Bank.Banking.DTO.NotificationDTO;

import com.Bank.Banking.Enum.RecipientScope;
import com.Bank.Banking.Enum.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Recipient scope is required")
    private RecipientScope recipientScope;

    private Role recipientRole;

    private Long recipientUserId;

    private String sourceType;

    private String sourceId;
}

