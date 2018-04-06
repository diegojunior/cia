package br.com.totvs.cia.infra.job.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
@PropertySources({
	@PropertySource("classpath:cia-batch-job.properties"), 
	@PropertySource(value = "file:${CIA_HOME}/cia-batch-job.properties", ignoreResourceNotFound = true)})
public class BatchJobConfiguration {
	
	private static final Logger log = Logger.getLogger(BatchJobConfiguration.class);
	
	@Resource
    private Environment env;
	
	private static final String TABLEPREFIX = "batch.repository.tableprefix";
	private static final String DATABASE_TYPE = "batch.repository.database.type";
	private static final String ISOLATE_LEVEL_FOR_CREATE = "batch.repository.isolationlevelforcreate";
	
	private DataSource dataSource;

	private PlatformTransactionManager transactionManager;

	@Autowired
	protected void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
		this.transactionManager = new DataSourceTransactionManager(dataSource);
	}

	public JobRepository createJobRepository() {
		
		try {
			
			JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
			factory.setDataSource(this.dataSource);
			factory.setTransactionManager(this.transactionManager);
			factory.setDatabaseType(this.env.getProperty(DATABASE_TYPE));
			factory.setIsolationLevelForCreate(this.env.getProperty(ISOLATE_LEVEL_FOR_CREATE));
			factory.setTablePrefix(this.env.getProperty(TABLEPREFIX));
			factory.afterPropertiesSet();
			
			return (JobRepository) factory.getObject();
			
		} catch (Exception e) {
			
			log.error("Não foi possível configurar o repositório de Jobs do Spring Batch", e);
			
			throw new RuntimeException(e);
			
		}
	}
}