package com.victor.elliott.humala.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.victor.elliott.humala.formulario.ComponenteForm;
import com.victor.elliott.humala.formulario.UsuarioForm;
import com.victor.elliott.humala.servicios.ComponenteService;
import com.victor.elliott.humala.servicios.ComponenteServiceImpl;

@Controller
public class ComponenteController {

	private ComponenteService componenteService= new ComponenteServiceImpl();
	
	@RequestMapping(value="/agregarComponentes.html", method = RequestMethod.POST)
	public ModelAndView guardarComponente(@ModelAttribute("componenteForm") ComponenteForm componente) {
		System.out.println("Ingreso a componente guardado");
		componenteService.addComponente(componente);
		System.out.println("Componente guardado");
		return new ModelAndView("mostrarComponentes" , "listaComponentes", componenteService.getComponente());
	}
	
	@RequestMapping(value="/volverComponente.html", method = RequestMethod.GET)
	public ModelAndView volverComponente() {
		System.out.println("Volvemos a agregar componente");
		System.out.println("Se coloco al usuario y clave correctamente y va a la pagina de agregarComponentes");
		ModelAndView modelo= new ModelAndView("agregarComponentes" , "mensaje", "Usuario Correcto");
		modelo.addObject("usuarioForm", new UsuarioForm("Victor","Elliott"));
		modelo.addObject("componenteForm", new ComponenteForm());
		return modelo;
	}
	
}
