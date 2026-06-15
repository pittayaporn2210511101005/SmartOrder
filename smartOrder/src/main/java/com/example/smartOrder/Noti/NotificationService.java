package com.example.smartOrder.Noti;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getMobileNotifications() {
        return notificationRepository.findByTargetmobileTrueOrderByDateSentDesc();
    }

    public List<Notification> getUnreadMobileNotifications() {
        return notificationRepository.findByTargetmobileTrueAndIsReadFalseOrderByDateSentDesc();
    }

    public Notification markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบแจ้งเตือน id: " + id));

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }
}