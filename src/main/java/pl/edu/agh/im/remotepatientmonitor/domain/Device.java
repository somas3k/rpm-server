package pl.edu.agh.im.remotepatientmonitor.domain;

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
    private ApplicationUser user;

    @OneToMany
    List<HeartRateRecord> records = new ArrayList<>();

}
