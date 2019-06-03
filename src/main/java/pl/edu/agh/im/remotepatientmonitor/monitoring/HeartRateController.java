package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/heartrate")
public class HeartRateController {

    @Autowired
    private HeartRateService service;

    @GetMapping("/{deviceId}")
    public ResponseEntity saveRecord(@PathVariable String deviceId, @RequestParam Integer heartRate, @RequestParam LocalDateTime timestamp){
        System.out.println("Got record from " + deviceId + " bpm: " + heartRate + " timestamp: " + timestamp);
        return service.saveRecordForDevice(deviceId, heartRate, Timestamp.valueOf(timestamp)) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}
