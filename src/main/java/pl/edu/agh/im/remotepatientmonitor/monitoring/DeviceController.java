package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.im.remotepatientmonitor.auth.UserRepository;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;
import pl.edu.agh.im.remotepatientmonitor.domain.Device;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository repository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{deviceId}")
    public ResponseEntity saveDevice(@PathVariable String deviceId, @RequestParam String deviceName, Principal principal) {
        try {
            ApplicationUser user = Optional.ofNullable(userRepository.findByEmail(principal.getName()))
                    .orElseThrow(NoSuchElementException::new);
            Device newDevice = new Device(deviceId, user, deviceName);
            if (repository.save(newDevice) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<Device>> getDevices(Principal principal) {
        try {
            ApplicationUser user = Optional.ofNullable(userRepository.findByEmail(principal.getName())).orElseThrow(NoSuchElementException::new);
            return ResponseEntity.ok(user.getDevices());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
