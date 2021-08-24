package curso.springboot.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	@NotBlank(message = "Nome é Obrigatório.")
	private String nome;
	
	@NotBlank(message = "Sobrenome Obrigatório.")
	private String sobrenome;
	
	@Min(value = 18, message = "Idade Inválida apenas maiores de 18 anos.")
	private int idade;
	
	@NotBlank(message = "Informe o Sexo.")
	private String sexopessoa;
	
	@ManyToOne //muitas pessoas para essa profissão
	private Profissao profissao;
	
	@NotBlank(message = "Informe o Cep.")
	private String cep;
	
	@Enumerated(EnumType.STRING) //tag qie vai usar o enum e do tipo string
	private Cargo cargo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Lob
	private byte[] curriculo;
	
	private String nomeFileCurriculo;

	private String tipoFileCurriculo;
 	
	private String rua;
	
	private String bairro;
	
	private String cidade;
	
	private String uf;
	
	private String ibge;

    private String complemento;	
	
	//referencia o nome do relacionamento da outra classe
	//orphanRemoval = true, cascade = CascadeType.ALL permite excluir em cascata
	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL) 
	private List<Telefone> telefones;
	

}
