package br.com.imageliteapi.emailTemplates;

import br.com.imageliteapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Component
public class MailToUsuario {

	@Value("${app.allowed-origins}")
	private String clienteUrl;
	
	@Autowired
	private TemplateEngine templateEngine;

	public String ativarUsuario(User usuario) {
		
		Context context = new Context();
		context.setVariable("url", clienteUrl + "new-password?q=" + usuario.getCodToken());
		context.setVariable("nome", usuario.getName().split(" ")[0]);
		return templateEngine.process("ativar-usuario", context);
	}
	
	public String recuperarSenha(User usuario) {

		Context context = new Context();
		context.setVariable("url", clienteUrl + "recover-password?q=" + usuario.getCodToken());
		context.setVariable("nome", usuario.getName().split(" ")[0]);
		return templateEngine.process("recuperar-senha", context);
	}

}
