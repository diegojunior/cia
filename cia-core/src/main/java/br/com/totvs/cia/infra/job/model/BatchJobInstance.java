package br.com.totvs.cia.infra.job.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BATCH_JOB_INSTANCE", uniqueConstraints = @UniqueConstraint(columnNames = {"JOB_NAME", "JOB_KEY"}))
public class BatchJobInstance implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="jobSequence")
	@SequenceGenerator(name="jobSequence", sequenceName="BATCH_JOB_SEQ")
	@Column(name = "JOB_INSTANCE_ID", nullable = false)
	private Long id;
	
	@Column(name = "VERSION")
	private Long version;
	
	@Column(name = "JOB_NAME", nullable = false)
	private String jobName;
	
	@Column(name="JOB_KEY", nullable = false)
	private String jobKey;

	@Override
	public String getId() {
		return this.id.toString();
	}
}
