package controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {

    private String clientName;

    private String currency;

    public AccountRequestDto(String clientName) {
        this.clientName = clientName;
        this.currency = null;
    }
}
