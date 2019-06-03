package pl.edu.agh.im.remotepatientmonitor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    private String fullName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    Boolean enabled;

    @JsonIgnore
    Long emailVerificationExpirationTime;

    @OneToMany(mappedBy = "user")
    List<Device> devices = new ArrayList<>();

    public ApplicationUser(Long id, String email, String fullName, String password, Boolean enabled, Long emailVerificationExpirationTime) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.enabled = enabled;
        this.emailVerificationExpirationTime = emailVerificationExpirationTime;
    }

    public ApplicationUser withEncodedPassword(BCryptPasswordEncoder encoder) {
        return new ApplicationUser(id, email, fullName, encoder.encode(password), false, System.currentTimeMillis());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationUser that = (ApplicationUser) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return email != null ? email.equals(that.email) : that.email == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
