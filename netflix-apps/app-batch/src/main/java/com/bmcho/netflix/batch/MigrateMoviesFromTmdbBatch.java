package com.bmcho.netflix.batch;

import com.bmcho.netflix.movie.FetchMovieUseCase;
import com.bmcho.netflix.movie.InsertMovieUseCase;
import com.bmcho.netflix.movie.response.MovieResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MigrateMoviesFromTmdbBatch {

    private final static String BATCH_NAME = "MigrateMoviesFromTmdbBatch";

    private final FetchMovieUseCase fetchMovieUseCase;
    private final InsertMovieUseCase insertMovieUseCase;

    @Bean(name = BATCH_NAME)
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder(BATCH_NAME, jobRepository)
            .preventRestart()
            .start(this.step(jobRepository, platformTransactionManager))
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean(name = "MigrateMoviesFromTmdbBatchTaskletStep")
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("MigrateMoviesFromTmdbBatchTaskletStep", jobRepository)
            .chunk(10, platformTransactionManager)
            .reader(new HttpPageItemReader(1, fetchMovieUseCase))
            .writer(chunk -> {
                List<MovieResponse> items = (List<MovieResponse>) chunk.getItems();
                insertMovieUseCase.insert(items);
            })
            .build();
    }

}
