package br.com.totvs.cia.infra.job.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BATCH_JOB_EXECUTION")
public class BatchJobExecution implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="jobExecutionSequence")
	@SequenceGenerator(name="jobExecutionSequence", sequenceName="BATCH_JOB_EXECUTION_SEQ")
	@Column(name = "JOB_EXECUTION_ID", nullable = false)
	private Long id;
	
	@Column(name = "VERSION")
	private Long version;
	
	@ManyToOne
	@JoinColumn(name="JOB_INSTANCE_ID", nullable = false)
	private BatchJobInstance jobInstance;
	
	@Column(name = "CREATE_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "EXIT_CODE", length = 2500)
	private String exiteCode;
	
	@Column(name = "EXIT_MESSAGE", length = 2500)
	private String exiteMessage;

	@Column(name = "LAST_UPDATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;  
	
	@Column(name = "JOB_CONFIGURATION_LOCATION")
	private String jobConfigurationLocation;
	
	@Override
	public String getId() {
		return this.id.toString();
	}
}
