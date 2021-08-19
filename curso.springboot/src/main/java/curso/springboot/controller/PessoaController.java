package curso.springboot.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//retorna uma pessoa vázia pra poder aparecer a tela normalmente
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;
	}

	// **/salvarpessoa ele intercepta salvar pessoa de qualquer forma
	//ignora tudo que venha antes e pegue a pessoa que quer salvar
	@PostMapping(value = "**/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {

		pessoaRepository.save(pessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

		// retorna iterable por isso n vai por uma lista
		Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoasIterator);
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());

		return  modelAndView;
	}

	@GetMapping(value = "/listapessoas")
	public ModelAndView pessoas() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		// retorna iterable por isso n vai por uma lista
		Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoasIterator);
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;
	}
	//pega na url o id pessoa
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView	editar(@PathVariable("idpessoa") Long idpessoa) {
		
		//vai buscar o id da pessoa carregar o objeto do banco de dados consultou
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		//vai retorna pra mesma tela de cadastro o retorno
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//vai addicionar o objeto pessoa com get que vai buscar seus dados pra edição
		modelAndView.addObject("pessoaobj", pessoa.get());
		
		
		return modelAndView;
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView	excluir(@PathVariable("idpessoa") Long idpessoa) {
		
        pessoaRepository.deleteById(idpessoa); //deleta por id  
		
		//vai retorna pra mesma tela de cadastro o retorno
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//vai objeto pessoas da tabela pra atualizar qnd excluir alguma pessoa
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		
		return modelAndView;
	}
}
