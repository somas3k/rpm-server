package pl.edu.agh.im.remotepatientmonitor.monitoring;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HeartRateRepository extends CrudRepository<HeartRateRecord, Long> {

    @Query("from HeartRateRecord as ht where ht.device.id = ?1 and ht.timestamp between ?2 and ?3 order by ht.timestamp")
    List<HeartRateRecord> getRecordInDateRange(String deviceId, LocalDateTime dateFrom, LocalDateTime dateTo);
}
