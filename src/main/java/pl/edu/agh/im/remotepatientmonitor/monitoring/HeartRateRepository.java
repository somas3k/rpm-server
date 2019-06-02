package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;

@Repository
public interface HeartRateRepository extends CrudRepository<HeartRateRecord, Long> {
}
