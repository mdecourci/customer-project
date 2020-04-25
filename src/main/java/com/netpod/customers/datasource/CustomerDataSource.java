package com.netpod.customers.datasource;

import java.util.List;

public interface CustomerDataSource {
    boolean exists();

    List<String> read();
}
