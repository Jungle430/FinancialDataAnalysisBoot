package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.mapper.StockMapper;
import com.taosdata.jdbc.TSDBDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@SpringBootTest(classes = FinancialDataAnalysisBoot.class)
@Slf4j
public class TDEngineDataBaseSQLTest {
    private final String jdbcUrl;

    private final StockMapper stockMapper;

    private boolean canConnTaoSiDataConnect() {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");

        log.info("try connect tao si database: TDengine");
        try (Connection conn = DriverManager.getConnection(jdbcUrl, connProps)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeQuery("SELECT SERVER_VERSION()");
                ResultSet rs = stmt.getResultSet();
                if (rs.next()) {
                    String version = rs.getString(1);
                    log.info("connected success! TD-engine server version: {}", version);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("can't connection TD-engine database, the database test will not exec");
            return false;
        }
    }

    @Autowired
    public TDEngineDataBaseSQLTest(@Value("${spring.datasource.url}") String jdbcUrl, StockMapper stockMapper, @Value("${personal.database.name}") String personalDatabaseName) {
        log.info("jdbcUrl: {}, database.name: {}", jdbcUrl, personalDatabaseName);
        this.jdbcUrl = jdbcUrl;
        this.stockMapper = stockMapper;
    }

    @Test
    @EnabledIf("canConnTaoSiDataConnect")
    public void TestStockSelect() {
        log.info("{}", stockMapper.queryByCompany("PDD", 5));
    }
}
