package org.loretdemolas.pzsw.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginUserDTO {
    private String username;

    private  String password;
}
