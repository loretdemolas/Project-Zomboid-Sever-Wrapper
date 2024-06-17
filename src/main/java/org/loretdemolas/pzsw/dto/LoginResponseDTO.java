package org.loretdemolas.pzsw.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginResponseDTO {
    @Getter
    private String token;

    private long expiresIn;

}
