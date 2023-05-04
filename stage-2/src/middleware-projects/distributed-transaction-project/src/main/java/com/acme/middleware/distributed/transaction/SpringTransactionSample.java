package com.acme.middleware.distributed.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * todo
 *
 * @author qian.he
 * @since 2023-04-17
 * @version 1.0.0
 */
public class SpringTransactionSample {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String jdbcURL1 = "jdbc:mysql://127.0.0.1:3306/test";
        DriverManagerDataSource dataSource = new DriverManagerDataSource(jdbcURL1, "root", "123456");
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    }


}
