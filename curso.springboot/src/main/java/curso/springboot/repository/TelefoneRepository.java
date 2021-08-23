package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.springboot.model.Telefone;

@Repository
@org.springframework.transaction.annotation.Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long> {

	@Query("select t from Telefone t where t.pessoa.id = ?1")
	public List<Telefone> getTelefones(Long pessoaid);
}
