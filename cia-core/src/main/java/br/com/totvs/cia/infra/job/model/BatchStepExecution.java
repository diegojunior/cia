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
@Table(name = "BATCH_STEP_EXECUTION")
public class BatchStepExecution implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="stepExecutionSequence")
	@SequenceGenerator(name="stepExecutionSequence", sequenceName="BATCH_STEP_EXECUTION_SEQ")
	@Column(name = "STEP_EXECUTION_ID", nullable = false)
	private Long id;
	
	@Column(name = "VERSION", nullable = false)
	private Long version;
	
	@Column(name = "STEP_NAME", nullable = false)
	private String stepName;
	
	@ManyToOne
	@JoinColumn(name="JOB_EXECUTION_ID", nullable = false)
	private BatchJobExecution jobExecution;
	
	@Column(name = "START_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "COMMIT_COUNT")
	private Long commitCount;
	
	@Column(name = "READ_COUNT")
	private Long readCount;
	
	@Column(name = "FILTER_COUNT")
	private Long filterCount;
	
	@Column(name = "WRITE_COUNT")
	private Long writeCount;
	
	@Column(name = "READ_SKIP_COUNT")
	private Long readSkipCount;
	
	@Column(name = "WRITE_SKIP_COUNT")
	private Long writeSkipCount;
	
	@Column(name = "PROCESS_SKIP_COUNT")
	private Long processSkipCount;
	
	@Column(name = "ROLLBACK_COUNT")
	private Long rollbackCount;
	
	@Column(name = "EXIT_CODE", length = 2500)
	private String exiteCode;
	
	@Column(name = "EXIT_MESSAGE", length = 2500)
	private String exiteMessage;

	@Column(name = "LAST_UPDATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;
	
	@Override
	public String getId() {
		return this.id.toString();
	}
}