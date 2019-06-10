package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.im.remotepatientmonitor.auth.UserRepository;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/heartrate")
public class HeartRateController {
    private static Logger LOGGER = Logger.getLogger(HeartRateController.class.getSimpleName());

    private final HeartRateService service;

    private final UserRepository userRepository;

    public HeartRateController(HeartRateService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/{deviceId}", params = {"heartRate", "timestamp"})
    public ResponseEntity saveRecord(@PathVariable String deviceId, @RequestParam Integer heartRate, @RequestParam String timestamp) {
        LOGGER.log(Level.INFO, "Got record from " + deviceId + " bpm: " + heartRate + " timestamp: " + timestamp);
        return service.saveRecordForDevice(deviceId, heartRate, LocalDateTime.parse(timestamp)) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


    @GetMapping(value = "/{deviceId}", params = {"dateFrom", "dateTo"})
    public ResponseEntity getHeartRecords(
            @PathVariable String deviceId,
            @RequestParam String dateFrom,
            @RequestParam String dateTo,
            Principal principal){
        LOGGER.log(Level.INFO, "Got request for device " + deviceId + " with " + dateFrom + " and " + dateTo);
        ApplicationUser user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        if (user.getDevices().stream().filter(device -> device.getId().equals(deviceId)).count() == 1) {
            return ResponseEntity.ok(service.getRecords(deviceId, LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo)));
        }
        else return ResponseEntity.badRequest().body("This device doesn't signed to this user!");
    }


}
