package curso.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "login";
	}

	@RequestMapping("/index")
	public String login() {
		return "index";
	}

}
