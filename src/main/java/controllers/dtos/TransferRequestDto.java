package controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TransferRequestDto {

    private String clientNameFrom;

    private String clientNameTo;

    private String currency;

    private Long amountMoney;

    public TransferRequestDto(String clientNameFrom, String currency, Long amountMoney) {
        this.clientNameFrom = clientNameFrom;
        this.currency = currency;
        this.amountMoney = amountMoney;
        this.clientNameTo = null;
    }
}
