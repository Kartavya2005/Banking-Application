package com.Bank.Banking.Mapper;

import com.Bank.Banking.DTO.NotificationDTO.NotificationResponse;
import com.Bank.Banking.Entity.Notification;

public class NotificationMapper {

    private NotificationMapper() {
    }

    public static NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .recipientScope(notification.getRecipientScope())
                .recipientRole(notification.getRecipientRole())
                .recipientUserId(notification.getRecipientUserId())
                .read(notification.isRead())
                .readAt(notification.getReadAt())
                .sourceType(notification.getSourceType())
                .sourceId(notification.getSourceId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

