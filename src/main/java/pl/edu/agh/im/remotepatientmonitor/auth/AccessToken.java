package pl.edu.agh.im.remotepatientmonitor.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {
    private String accessToken;
    private String refreshToken;
}
