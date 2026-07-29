package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.Notification;
import com.Bank.Banking.Enum.RecipientScope;
import com.Bank.Banking.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE " +
            "n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ALL OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ROLE AND n.recipientRole = :role) OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.USER AND n.recipientUserId = :userId) " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findVisibleNotificationsForUser(
            @Param("userId") Long userId,
            @Param("role") Role role);

    @Query("SELECT n FROM Notification n WHERE " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ALL OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ROLE AND n.recipientRole = :role) OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.USER AND n.recipientUserId = :userId)) " +
            "AND n.isRead = false " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findUnreadVisibleNotificationsForUser(
            @Param("userId") Long userId,
            @Param("role") Role role);

    @Query("SELECT COUNT(n) FROM Notification n WHERE " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ALL OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ROLE AND n.recipientRole = :role) OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.USER AND n.recipientUserId = :userId)) " +
            "AND n.isRead = false")
    long countUnreadVisibleNotificationsForUser(
            @Param("userId") Long userId,
            @Param("role") Role role);

    @Query("SELECT n FROM Notification n WHERE n.notificationId = :notificationId AND (" +
            "n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ALL OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ROLE AND n.recipientRole = :role) OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.USER AND n.recipientUserId = :userId))")
    Optional<Notification> findVisibleNotificationById(
            @Param("notificationId") Long notificationId,
            @Param("userId") Long userId,
            @Param("role") Role role);

    @Query("SELECT n FROM Notification n WHERE " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ALL OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.ROLE AND n.recipientRole = :role) OR " +
            "(n.recipientScope = com.Bank.Banking.Enum.RecipientScope.USER AND n.recipientUserId = :userId)) " +
            "AND n.isRead = false")
    List<Notification> findUnreadNotificationsForRecipients(
            @Param("userId") Long userId,
            @Param("role") Role role);
}

