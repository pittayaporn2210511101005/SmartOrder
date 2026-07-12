package com.example.smartOrder.dailyReport;

import com.example.smartOrder.Noti.Notification;
import com.example.smartOrder.Noti.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

@Component
public class DailyReportScheduler {

    private static final ZoneId BANGKOK_ZONE = ZoneId.of("Asia/Bangkok");

    private final DailyReportService dailyReportService;
    private final NotificationRepository notificationRepository;

    public DailyReportScheduler(
            DailyReportService dailyReportService,
            NotificationRepository notificationRepository
    ) {
        this.dailyReportService = dailyReportService;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Bangkok")
    public void generateDailyReportAtNight() {

        // ตอนเวลา 00:00 ของวันใหม่ ให้สร้างรายงานของเมื่อวาน
        LocalDate reportDate =
                LocalDate.now(BANGKOK_ZONE).minusDays(1);

        boolean alreadyCreated =
                notificationRepository
                        .existsByDailyReport_ReportDateAndTargetmobileTrue(
                                reportDate
                        );

        if (alreadyCreated) {
            System.out.println(
                    "สร้างแจ้งเตือนสรุปยอดขายไปแล้ว : " + reportDate
            );
            return;
        }

        DailyReport report =
                dailyReportService.generateReport(reportDate);

        NumberFormat numberFormat =
                NumberFormat.getNumberInstance(
                        new Locale("th", "TH")
                );

        Notification notification = new Notification();

        notification.setTargetAdmin(false);
        notification.setTargetmobile(true);
        notification.setDateSent(
                LocalDateTime.now(BANGKOK_ZONE)
        );
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setDailyReport(report);

        notification.setMessage(
                "สรุปยอดขายประจำวันที่ " + reportDate +
                        " ยอดขาย ฿" +
                        numberFormat.format(report.getTotalSell()) +
                        " ต้นทุน ฿" +
                        numberFormat.format(report.getTotalCost()) +
                        " กำไร ฿" +
                        numberFormat.format(report.getProfit())
        );

        notificationRepository.save(notification);

        System.out.println(
                "สร้างรายงานและแจ้งเตือนมือถือแล้ว : " + reportDate
        );
    }
}