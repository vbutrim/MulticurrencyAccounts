package controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDto {

    private String name;

    private String passportId;

    private String ccyOfInitialAccount;

    public ClientRequestDto(String name, String passportId) {
        this.name = name;
        this.passportId = passportId;
        this.ccyOfInitialAccount = null;
    }
}
