package org.example.config;

import org.example.DTO.CustomerAccountDTO;
import org.example.DTO.FillerDto;
import org.springframework.batch.item.ItemProcessor;

public class AccountDtoToFiller implements ItemProcessor<CustomerAccountDTO, FillerDto> {
    @Override
    public FillerDto process(CustomerAccountDTO item) throws Exception {
        //convert Customer Dto to FullCustomerDto
        FillerDto fillerDto = new FillerDto();
        fillerDto.setId(item.getId());
        fillerDto.setAccountdebt(item.getAccountdebt());
        fillerDto.setAccountnumber(item.getAccountnumber());
        fillerDto.setType("Account");
        return fillerDto;
    }
}
