package curso.springboot.model;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "Nome é obrigatório")
	@NotEmpty(message = "Nome é obrigatório")
	private String nome;
	
	@NotNull(message = "Sobrenome é obrigatório")
	@NotEmpty(message = "Sobrenome é obrigatório")
	private String sobrenome;
	
	@Min(value = 18, message = "Idade Inválida apenas maiores de 18 anos")
	private int idade;
	
	//referencia o nome do relacionamento da outra classe
	//orphanRemoval = true, cascade = CascadeType.ALL permite excluir em cascata
	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL) 
	private List<Telefone> telefones;
	

}
