package org.example.Listener;

import lombok.RequiredArgsConstructor;
import org.example.DTO.FillerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileItemWriter;



@RequiredArgsConstructor
public class StepExecutionListenerCustom implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(StepExecutionListenerCustom.class);
    private FlatFileItemWriter<FillerDto> writer;
    @Override
    public ExitStatus afterStep(StepExecution stepExecution)
    {
        try{
            log.warn("step done");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            // Return ExitStatus.COMPLETED to indicate success
            return ExitStatus.COMPLETED;
        } else {
            // Return ExitStatus.FAILED to indicate failure
            return ExitStatus.FAILED;
        }
    }
}
