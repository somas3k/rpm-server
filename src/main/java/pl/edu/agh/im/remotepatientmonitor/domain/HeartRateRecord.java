package pl.edu.agh.im.remotepatientmonitor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeartRateRecord {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Device device;

    private int heartRate;
    private LocalDateTime timestamp;

    public HeartRateRecord(int heartRate, LocalDateTime timestamp) {
        this.heartRate = heartRate;
        this.timestamp = timestamp;
    }
}
