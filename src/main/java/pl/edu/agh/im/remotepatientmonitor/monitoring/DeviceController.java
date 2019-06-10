package pl.edu.agh.im.remotepatientmonitor.monitoring;

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

    private final DeviceRepository repository;

    private final UserRepository userRepository;

    public DeviceController(DeviceRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity deleteDevice(@PathVariable String deviceId, Principal principal) {
        try {
            ApplicationUser user = Optional.ofNullable(userRepository.findByEmail(principal.getName()))
                    .orElseThrow(NoSuchElementException::new);

            if (user != null && user.getDevices().stream().filter(device -> device.getId().equals(deviceId)).count() == 1) {
                repository.deleteById(deviceId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{deviceId}", params = {"deviceName"})
    public ResponseEntity<Device> saveDevice(@PathVariable String deviceId, @RequestParam String deviceName, Principal principal) {
        Device newDevice;
        try {
            ApplicationUser user = Optional.ofNullable(userRepository.findByEmail(principal.getName()))
                    .orElseThrow(NoSuchElementException::new);
            newDevice = new Device(deviceId, user, deviceName);
            if (repository.findById(deviceId).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            repository.save(newDevice);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newDevice);
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
