package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;

import javax.persistence.Temporal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/heartrate")
public class HeartRateController {
    private static Logger LOGGER = Logger.getLogger(HeartRateController.class.getSimpleName());

    @Autowired
    private HeartRateService service;

    @GetMapping(value = "/{deviceId}", params = {"heartRate", "timestamp"})
    public ResponseEntity saveRecord(@PathVariable String deviceId, @RequestParam Integer heartRate, @RequestParam String timestamp) {
        LOGGER.log(Level.INFO, "Got record from " + deviceId + " bpm: " + heartRate + " timestamp: " + timestamp);
        return service.saveRecordForDevice(deviceId, heartRate, LocalDateTime.parse(timestamp)) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


    @GetMapping(value = "/{deviceId}", params = {"dateFrom", "dateTo"})
    public ResponseEntity<List<HeartRateRecord>> getHeartRecords(@PathVariable String deviceId, @RequestParam String dateFrom, @RequestParam String dateTo){
        LOGGER.log(Level.INFO, "Got request for device " + deviceId + " with " + dateFrom + " and " + dateTo);
        return ResponseEntity.ok(service.getRecords(deviceId, LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo)));

    }


}
