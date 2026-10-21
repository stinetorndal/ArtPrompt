package app.config;

import app.utils.Utils;
import jakarta.persistence.EntityManagerFactory;

import java.util.Properties;


public final class HibernateConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/";

    private static volatile EntityManagerFactory emf;
    private static volatile EntityManagerFactory emfTest;

    private HibernateConfig() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (HibernateConfig.class) {
                if (emf == null) {
                    emf = HibernateEmfBuilder.build(buildProps());
                }
            }
        }
        return emf;
    }

    // Bruges kun til JUnit integrationstests
    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        if (emfTest == null) {
            synchronized (HibernateConfig.class) {
                if (emfTest == null) {
                    emfTest = HibernateEmfBuilder.build(buildTestProps());
                }
            }
        }
        return emfTest;
    }

    private static Properties buildProps() {
        Properties props = HibernateBaseProperties.createBase();
        // Teaching-friendly default - change to update in production
        props.put("hibernate.hbm2ddl.auto", "update");

        if ("true".equalsIgnoreCase(System.getenv("DEPLOYED"))) {
            applyDeployedConnection(props);
        } else {
            applyConfigFileConnection(props, "");
        }
        return props;
    }

    // Test-databasen er den almindelige database med "_test" som suffix
    private static Properties buildTestProps() {
        Properties props = HibernateBaseProperties.createBase();
        // Friske tabeller fra bunden hver gang tests køres
        props.put("hibernate.hbm2ddl.auto", "create");

        applyConfigFileConnection(props, "_test");
        return props;
    }

    private static void applyDeployedConnection(Properties props) {
        props.setProperty("hibernate.connection.url",
                System.getenv("CONNECTION_STR") + System.getenv("DB_NAME"));
        props.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
        props.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
    }

    private static void applyConfigFileConnection(Properties props, String dbNameSuffix) {
        String dbName = config("DB_NAME") + dbNameSuffix;
        String connectionStr = config("CONNECTION_STR");

        // Hvis CONNECTION_STR findes (fx via GitHub Secrets), bruges den. Ellers fallback til localhost.
        String baseUrl = (connectionStr != null && !connectionStr.isBlank())
                ? connectionStr
                : LOCAL_URL;

        props.put("hibernate.connection.url", baseUrl + dbName);
        props.put("hibernate.connection.username", config("DB_USERNAME"));
        props.put("hibernate.connection.password", config("DB_PASSWORD"));
    }

    private static String config(String key) {
        return Utils.getPropertyValue(key, CONFIG_FILE);
    }
}