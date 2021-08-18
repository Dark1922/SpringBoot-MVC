package curso.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PessoaController {

	//autowride se n tivesse allargscontructor
	private PessoaRepository pessoaRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public String inicio() {
		
		return "cadastro/cadastropessoa";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/salvarpessoa")
	public String salvar(Pessoa pessoa) {
		
		pessoaRepository.save(pessoa);
		return "cadastro/cadastropessoa";
	}
 }
