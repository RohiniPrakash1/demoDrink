package com.example_springbatch1.demoDrink;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableBatchProcessing
public class BatchConfiguration {
    @Value("${file.input}")
    private String fileInput;

    // ...
    @Bean
    public FlatFileItemReader reader() {
        System.out.println("reached reader");
        return new FlatFileItemReaderBuilder().name("drinkItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names(new String[] { "brand", "origin", "characteristics" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
                    setTargetType(Drink.class);
                }})
                .build();
    }
    @Bean
    public JdbcBatchItemWriter writer(DataSource dataSource) {
        System.out.println("reaching item writer");
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coffee (brand, origin, characteristics) VALUES (:brand, :origin, :characteristics)")
                .dataSource(dataSource)
                .build();
    }
    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        System.out.println("reaching JobBuilderstep1");
        return new JobBuilder("importUserJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, JdbcBatchItemWriter writer, PlatformTransactionManager transactionManager) {
        //PlatformTransactionManager transactionManager;
        System.out.println("reaching StepBuilderstep1");

        return new StepBuilder("step1",jobRepository)
                .<Drink, Drink> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
               // .transactionManager(transactionManager)
                .build();
    }
    @Bean
    public JobCompletionNotificationListener JobCompletionNotificationListener(){
        return new JobCompletionNotificationListener();
    }

    @Bean
    public DrinkItemProcessor processor() {
//        private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeItemProcessor.class);

//        @Override
//       // public Coffee process(final Coffee coffee) throws Exception {
//            final Coffee coffee;
//            System.out.println("processing the coffee list");
//            String brand = coffee.getBrand().toUpperCase();
//            String origin = coffee.getOrigin().toUpperCase();
//            String characteristics = coffee.getCharacteristics().toUpperCase();
//
//            Coffee transformedCoffee = new Coffee(brand, origin, characteristics);
//            LOGGER.info("Converting ( {} ) into ( {} )", coffee, transformedCoffee);
//
//            return transformedCoffee;
//        }
        System.out.println("calling processor");
        return new DrinkItemProcessor();
    }


}
