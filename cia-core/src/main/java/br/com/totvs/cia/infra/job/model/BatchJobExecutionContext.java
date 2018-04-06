package br.com.totvs.cia.infra.job.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
public class BatchJobExecutionContext implements Model {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="JOB_EXECUTION_ID", nullable = false)
	private BatchJobExecution jobExecution;

	@Column(name = "SHORT_CONTEXT", nullable = false)
	private String shortContext;
	
	@Lob
	@Column(name = "SERIALIZED_CONTEXT")
	private String serializedContext;

	@Override
	public String getId() {
		return this.jobExecution.getId();
	}
}