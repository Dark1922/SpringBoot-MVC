package curso.springboot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Entity
@Getter
@Setter
public class Telefone implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String numero;
	
	private String tipo;
	
	@ManyToOne
	@ForeignKey(name = "pessoa_id")
	private Pessoa pessoa;
}
