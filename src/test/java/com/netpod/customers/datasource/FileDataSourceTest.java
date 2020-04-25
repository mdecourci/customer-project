package com.netpod.customers.datasource;


import org.springframework.core.io.ResourceLoader;

public class FileDataSourceTest extends AbstractCustomerDataSourceTester {

    @Override
    CustomerDataSource createDataSource(String fileName, ResourceLoader resourceLoader) {
        FileDataSource fileDataSource = new FileDataSource(fileName, resourceLoader);
        fileDataSource.init();
        return fileDataSource;
    }
}