package com.Bank.Banking.Service;

import com.Bank.Banking.DTO.NotificationDTO.NotificationRequest;
import com.Bank.Banking.DTO.NotificationDTO.NotificationResponse;
import com.Bank.Banking.Enum.Role;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    NotificationResponse notifyAllUsers(String title, String message, String sourceType, String sourceId);

    NotificationResponse notifyRole(Role role, String title, String message, String sourceType, String sourceId);

    NotificationResponse notifyUser(Long userId, String title, String message, String sourceType, String sourceId);

    List<NotificationResponse> getMyNotifications();

    List<NotificationResponse> getMyUnreadNotifications();

    long getMyUnreadCount();

    NotificationResponse markAsRead(Long notificationId);

    void markAllAsRead();
}

