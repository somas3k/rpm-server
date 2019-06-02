package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.im.remotepatientmonitor.domain.Device;

@Repository
public interface DeviceRepository extends CrudRepository<Device, String> {
}
