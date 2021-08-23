package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
	//validações @Valid pra usar as validações do model
	@PostMapping(value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult ) {

		//carrega o telefone dos filho pra poder atualizar pq está cascade = CascadeType.ALL
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		//bindingResult objetos de erro
		if(bindingResult.hasErrors()) { //se tiver erro vai entrar aqui dentro
			
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll();
			modelAndView.addObject("pessoas", pessoasIterator);
			
			//passa o objeto que ta vindo da view vai continuar os dados da pessoa
			//fazer a validação e mostrar os erros que tá tendo
			modelAndView.addObject("pessoaobj", pessoa);
			
			List<String> msgValidacao = new ArrayList<String>();
			
			//varre os erros encontrados pelo objetos de erros e a lista do binding
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				
				//getDefaultMessage() vem das anotações da model NotBlank e outras
				msgValidacao.add(objectError.getDefaultMessage());
			}
			
			modelAndView.addObject("msg", msgValidacao);
			
			return modelAndView;
		}
		
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
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa,
			@RequestParam("pesqsexo") String pesqsexo) {
		
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
			
			//carrega os telefones dessa pessoa
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
			
			return modelAndView;
		}
		
		//n interessa oque vem antes da url, e junto vai vir o pessoa id que clicamos no mome tabela
		@PostMapping("**/addfonePessoa/{pessoaid}")
		public	ModelAndView addFonePessoa(@Valid  Telefone telefone , 
				@PathVariable("pessoaid")Long pessoaid) {
			
			//código da pessoa que recebeu .get com seus dados classe pai
			Pessoa pessoa =  pessoaRepository.findById(pessoaid).get();
			
			if  (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty())  {
				
				//traz o retorno pro cadastro telefones
				ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
				
				modelAndView.addObject("pessoaobj", pessoa); //objeto pessoa retorna pra mostra ele
				
				//carrega a lista de teleofnes da pessoa
				modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
				 
				List<String> msg  = new  ArrayList<String>();
				
				if(telefone.getNumero().isEmpty()) {
				msg.add("Número deve ser informado");
				}
				
				if(telefone.getTipo().isEmpty()) {
					msg.add("Tipo deve ser informado");
					}
				
				modelAndView.addObject("msg", msg );//variavel msg adiciona lista de msg de erros 
				return modelAndView;
			}
			
			telefone.setPessoa(pessoa); //seta o telefone pra essa pessoa do id que ta recebendo
			
			telefoneRepository.save(telefone); //salva método repository passando o telefone com os dados
			
			//traz o retorno pro cadastro telefones
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			
			//passa o pai pra retorna pra sua tela onde cadastro o telefone com seu id
			modelAndView.addObject("pessoaobj", pessoa);
			
			//passa pro telefones o métodos de vir a lista de telefone do usuario no método telefone
			//vai receber o método id da pessoa que é o pai do telefone e vai consultar pelo repository 
			//pela query vai consultar apenas pra gente os telefones dessa pessoa que foi selecionada
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
			return modelAndView;
		}
		
		@GetMapping("/removertelefone/{idtelefone}")
		public ModelAndView	excluirTelefone(@PathVariable("idtelefone") Long idtelefone) {
			
			//retorna o objeto pai telefone e pessoa 
		Pessoa pessoa =	telefoneRepository.findById(idtelefone).get().getPessoa();
			
	        telefoneRepository.deleteById(idtelefone); //deleta por id o telefone que quer  
			
			//vai retorna pra mesma tela de cadastro o retorno
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			
			//objeto pessoa pai pra mostrar na tela
			modelAndView.addObject("pessoaobj", new Pessoa());
			
			//carregar os objetos novamente menos o que foi removido
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
			
			return modelAndView;
		}
		
		
}
