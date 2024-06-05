package org.example.config;

import org.example.DTO.FullCustomerDTO;
import org.springframework.batch.item.ItemProcessor;

import java.util.Collections;
import java.util.List;

public class SortingItemProcessor implements ItemProcessor<FullCustomerDTO,FullCustomerDTO> {

    @Override
    public FullCustomerDTO process(FullCustomerDTO item) throws Exception {

        return null;
    }
}
