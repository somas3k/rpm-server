package pl.edu.agh.im.remotepatientmonitor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private String id;

    @ManyToOne
    @JsonIgnore
    private ApplicationUser user;

    private String deviceName;

    public Device(String id, ApplicationUser user, String deviceName) {
        this.id = id;
        this.user = user;
        this.deviceName = deviceName;
    }

    @OneToMany(mappedBy = "device")
    @JsonIgnore
    List<HeartRateRecord> records = new ArrayList<>();



}
