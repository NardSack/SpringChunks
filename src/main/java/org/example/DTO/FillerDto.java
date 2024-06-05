package org.example.DTO;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
@Getter@Setter@ToString
public class FillerDto  extends BaseEntity{

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

    @CsvBindByName(column = "type")
    private String type;

    @Override
    public boolean equals(Object o) {// defining how to compare if objects are equal/same
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillerDto fillerDto = (FillerDto) o;
        return Objects.equals(accountnumber, fillerDto.accountnumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountnumber);
    }
}
