package com.Bank.Banking.Controller;

import com.Bank.Banking.DTO.NotificationDTO.NotificationRequest;
import com.Bank.Banking.DTO.NotificationDTO.NotificationResponse;
import com.Bank.Banking.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    @GetMapping("/my/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponse>> getMyUnreadNotifications() {
        return ResponseEntity.ok(notificationService.getMyUnreadNotifications());
    }

    @GetMapping("/my/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getMyUnreadCount() {
        return ResponseEntity.ok(notificationService.getMyUnreadCount());
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @PutMapping("/my/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }
}

