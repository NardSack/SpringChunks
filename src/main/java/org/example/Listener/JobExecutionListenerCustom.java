package org.example.Listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.io.File;

@RequiredArgsConstructor
public class JobExecutionListenerCustom implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobExecutionListenerCustom.class);

    @Override
    public void afterJob(JobExecution jobExecution){
        File file = new File("src/main/resources/OutputFiles/FullCustomer.csv");
        if (file.exists())
        {
            if (!file.delete())
            {
                log.warn("Failed to delete file");
            }
        }
    }
}
