package com.zebone.dataway.config;

import net.hasor.core.ApiBinder;
import net.hasor.core.DimModule;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@DimModule
@Component
public class DataWayModule implements SpringModule {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("datasourceB")
    private DataSource datasourceB;

    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        apiBinder.installModule(new JdbcModule(Level.Full, this.dataSource));
        apiBinder.installModule(new JdbcModule(Level.Full, "datasourceB", this.datasourceB ));
    }
}
