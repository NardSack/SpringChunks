package org.example.DTO;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Setter
@Getter@ToString

public class CustomerAccountDTO extends BaseEntity{

    @CsvBindByName (column = "id")
    private Long id;

    @CsvBindByName(column = "accountdebt")
    private String accountdebt;

    @CsvBindByName(column = "accountnumber")
    private String accountnumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAccountDTO customerAccountDTO = (CustomerAccountDTO) o;
        return Objects.equals(accountnumber, customerAccountDTO.accountnumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountnumber);
    }
}
