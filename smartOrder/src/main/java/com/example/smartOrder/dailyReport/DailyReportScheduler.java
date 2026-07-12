package com.example.smartOrder.dailyReport;

import com.example.smartOrder.Noti.Notification;
import com.example.smartOrder.Noti.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class DailyReportScheduler {

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
        LocalDate reportDate = LocalDate.now().minusDays(1);

        boolean alreadyCreated =
                notificationRepository
                        .existsByDailyReport_ReportDateAndTargetmobileTrue(reportDate);

        if (alreadyCreated) {
            System.out.println(
                    "สร้างแจ้งเตือนสรุปยอดขายไปแล้ว : " + reportDate
            );
            return;
        }

        DailyReport report = dailyReportService.generateReport(reportDate);

        NumberFormat nf =
                NumberFormat.getNumberInstance(new Locale("th", "TH"));

        Notification notification = new Notification();

        notification.setTargetAdmin(false);
        notification.setTargetmobile(true);
        notification.setDateSent(LocalDateTime.now());
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setDailyReport(report);

        notification.setMessage(
                "สรุปยอดขายประจำวันที่ " + reportDate +
                        " ยอดขาย ฿" + nf.format(report.getTotalSell()) +
                        " ต้นทุน ฿" + nf.format(report.getTotalCost()) +
                        " กำไร ฿" + nf.format(report.getProfit())
        );

        notificationRepository.save(notification);

        System.out.println(
                "สร้างรายงานและแจ้งเตือนมือถือแล้ว : " + reportDate
        );
    }
}