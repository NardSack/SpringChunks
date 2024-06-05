package org.example.config;

import org.example.DTO.CustomerDTO;
import org.example.DTO.FillerDto;
import org.springframework.batch.item.ItemProcessor;

public class CustomerDtoToFiller implements ItemProcessor<CustomerDTO, FillerDto> {

    @Override
    public FillerDto process(CustomerDTO customerDTO) throws Exception{
        //convert Customer Dto to FullCustomerDto
        FillerDto fillerDto = new FillerDto();
        fillerDto.setId(customerDTO.getId());
        fillerDto.setFirstname(customerDTO.getFirstname());
        fillerDto.setLastname(customerDTO.getLastname());
        fillerDto.setEmail(customerDTO.getEmail());
        fillerDto.setAccountnumber(customerDTO.getAccountnumber());
        fillerDto.setType("Customer");
        return fillerDto;
    }
}