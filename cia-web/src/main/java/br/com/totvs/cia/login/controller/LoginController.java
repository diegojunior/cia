package br.com.totvs.cia.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login.jsp";
	}
	
	@RequestMapping(value = "/home")
	public String home(final ModelMap model) {
		model.addAttribute("user", this.getPrincipal());
		return "home.html";
	}
	
	@RequestMapping(value = "/expired")
	public String expired() {
		return "expired.html";
	}
	
	@RequestMapping(value = "/invalidSession")
	public String invalidSession() {
		return "invalidSession.html";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage(final HttpServletRequest request, final HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
			securityContextLogoutHandler.setInvalidateHttpSession(false);
			securityContextLogoutHandler.logout(request, response, auth);
        }
        return "redirect:login?logout";
    }
 
    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
	
}
