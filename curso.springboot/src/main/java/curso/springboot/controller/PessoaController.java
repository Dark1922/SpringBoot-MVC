package curso.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.ProfissaoRepository;
import curso.springboot.repository.TelefoneRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PessoaController {

	// autowride se n tivesse allargscontructor
	private PessoaRepository pessoaRepository;
	private TelefoneRepository telefoneRepository; 
	private ReportUtil reportUtil;
	private ProfissaoRepository profissaoRepository;
   

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//retorna uma pessoa vázia pra poder aparecer a tela normalmente
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		//carrega a lista qnd entrar no cadastropessoa
		// retorna iterable por isso n vai por uma lista
		Iterable<Pessoa> pessoasIterator = pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoasIterator);
		
		//carrega todas profissao
		modelAndView.addObject("profissoes", profissaoRepository.findAll());

		return modelAndView;
	}

	// **/salvarpessoa ele intercepta salvar pessoa de qualquer forma
	//ignora tudo que venha antes e pegue a pessoa que quer salvar
	//validações @Valid pra usar as validações do model
	@PostMapping(value = "**/salvarpessoa", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult,
			final MultipartFile file) throws IOException {

		//carrega o telefone dos filho pra poder atualizar pq está cascade = CascadeType.ALL
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		//bindingResult objetos de erro
		if(bindingResult.hasErrors()) { //se tiver erro vai entrar aqui dentro
			
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			
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
		
		if (file.getSize() > 0) { //salvando com upload se tiver colocado
			pessoa.setCurriculo(file.getBytes());
			pessoa.setTipoFileCurriculo(file.getContentType());
			pessoa.setNomeFileCurriculo(file.getOriginalFilename());
		
		}else {
			if (pessoa.getId() != null && pessoa.getId() > 0) {//editando pessoa com upload
				Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
				pessoa.setCurriculo(pessoaTemp.getCurriculo());
				pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
				pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
			}
		}
		
		pessoaRepository.save(pessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");


		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());

		return  modelAndView;
	}

	@GetMapping(value = "/listapessoas")
	public ModelAndView pessoas() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		

		// para o objeto de pessoas joga a lista pramim
		modelAndView.addObject("pessoas",pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;
	}
	//pega na url o id pessoa
	@GetMapping("**/editarpessoa/{idpessoa}")
	public ModelAndView	editar(@PathVariable("idpessoa") Long idpessoa) {
		
		//vai buscar o id da pessoa carregar o objeto do banco de dados consultou
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		//vai retorna pra mesma tela de cadastro o retorno
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//vai addicionar o objeto pessoa com get que vai buscar seus dados pra edição
		modelAndView.addObject("pessoaobj", pessoa.get());
		
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		
		
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
			@RequestParam("pesqsexo") String pesqsexo, @PageableDefault(size = 5, sort = {"nome"})
	         Pageable pageable) { //paginação tamanho 5 por ordenação de nome e pageable de paginas
		
		Page<Pessoa> pessoas = null; 
		
		//diferente de nulo ou vazio ou seja tem alguma coisa
		if(pesqsexo != null && !pesqsexo.isEmpty() ) {
			
			//pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa.trim().toUpperCase(), pesqsexo);
			pessoas = pessoaRepository.findPessoaBySexoPage(nomepesquisa, pesqsexo, pageable);
		}else {
			//pessoas = pessoaRepository.findPessoaByName(nomepesquisa.trim().toUpperCase());	 
			pessoas = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
		}
		
		
		//vai consultar e retornar para mesma tela de cadastro
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		//passa a lista de pessoas
		modelAndView.addObject("pessoas", pessoas);
		
		//objeto vazio pro formulário trabalhar corretamente
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		//deixar mantido em tela qnd pesqusiar o nome da pesosa
		modelAndView.addObject("nomepesquisa", nomepesquisa);
		
		
		
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
			
            modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			
			//objeto pessoa pai pra mostrar na tela
			modelAndView.addObject("pessoaobj", new Pessoa());
			
			//carregar os objetos novamente menos o que foi removido
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
			
			return modelAndView;
		}
		
		@GetMapping("**/pesquisarpessoa")
		public void imprimiPdf(@RequestParam("nomepesquisa") String nomepesquisa,
				@RequestParam("pesqsexo") String pesqsexo,
				HttpServletRequest request, HttpServletResponse response) throws Exception { 
			
			//lista de pessoas o msm que foi criado com o jasper
			List<Pessoa> pessoas = new ArrayList<Pessoa>();
			
			if(pesqsexo != null && !pesqsexo.isEmpty()
					&& nomepesquisa != null && !nomepesquisa.isEmpty()) { //busca por nome e sexo
				
				pessoas = pessoaRepository.findPessoaByNameSexo(nomepesquisa.trim().toUpperCase(), pesqsexo);
				
			}else if (nomepesquisa != null && !nomepesquisa.isEmpty()){ //busca somente por nome
				
				pessoas = pessoaRepository.findPessoaByName(nomepesquisa.trim().toUpperCase());	
				
			}else if (pesqsexo != null && !pesqsexo.isEmpty()){ //busca somente por sexo
					
					pessoas = pessoaRepository.findPessoaBySexo(pesqsexo);	
				
			}else {  // busca todos
				 Iterable<Pessoa> iterator = pessoaRepository.findAll();
				 
				 for (Pessoa pessoa : iterator) {
					pessoas.add(pessoa); //vai adicionar as pessoa desse iterator pra lista de pessoas
				}
			}
			
			//chamar o  serviço que faz a geração do relatorio
			byte[] pdf = reportUtil.geraRealatorio(pessoas, "pessoa", request.getServletContext());
			
			//tamanho da resposta
			response.setContentLength(pdf.length);
			
			//definir na resposta o tipo de arquivo aquivo pdf arquivo de midia
			response.setContentType("application/octet-stream");
			
			//Definir o cabeçalho da resposta
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
			response.setHeader(headerKey, headerValue);
			
			//finaliza a resposta pro navegador
			response.getOutputStream().write(pdf);
		}
		
		@GetMapping("**/baixarcurriculo/{idpessoa}")
		public void baixarcurriculo(@PathVariable("idpessoa")Long idpessoa,
				HttpServletResponse response) throws IOException {
			
			//Consultar objeto pessoa no banco de dados
			Pessoa pessoa = pessoaRepository.findById(idpessoa).get();//.get retorna id da pessoa
			
			if(pessoa.getCurriculo() != null) {
				
				//setar o tamanho da resposta
				response.setContentLength(pessoa.getCurriculo().length);
				
				//tipo do arquivo para dowload ou pode ser generico "application/octet-stream"
				//no lugar do  pessoa.getTipoFileCurriculo()
				response.setContentType(pessoa.getTipoFileCurriculo());
				
				//Define o cabeçalho dá resposta
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
				response.setHeader(headerKey, headerValue);
				
				//Finaliza a resposta passando o arquivo
				response.getOutputStream().write(pessoa.getCurriculo());
				
			}
			
		}
		@GetMapping("/pessoaspag")                     //vai carregar sempre 5 ent size 5
		public ModelAndView carregaPessoasPorPaginacao(@PageableDefault(size = 5) Pageable pageable,
				ModelAndView modelAndView, @RequestParam("nomepesquisa") String nomepesquisa) { //modelAndView controlador de tela
			
			//vai carregar do banco
			Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable); //vai trazer páginado pag1 pag2 etc
          
			modelAndView.addObject("pessoas", pagePessoa);//variavel pessoa a nossa paginação dos objetos
            
            modelAndView.addObject("pessoaobj", new Pessoa());//formulario carregar vazio evitar erro 
            
            modelAndView.addObject("nomepesquisa", nomepesquisa);
            
            modelAndView.setViewName("cadastro/cadastropessoa"); //retorno da tela
			
			return modelAndView;
			
		}
}
