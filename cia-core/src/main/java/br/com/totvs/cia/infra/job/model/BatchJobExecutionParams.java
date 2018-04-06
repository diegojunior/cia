package br.com.totvs.cia.infra.job.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.totvs.cia.infra.model.Model;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams implements Model  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "JOB_EXECUTION_ID")
	private BatchJobExecution jobExecution;
	
	@Id
	@Column(name="TYPE_CD", nullable = false)
	private String tipoCodigo;
	
	@Id
	@Column(name="KEY_NAME", nullable = false)
	private String keyName;
	
	@Column(name="STRING_VAL")
	private String stringValue;
	
	@Column(name="DATE_VAL")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateValue;
	
	@Column(name="LONG_VAL")
	private Long longValue;
	
	@Column(name="DOUBLE_VAL", columnDefinition="NUMBER")
	private Double doubleValue;
	
	@Column(name="IDENTIFYING", nullable = false)
	private String identifying;

	@Override
	public String getId() {
		return this.jobExecution.getId();
	}
}