package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.stereotype.Service;
import pl.edu.agh.im.remotepatientmonitor.domain.Device;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;

    private final DeviceRepository deviceRepository;

    public HeartRateService(HeartRateRepository heartRateRepository, DeviceRepository deviceRepository) {
        this.heartRateRepository = heartRateRepository;
        this.deviceRepository = deviceRepository;
    }


    boolean saveRecordForDevice(String deviceId, Integer heartRate, LocalDateTime timestamp) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            HeartRateRecord record = new HeartRateRecord(heartRate, timestamp);
            record.setDevice(device);
            heartRateRepository.save(record);
            return true;
        }
        return false;
    }


    List<HeartRateRecord> getRecords(String deviceId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return heartRateRepository.getRecordInDateRange(deviceId, dateFrom, dateTo);
    }
}

