package com.Bank.Banking.Service.Impl;

import com.Bank.Banking.DTO.NotificationDTO.NotificationRequest;
import com.Bank.Banking.DTO.NotificationDTO.NotificationResponse;
import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Entity.Notification;
import com.Bank.Banking.Enum.RecipientScope;
import com.Bank.Banking.Enum.Role;
import com.Bank.Banking.Mapper.NotificationMapper;
import com.Bank.Banking.Repository.NotificationRepository;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private AuthUser getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authenticated user not found.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        validateRequest(request);
        Notification notification = Notification.builder()
                .title(request.getTitle().trim())
                .message(request.getMessage().trim())
                .recipientScope(request.getRecipientScope())
                .recipientRole(request.getRecipientRole())
                .recipientUserId(request.getRecipientUserId())
                .sourceType(request.getSourceType())
                .sourceId(request.getSourceId())
                .isRead(false)
                .build();
        return NotificationMapper.toResponse(notificationRepository.save(notification));
    }

    @Override
    public NotificationResponse notifyAllUsers(String title, String message, String sourceType, String sourceId) {
        return createNotification(NotificationRequest.builder()
                .title(title)
                .message(message)
                .recipientScope(RecipientScope.ALL)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .build());
    }

    @Override
    public NotificationResponse notifyRole(Role role, String title, String message, String sourceType, String sourceId) {
        return createNotification(NotificationRequest.builder()
                .title(title)
                .message(message)
                .recipientScope(RecipientScope.ROLE)
                .recipientRole(role)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .build());
    }

    @Override
    public NotificationResponse notifyUser(Long userId, String title, String message, String sourceType, String sourceId) {
        return createNotification(NotificationRequest.builder()
                .title(title)
                .message(message)
                .recipientScope(RecipientScope.USER)
                .recipientUserId(userId)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications() {
        AuthUser user = getLoggedInUser();
        return notificationRepository.findVisibleNotificationsForUser(user.getId(), user.getRole())
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyUnreadNotifications() {
        AuthUser user = getLoggedInUser();
        return notificationRepository.findUnreadVisibleNotificationsForUser(user.getId(), user.getRole())
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long getMyUnreadCount() {
        AuthUser user = getLoggedInUser();
        return notificationRepository.countUnreadVisibleNotificationsForUser(user.getId(), user.getRole());
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        AuthUser user = getLoggedInUser();
        Notification notification = notificationRepository.findVisibleNotificationById(notificationId, user.getId(), user.getRole())
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(Instant.now());
            notification = notificationRepository.save(notification);
        }
        return NotificationMapper.toResponse(notification);
    }

    @Override
    public void markAllAsRead() {
        AuthUser user = getLoggedInUser();
        List<Notification> notifications = notificationRepository.findUnreadNotificationsForRecipients(user.getId(), user.getRole());
        for (Notification notification : notifications) {
            notification.setRead(true);
            notification.setReadAt(Instant.now());
        }
        notificationRepository.saveAll(notifications);
    }

    private void validateRequest(NotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Notification request must not be null.");
        }
        if (request.getRecipientScope() == RecipientScope.ROLE && request.getRecipientRole() == null) {
            throw new IllegalArgumentException("Recipient role is required for role-scoped notifications.");
        }
        if (request.getRecipientScope() == RecipientScope.USER && request.getRecipientUserId() == null) {
            throw new IllegalArgumentException("Recipient user id is required for user-scoped notifications.");
        }
    }
}

