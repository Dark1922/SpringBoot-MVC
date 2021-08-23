package curso.springboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.springboot.model.Usuario;

@Repository
@org.springframework.transaction.annotation.Transactional
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	//select usuario from na tabela Usuario onde usuario.login =  ao 1 parametro login passado
	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
}
