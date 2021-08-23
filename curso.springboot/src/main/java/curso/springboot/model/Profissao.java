package curso.springboot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profissao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	private String nome;
	
	

}
