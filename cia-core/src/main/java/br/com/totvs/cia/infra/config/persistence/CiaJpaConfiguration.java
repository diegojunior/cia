package br.com.totvs.cia.infra.config.persistence;

import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("br.com.totvs.cia")
@PropertySources({
	@PropertySource("classpath:cia-application.properties"), 
	@PropertySource(value = "file:${CIA_HOME}/cia-application.properties", ignoreResourceNotFound = true)})
@EnableTransactionManagement
public class CiaJpaConfiguration {
	
	private static final String HIBERNATE_ORDER_UPDATES = "hibernate.order_updates";
	private static final String HIBERNATE_ORDER_INSERTS = "hibernate.order_inserts";
	private static final String JNDI_DATASOURCE = "jndi.datasource";
	private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	private static final String HIBERNATE_HBM2DDL_AUTO_TEST = "create-drop";
	private static final String HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
	private static final String PACKAGE_TO_SCAN = "entitymanager.packages.to.scan";
	
	@Resource
    private Environment env;
	
	private Properties properties;

	@Bean
	public EntityManagerFactory entityManagerFactory(final DataSource dataSource) {
		//Implementacao da JPA que no caso sera do hibernate
		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(this.env.getProperty(PACKAGE_TO_SCAN));
		
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(this.properties);
		em.afterPropertiesSet();
		
		return em.getObject();
	}

	@Bean
	@Profile({"dev", "prod"})
	public DataSource dataSource() {
		this.loadProdProperties();
		final JndiDataSourceLookup ds = new JndiDataSourceLookup();
		return ds.getDataSource(this.env.getProperty(JNDI_DATASOURCE));
	}
	
	@Bean
	@Profile("test")
	public DataSource dataSourceTest() {
		this.loadTestProperties();
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).build();
	}

	// Create a transaction manager
	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(emf);
		return jpaTransactionManager;
	}

	private void loadProdProperties() {
		this.properties = new Properties();
		this.properties.setProperty(HIBERNATE_HBM2DDL_AUTO, this.env.getProperty(HIBERNATE_HBM2DDL_AUTO));
		this.properties.setProperty(HIBERNATE_DIALECT, this.env.getProperty(HIBERNATE_DIALECT));
		this.properties.setProperty(HIBERNATE_SHOW_SQL, this.env.getProperty(HIBERNATE_SHOW_SQL));
		this.properties.setProperty(HIBERNATE_JDBC_BATCH_SIZE, this.env.getProperty(HIBERNATE_JDBC_BATCH_SIZE));
		this.properties.setProperty(HIBERNATE_ORDER_INSERTS, this.env.getProperty(HIBERNATE_ORDER_INSERTS));
		this.properties.setProperty(HIBERNATE_ORDER_UPDATES, this.env.getProperty(HIBERNATE_ORDER_UPDATES));
	}
	
	private void loadTestProperties() {
		this.properties = new Properties();
		this.properties.setProperty(HIBERNATE_HBM2DDL_AUTO, HIBERNATE_HBM2DDL_AUTO_TEST);
		this.properties.setProperty(HIBERNATE_DIALECT, this.env.getProperty(HIBERNATE_DIALECT));
		this.properties.setProperty(HIBERNATE_SHOW_SQL, this.env.getProperty(HIBERNATE_SHOW_SQL));
		this.properties.setProperty(HIBERNATE_JDBC_BATCH_SIZE, this.env.getProperty(HIBERNATE_JDBC_BATCH_SIZE));
		this.properties.setProperty(HIBERNATE_ORDER_INSERTS, this.env.getProperty(HIBERNATE_ORDER_INSERTS));
		this.properties.setProperty(HIBERNATE_ORDER_UPDATES, this.env.getProperty(HIBERNATE_ORDER_UPDATES));
	}
}