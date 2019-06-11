package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.stereotype.Service;
import pl.edu.agh.im.remotepatientmonitor.auth.EmailService;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;
import pl.edu.agh.im.remotepatientmonitor.domain.Device;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;

    private final DeviceRepository deviceRepository;

    private final EmailService emailService;

    public HeartRateService(HeartRateRepository heartRateRepository, DeviceRepository deviceRepository, EmailService emailService) {
        this.heartRateRepository = heartRateRepository;
        this.deviceRepository = deviceRepository;
        this.emailService = emailService;
    }

    private Map<String, HeartRateRecord> records = new ConcurrentHashMap<>();

    boolean saveRecordForDevice(String deviceId, Integer heartRate, LocalDateTime timestamp) throws MessagingException {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            HeartRateRecord record = new HeartRateRecord(heartRate, timestamp);
            record.setDevice(device);
            heartRateRepository.save(record);
            checkHeartRate(deviceId, record, device.getUser());
            return true;
        }
        return false;
    }

    private void checkHeartRate(String deviceId, HeartRateRecord record, ApplicationUser user) throws MessagingException {
        if (record.getHeartRate() > user.getHeartRateLimit()) {
            if (records.containsKey(deviceId)) {
                HeartRateRecord record1 = records.get(deviceId);
                if (record1.getTimestamp().plus(1, ChronoUnit.MINUTES).isBefore(record.getTimestamp())) {
                    sendAlert(record, user);
                    records.put(deviceId, record);
                }
            } else {
                records.put(deviceId, record);
                sendAlert(record, user);
            }
        }
    }

    private void sendAlert(HeartRateRecord record, ApplicationUser user) throws MessagingException {
        emailService.sendAlert(user,
                String.format("At %s your heartbeat exceeded your limit with value %d.",
                        record.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME),
                        record.getHeartRate())
        );
    }

    List<HeartRateRecord> getRecords(String deviceId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return heartRateRepository.getRecordInDateRange(deviceId, dateFrom, dateTo);
    }
}

