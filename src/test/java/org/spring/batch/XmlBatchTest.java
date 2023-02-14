package org.spring.batch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlBatchTest {

    @Test
    void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/springBashContext.xml");

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("jobXml");

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
