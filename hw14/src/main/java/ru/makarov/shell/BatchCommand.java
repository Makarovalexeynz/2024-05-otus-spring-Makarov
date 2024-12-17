package ru.makarov.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.batch.core.Job;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommand {

    private final JobLauncher jobLauncher;

    private final Job magrationJob;


    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        try {
            JobExecution jobExecution = jobLauncher.run(magrationJob, new JobParameters());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
