package org.example.DTO;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Setter@Getter@ToString
public class CustomerDTO extends BaseEntity {
    @CsvBindByName(column="id")
    private Long id;

    @CsvBindByName(column = "firstname")
    private String firstname;

    @CsvBindByName (column = "lastname")
    private String lastname;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "accountnumber")
    private String accountnumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO customerDTO = (CustomerDTO) o;
        return Objects.equals(accountnumber, customerDTO.accountnumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountnumber);
    }
}
