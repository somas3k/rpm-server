package pl.edu.agh.im.remotepatientmonitor.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
}
