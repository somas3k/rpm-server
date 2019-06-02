package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/heartrate")
public class HeartRateController {

    @Autowired
    private HeartRateService service;

    @GetMapping("/{deviceId}")
    public ResponseEntity saveRecord(@PathVariable String deviceId, @RequestParam Integer heartRate, @RequestParam Timestamp timestamp){
        return service.saveRecordForDevice(deviceId, heartRate, timestamp) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}
