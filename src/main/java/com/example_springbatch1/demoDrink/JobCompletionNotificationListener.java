package com.example_springbatch1.demoDrink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;


public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("job--finish");
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("!!! JOB FINISHED! Time to verify the results");

            String query = "SELECT brand, origin, characteristics FROM coffee";
            jdbcTemplate.query(query, (rs, row) -> new Drink(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(drink -> LOGGER.info("Found < {} > in the database.", drink));
        }
    }
}
