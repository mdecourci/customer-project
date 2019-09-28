package com.netpod.customers.datasource;

import com.netpod.customers.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileDataSource implements CustomerDataSource {
    private File file;

    private String fileName;
    private final ResourceLoader resourceLoader;

    @Autowired
    public FileDataSource(@Value("${customer.datafile}") String fileName, ResourceLoader resourceLoader) {
        this.fileName = fileName;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        log.info("Initialise file datasource filename = {}", this.fileName);
        if (StringUtils.isNotBlank(this.fileName)) {
            try {
                Resource resource = resourceLoader.getResource(fileName);
                if (resource != null) {
                    this.file = resource.getFile();
                }
            } catch (Throwable e) {
                log.error("File not found {}", fileName, e);
            }
        }
    }

    @Override
    public boolean exists() {
        return (file != null) ? file.exists() : false;
    }

    @Override
    public List<String> read() {
        log.info("Read lines from file {}", this.fileName);

        if (file == null || !file.exists()) {
            throw new DataNotFoundException("File cannot be located");
        }
        try {
            return Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (Throwable e) {
            throw new DataNotFoundException(e);
        }
    }
}
