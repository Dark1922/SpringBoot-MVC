package curso.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;

@Repository
@Transactional //transações com o banco de dados
public interface PessoaRepository extends CrudRepository<Pessoa, Long>{

	
}
