package com.victor.elliott.humala.controlador;

import com.victor.elliott.humala.formulario.UsuarioForm;
import com.victor.elliott.humala.formulario.ComponenteForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsuarioController {
	
	//Con esto indicamos que nuestro Usuario y clave seran Usuario y Clave respectivamente
    private static UsuarioForm usuarioForm = new UsuarioForm("Victor","Elliott");
    //private static ComponenteForm componenteForm = new ComponenteForm();
    
    @RequestMapping(value = "/inicializarLogin.html", method = RequestMethod.GET)
    public ModelAndView inicializarUsuario() {
        
    	System.out.println("Si hemos llegado aqui quiere decir que la pagina index.jsp ha invocado a este controlador por el request /inicializarLogin.html y requiere el inicializarUsuario View");
    	
    	return new ModelAndView("login" , "usuarioForm", new UsuarioForm());
    }
    
    @RequestMapping(value = "/verificarLogin.html", method = RequestMethod.POST)
    public ModelAndView verificarUsuario(@ModelAttribute("usuarioForm") UsuarioForm usuarioForm) {
        
    	System.out.println("Si hemos llegado aqui quiere decir que la pagina login.jsp ha invocado a este controlador por el request /verificarLogin.html y requiere el verificarUsuario View");
    	
    	if("".equals(usuarioForm.getNombre())&&"".equals(usuarioForm.getClave())){
    		System.out.println("Cargaremos por primera vez la pagina de login con el mensaje vacio");
    		return new ModelAndView("login" , "mensaje", "Debe de llenar los campos de Usuario y Clave");
    	}
    	else if(usuarioForm.getNombre().equals(UsuarioController.usuarioForm.getNombre())&&usuarioForm.getClave().equals(UsuarioController.usuarioForm.getClave())){
    		System.out.println("Se coloco al usuario y clave correctamente y va a la pagina de agregarComponentes");
    		ModelAndView modelo= new ModelAndView("agregarComponentes" , "mensaje", "Usuario Correcto");
    		modelo.addObject("usuarioForm", usuarioForm);
    		modelo.addObject("componenteForm", new ComponenteForm());
    		return modelo;
    	}
    	else{
    		System.out.println("Se coloco al usuario y clave incorrectamente y regresamos a la pagina de login con el mensaje de Usuario Incorrecto");
    		return new ModelAndView("login" , "mensaje", "Usuario Incorrecto");
    	}
    }

}