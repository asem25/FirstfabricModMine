package ru.semavin.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.semavin.entity.MessageEntity;

import javax.sql.DataSource;
import java.util.Properties;

public final class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static void init() {
        if (sessionFactory != null) return;

        String url  = System.getenv().getOrDefault("DB_URL",  System.getProperty("db_url",  "jdbc:postgresql://localhost:5432/mc"));
        String user = System.getenv().getOrDefault("DB_USER", System.getProperty("db_user", "mc_user"));
        String pass = System.getenv().getOrDefault("DB_PASS", System.getProperty("db_password", "mc_pass"));

        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl(url);
        hc.setUsername(user);
        hc.setPassword(pass);
        hc.setMaximumPoolSize(5);
        DataSource ds = new HikariDataSource(hc);

        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "false");
        props.put("hibernate.format_sql", "false");

        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(MessageEntity.class);
        cfg.setProperties(props);
        cfg.getProperties().put("hibernate.hikari.dataSource", ds);

        sessionFactory = cfg.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) init();
        return sessionFactory;
    }

    private HibernateUtil() {}
}
