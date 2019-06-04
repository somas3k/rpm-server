package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.im.remotepatientmonitor.domain.Device;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HeartRateService {

    @Autowired
    private HeartRateRepository heartRateRepository;

    @Autowired
    private DeviceRepository deviceRepository;


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


}

