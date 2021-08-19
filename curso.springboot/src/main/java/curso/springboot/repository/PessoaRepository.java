package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;

@Repository
@Transactional //transações com o banco de dados
public interface PessoaRepository extends CrudRepository<Pessoa, Long>{

	//parte de consultar pessoa p abreviação like por parte de nome
	@Query("select p from Pessoa p where p.nome like %?1%") //1 parametro ?1
	List<Pessoa> findPessoaByName(String nome);
	
}
