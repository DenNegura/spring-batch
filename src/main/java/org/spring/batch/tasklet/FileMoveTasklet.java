package org.spring.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class FileMoveTasklet implements Tasklet {

    private final Resource resource;

    private final String pathToFolder;

    public FileMoveTasklet(Resource resource, String pathToFolder) {
        this.resource = resource;
        this.pathToFolder = pathToFolder;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        File file;
        try {
            file = resource.getFile();
            if (!file.renameTo(new File(pathToFolder + file.getName()))) {
                throw new IOException("file not move");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return RepeatStatus.FINISHED;
    }
}
