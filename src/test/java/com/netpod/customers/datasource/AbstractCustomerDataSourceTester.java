package com.netpod.customers.datasource;

import com.netpod.customers.exception.DataNotFoundException;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractCustomerDataSourceTester {

    protected CustomerDataSource customerDataSource;
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Test
    public void givenValidFileWhenReadThenLinesFound() {
        customerDataSource = createDataSource("classpath:test-data.txt", resourceLoader);
        assertThat(customerDataSource.exists()).isTrue();
        assertThat(customerDataSource.read()).containsExactlyInAnyOrder("ABCD LMNP", "PQRS YYYYY");
    }

    @Test
    public void givenEmptyFileWhenCheckExistsThenDataSourceDoesExist() {
        customerDataSource = createDataSource("classpath:empty-file.txt", resourceLoader);
        assertThat(customerDataSource.exists()).isTrue();
    }

    @Test
    public void givenEmptyFileWhenReadThenEmptyList() {
        customerDataSource = createDataSource("classpath:empty-file.txt", resourceLoader);
        assertThat(customerDataSource.read()).isEmpty();
    }

    @Test
    public void givenFileDoesNotExistWhenCheckExistsThenDataSourceDoesNotExist() {
        customerDataSource = createDataSource("classpath:unknown-file.txt", resourceLoader);
        assertThat(customerDataSource.exists()).isFalse();
    }

    @Test(expected = DataNotFoundException.class)
    public void givenFileDoesNotExistWhenReadThenException() {
        customerDataSource = createDataSource("classpath:unknown-file.txt", resourceLoader);
        customerDataSource.read();
    }

    @Test
    public void givenEmptyFileNameWhenCheckExistsThenDataSourceDoesNotExist() {
        customerDataSource = createDataSource("", resourceLoader);
        assertThat(customerDataSource.exists()).isFalse();
    }

    @Test(expected = DataNotFoundException.class)
    public void givenEmptyFileNameWhenReadThenException() {
        customerDataSource = createDataSource("", resourceLoader);
        customerDataSource.read();
    }

    @Test
    public void givenNullFileNameWhenCheckExistsThenDataSourceDoesNotExist() {
        customerDataSource = createDataSource(null, resourceLoader);
        assertThat(customerDataSource.exists()).isFalse();
    }

    @Test(expected = DataNotFoundException.class)
    public void givenNullFileWhenReadThenException() {
        customerDataSource = createDataSource(null, resourceLoader);
        customerDataSource.read();
    }

    abstract CustomerDataSource createDataSource(String fileName, ResourceLoader resourceLoader);
}
