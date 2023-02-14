package org.spring.batch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spring.batch.config.SpringBatchConfig;
import org.spring.batch.config.SpringConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {SpringConfig.class, SpringBatchConfig.class})
public class AnnotationBatchTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Test
    void test() {
        boolean status = false;
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Job failed");
        }

        Assertions.assertTrue(status);
    }
}
