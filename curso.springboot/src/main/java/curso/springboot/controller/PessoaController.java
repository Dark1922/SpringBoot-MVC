package curso.springboot.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PessoaController {

	// autowride se n tivesse allargscontructor
	private PessoaRepository pessoaRepository;
	private TelefoneRepository telefoneRepository; 
   

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//retorna uma pessoa vázia pra poder aparecer a tela normalmente
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		//carrega a lista qnd entrar no cadastropessoa
		// retorna iterable por isso n vai por uma lista
		Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoasIterator);

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
	
	//n interessa oque vem antes de url
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		
		//vai consultar e retornar para mesma tela de cadastro
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//objeto pessoas vai passar o parametro de pesquisar por nome
		//vai pesquisar todas pessoas de acordo com a pesquisar que vier
		modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa.trim().toUpperCase()));
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	//pega na url o id pessoa
		@GetMapping("/telefones/{idpessoa}")
		public ModelAndView	telefones(@PathVariable("idpessoa") Long idpessoa) {
			
			//vai buscar o id da pessoa carregar o objeto do banco de dados q consultou
			Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
			
			//vai retorna pra mesma tela de cadastro o retorno
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			
			//vai addicionar o objeto pessoa com get que vai buscar seus dados pra edição
			modelAndView.addObject("pessoaobj", pessoa.get());
			
			
			return modelAndView;
		}
		
		//n interessa oque vem antes da url, e junto vai vir o pessoa id que clicamos no mome tabela
		@PostMapping("**/addfonePessoa/{pessoaid}")
		public	ModelAndView addFonePessoa(Telefone telefone , 
				@PathVariable("pessoaid")Long pessoaid) {
			
			//código da pessoa que recebeu .get com seus dados
			Pessoa pessoa =  pessoaRepository.findById(pessoaid).get();
			
			telefone.setPessoa(pessoa); //seta o telefone pra essa pessoa do id que ta recebendo
			
			telefoneRepository.save(telefone); //salva método repository passando o telefone com os dados
			
			//traz o retorno pro cadastro telefones
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			
			//passa o pai pra retorna pra sua tela onde cadastro o telefone com seu id
			modelAndView.addObject("pessoaobj", pessoa);
			
			return modelAndView;
		}
}
