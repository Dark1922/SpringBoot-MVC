package curso.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.springboot.model.Profissao;

@Repository
@org.springframework.transaction.annotation.Transactional
public interface ProfissaoRepository extends CrudRepository<Profissao, Long> {

}
