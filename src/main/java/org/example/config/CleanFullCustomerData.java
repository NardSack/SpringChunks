package org.example.config;

import org.example.DTO.FillerDto;
import org.example.DTO.FullCustomerDTO;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;



public class CleanFullCustomerData implements ItemProcessor<FillerDto, FullCustomerDTO> {

    public static SortedMap<String, FillerDto> incompleteList = new TreeMap<>();

    @Override
    public FullCustomerDTO process(FillerDto item) throws Exception {
        FullCustomerDTO finalDTO = fillerToFullCustomerDTO(item);

        if (!incompleteList.containsKey(finalDTO.getAccountnumber())){// not in treemap
            incompleteList.put(finalDTO.getAccountnumber(), item);
            return null;
        }
        else {
            FillerDto savedData = incompleteList.get(finalDTO.getAccountnumber());
            if (Objects.equals(savedData.getType(), "Account")&& Objects.equals(item.getType(),"Customer")) {
                finalDTO.setAccountnumber(savedData.getAccountnumber());
                finalDTO.setAccountdebt(savedData.getAccountdebt());
                finalDTO.setId(savedData.getId());
            }
            else if (Objects.equals(savedData.getType(), "Customer")&& Objects.equals(item.getType(),"Account")){
                finalDTO.setFirstname(savedData.getFirstname());
                finalDTO.setEmail(savedData.getEmail());
                finalDTO.setLastname(savedData.getLastname());
                finalDTO.setId(savedData.getId());
            }
            else {finalDTO = null;}
            return finalDTO;
        }

    }

    private FullCustomerDTO fillerToFullCustomerDTO(FillerDto item) {
        return FullCustomerDTO.builder()
                .id(item.getId())
                .email(item.getEmail())
                .accountdebt(item.getAccountdebt())
                .lastname(item.getLastname())
                .firstname(item.getFirstname())
                .accountnumber(item.getAccountnumber())
                .build();
    }


}
