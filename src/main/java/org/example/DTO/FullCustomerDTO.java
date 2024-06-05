package org.example.DTO;


import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
public class FullCustomerDTO extends BaseEntity {

    @CsvBindByName(column="id")
    private Long id;

    @CsvBindByName(column = "firstname")
    private String firstname;

    @CsvBindByName (column = "lastname")
    private String lastname;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "accountdebt")
    private String accountdebt;

    @CsvBindByName(column = "accountnumber")
    private String accountnumber;

    @Override
    public boolean equals(Object o) {// defining how to compare if objects are equal/same
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullCustomerDTO fullCustomerDTO = (FullCustomerDTO) o;
        return Objects.equals(accountnumber, fullCustomerDTO.accountnumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountnumber);
    }

//    public String getaccountnumber() {
//        return accountnumber;
//    }
}
