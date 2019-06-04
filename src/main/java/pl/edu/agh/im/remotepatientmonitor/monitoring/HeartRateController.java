package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/heartrate")
public class HeartRateController {
    private static Logger LOGGER = Logger.getLogger(HeartRateController.class.getSimpleName());

    @Autowired
    private HeartRateService service;

    @GetMapping("/{deviceId}")
    public ResponseEntity saveRecord(@PathVariable String deviceId, @RequestParam Integer heartRate, @RequestParam String timestamp){
        LOGGER.log(Level.INFO, "Got record from " + deviceId + " bpm: " + heartRate + " timestamp: " + timestamp);
        return service.saveRecordForDevice(deviceId, heartRate, LocalDateTime.parse(timestamp)) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}
