package pl.edu.agh.im.remotepatientmonitor.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class RefreshToken {
    private String refreshToken;
}
