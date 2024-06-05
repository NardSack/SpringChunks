package org.example.config;


import lombok.extern.slf4j.Slf4j;
import org.example.DTO.CustomerAccountDTO;
import org.example.DTO.CustomerDTO;
import org.example.DTO.FillerDto;
import org.example.DTO.FullCustomerDTO;
import org.example.Listener.JobExecutionListenerCustom;
import org.example.Listener.StepExecutionListenerCustom;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.util.*;

@Component
@Slf4j
public class ImportCustomerJobConfig {

    @Value("${input.folder.customer}")
    private Resource[] resourcesCustomer;

    @Value("${input.folder.account}")
    private Resource[] resourcesAccount;

    @Value("${output.folder.resource}")
    private String ResourcePathName;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ImportCustomerJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }
//JOB
    @Bean
    public Job importCustomerJob()
    {
        return new JobBuilder("importCustomerJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importCustomerStep(jobRepository, transactionManager))
                .next(importAccountStep(jobRepository, transactionManager))
                .next(cleanDataStructure(jobRepository,transactionManager))
//                .next(SortDataStructure(jobRepository,transactionManager))
                .listener(new JobExecutionListenerCustom())
                .build();

    }
//STEPS
//    @Bean
//    private Step SortDataStructure(JobRepository jobRepository,PlatformTransactionManager transactionManager){
//        return new StepBuilder("SortedData",jobRepository)
//                .chunk(10,transactionManager)
//                .reader(reportReader())
//                .processor(sortedData())
//                .writer()
//                .build();
//    }

    @Bean
    private Step cleanDataStructure(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return  new StepBuilder("CleanData",jobRepository)
                .<FillerDto,FullCustomerDTO>chunk(5,transactionManager)
                .reader(fullCustomerDTOFlatFileItemReader())
                .processor(fullCustomerDTOItemProcessor())
                .writer(cleanedDataWriter())//
                .build();

    }

    @Bean
    public Step importAccountStep(JobRepository jobRepository,PlatformTransactionManager transactionManager)
    {
        return  new StepBuilder("ImporAccountStep",jobRepository)
                .listener(new StepExecutionListenerCustom())
                .<CustomerAccountDTO,FillerDto>chunk(5,transactionManager)
                .reader(multiResourceAccReader())
                .processor(accountProcessor())
                .writer(mergedDataWriter())
                .build();

    }

    @Bean
    public Step importCustomerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager)
    {
        return new StepBuilder("importCustomerStep",jobRepository)
                .listener(new StepExecutionListenerCustom())
                .<CustomerDTO, FillerDto>chunk(5,transactionManager)
                .reader(multiResourceItemReader())
                .processor(customerProcessor())
                .writer(DataWriterNoAppend())
                .build();
    }
//ITEMPROCESSORS
    @Bean
    public ItemProcessor<CustomerDTO, FillerDto> customerProcessor(){
        return new CustomerDtoToFiller();
    }

    @Bean
    public ItemProcessor<CustomerAccountDTO,FillerDto> accountProcessor(){
        return new AccountDtoToFiller();
    }

    @Bean
    public ItemProcessor<FillerDto,FullCustomerDTO> fullCustomerDTOItemProcessor()
    {
        return new CleanFullCustomerData();
    }
//    @Bean
//    public  ItemProcessor<List<String>,List<String>> sortedData() { return new SortingItemProcessor();}


//ITEM READER
        //Multi Reader
    public MultiResourceItemReader<CustomerDTO> multiResourceItemReader(){ // to be called in STEP reader
        return new MultiResourceItemReaderBuilder<CustomerDTO>()
                .name("Customer Resources reader")
                .resources(resourcesCustomer)
                .delegate(customerFlatFileItemReader())
                .build();
    }

    public MultiResourceItemReader<CustomerAccountDTO> multiResourceAccReader(){ // to be called in STEP reader
        return new MultiResourceItemReaderBuilder<CustomerAccountDTO>()
                .name("Customer Resources reader")
                .resources(resourcesAccount)
                .delegate(customerAccountDTOFlatFileItemReader())
                .build();
    }
            // single file reader
//    @StepScope
//    @Bean
//    public FlatFileItemReader<FullCustomerDTO> reportReader(){
//        return new FlatFileItemReaderBuilder<FullCustomerDTO>()
//                .name("reportReader")
//                .resource(new FileSystemResource(ResourcePathName+"/OutputFiles/FinalReport.csv"))
//                .delimited()
//                .delimiter("|")
//                .names("id","firstname","lastname","email","accountdebt","accountnumber","type")
//                .targetType(FullCustomerDTO.class)
//                .linesToSkip(1)
//                .build();
//    }

    @StepScope
    @Bean
    public FlatFileItemReader<FillerDto> fullCustomerDTOFlatFileItemReader()
    {
        return new FlatFileItemReaderBuilder<FillerDto>()
                .name("FullCustomerDTO item reader")
                .resource(new FileSystemResource(ResourcePathName+"/OutputFiles/FullCustomer.csv"))
                .saveState(Boolean.FALSE)
                .linesToSkip(0)
                .delimited()
                .delimiter("|")
                .names("id","firstname","lastname","email","accountdebt","accountnumber","type")
                .targetType(FillerDto.class)
                .build();
    }



    @StepScope
    @Bean
    public FlatFileItemReader<CustomerDTO> customerFlatFileItemReader()//Customer File Reader
    {
        return new FlatFileItemReaderBuilder<CustomerDTO>()
                .name("CustomerDTO item reader")
                .resource(new ClassPathResource("/InputFiles/data/CustomerData.csv"))
                .saveState(Boolean.FALSE)
                .linesToSkip(1)
                .delimited()
                .delimiter("|")
                .names("id","firstname","lastname","email","accountnumber")
                .targetType(CustomerDTO.class)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<CustomerAccountDTO> customerAccountDTOFlatFileItemReader(){
        return new FlatFileItemReaderBuilder<CustomerAccountDTO>()
                .name("Customer Account Item Reader")
                .saveState(Boolean.FALSE)
                .resource(new ClassPathResource("/InputFiles/*Account*.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter("|")
                .names("id","accountdebt","accountnumber")
                .targetType(CustomerAccountDTO.class)
                .build();
    }



//ITEM WRITER

    @StepScope
    @Bean
    public FlatFileItemWriter<FillerDto> DataWriterNoAppend(){
        return new FlatFileItemWriterBuilder<FillerDto>()
                .name("Merged data Writer")
                .resource(new FileSystemResource(ResourcePathName+"/OutputFiles/FullCustomer.csv"))
                .delimited()
                .delimiter("|")
                .names("id","firstname","lastname","email","accountdebt","accountnumber","type")
                .append(false)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<FillerDto> mergedDataWriter(){
        return new FlatFileItemWriterBuilder<FillerDto>()
                .name("Merged data Writer")
                .resource(new FileSystemResource(ResourcePathName+"/OutputFiles/FullCustomer.csv"))
                .delimited()
                .delimiter("|")
                .names("id","firstname","lastname","email","accountdebt","accountnumber","type")
                .append(true)
                .build();
    }
//C:/Users/Ngee Ann/Microservice Demo/SpringBatchMFiles/src/main/resources/OutputFiles/FinalReport.csv
    @StepScope
    @Bean
    public FlatFileItemWriter<FullCustomerDTO> cleanedDataWriter(){
        FlatFileItemWriter<FullCustomerDTO> writer =  new FlatFileItemWriterBuilder<FullCustomerDTO>()
                .name("Clean data Writer")
                .resource(new FileSystemResource(new File(ResourcePathName+"/OutputFiles/FinalReport.csv")))
                .delimited()
                .delimiter("|")
                .names("id","firstname","lastname","email","accountdebt","accountnumber")
                .build();
        writer.setHeaderCallback(writer1 -> writer1.write("id|firstname|lastname|email|accountdebt|accountnumber"));
        writer.setFooterCallback(footer-> footer.write("Created @ : "+ new Date()));
        return writer;
    }
//    @Bean
//    public ItemWriter<FullCustomerDTO> finalReportWriter(){
//        return new SortedItemWriter(cleanedDataWriter());
//    }



}
