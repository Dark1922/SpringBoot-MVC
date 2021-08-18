package curso.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PessoaController {

	// autowride se n tivesse allargscontructor
	private PessoaRepository pessoaRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public String inicio() {

		return "cadastro/cadastropessoa";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {

		pessoaRepository.save(pessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

		// retorna iterable por isso n vai por uma lista
		Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoasIterator);

		return  modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

		// retorna iterable por isso n vai por uma lista
		Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoasIterator);

		return modelAndView;
	}
}
