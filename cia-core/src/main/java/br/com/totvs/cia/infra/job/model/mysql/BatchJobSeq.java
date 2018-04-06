package br.com.totvs.cia.infra.job.model.mysql;

import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "BATCH_JOB_SEQ", uniqueConstraints = @UniqueConstraint(columnNames = {"UNIQUE_KEY"}))
public class BatchJobSeq implements Model {
	
	private static final long serialVersionUID = 1L;

//	@Id
//	@Column(name = "ID", nullable = false)
	private Long id;
	
//	@Column(name = "UNIQUE_KEY")
	private String unique;

	@Override
	public String getId() {
		return this.id.toString();
	}
}
