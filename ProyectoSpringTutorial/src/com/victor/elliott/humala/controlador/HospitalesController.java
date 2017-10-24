package es.mma.perceptores.controller.hospital;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.mma.architecture.components.validation.ajax.AjaxValidation;
import es.mma.perceptores.controller.hospital.validator.HospitalValidator;
import es.mma.perceptores.dto.DetalleHospitalForm;
import es.mma.perceptores.dto.PerceptorId;
import es.mma.perceptores.dto.SearchPerceptorCriteria;
import es.mma.perceptores.exception.CatalogoNoExisteException;
import es.mma.perceptores.exception.PerceptorNoExisteException;
import es.mma.perceptores.exception.PerceptorProhibidoException;
import es.mma.perceptores.exception.SGEException;
import es.mma.perceptores.service.ICatalogoService;
import es.mma.perceptores.service.IGestorService;
import es.mma.perceptores.service.IPerceptorService;
import es.mma.perceptores.service.ISecurityService;

/**
 * Controlador para hospitales
 *
 * @author fdbesteban
 *
 */
@Controller
@RequestMapping("/hospitales")
public class HospitalesController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Key para catalogo de prioridades
	 */
	private static final String CATALOGO_PRIORIDADES = "prioridad";
	/**
	 * Key para catalogo de situaciones
	 */
	private static final String CATALOGO_SITUACIONES = "situacion";
	/**
	 * Key para catalogo de especialidades
	 */
	private static final String CATALOGO_ESPECIALIDADES = "especialidad";
	/**
	 * Key para catalogo de actividades
	 */
	private static final String CATALOGO_ACTIVIDADES = "actividad";
	/**
	 * Key para catalogo de actividades
	 */
	private static final String CATALOGO_RAMOS = "ramo";
	/**
	 * Vista del formulario para hospitales
	 */
	private static final String VIEW_EDIT_FORM = "hospital-edit-form";
	/**
	 * Vista del formulario para hospitales
	 */
	private static final String VIEW_ALTA_FORM = "hospital-alta-form";
	/**
	 * Vista del formulario de busqueda
	 */
	private static final String VIEW_LIST = "hospitales";

	/**
	 * Vista del listado de resultados.
	 */
	private static final String VIEW_RESULTS = "hospitales-list";

	/**
	 * Redirección a la acción de home de hospitales
	 */
	private static final String REDIRECT_ACTION_LIST = "redirect:/action/hospitales";
	/**
	 * Constante que define el tipo de perceptor HOSPITAL
	 */
	private static final String PERCIDEN_HOSPITALES = "H";
	/**
	 * Action nuevo.
	 */
	private static final String ACTION_NEW = "new";
	/**
	 * Action actualización.
	 */
	private static final String ACTION_UPDATE = "update";

	@Autowired
	private IPerceptorService perceptorService;

	@Autowired
	private ICatalogoService catalogoService;

	@Autowired
	@Qualifier("searchPerceptorCriteriaValidator")
	private Validator searchPerceptorCriteriaValidator;

	@Autowired
	private ISecurityService securityService;

	@Autowired
	private IGestorService gestorService;

	/**
	 * Binder para el formulario de alta de un hospital
	 *
	 * @param binder web data binder.
	 */
	@InitBinder({ "hospital" })
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new HospitalValidator());
	}

	/**
	 * Mostrará la home
	 *
	 * @param model Modelo
	 * @return departamentos
	 */
	@RequestMapping
	public String listaPerceptoresForm(ModelMap model) {
		SearchPerceptorCriteria searchForm = new SearchPerceptorCriteria();

		if (!securityService.isAdministrador()) {
			try {

				searchForm.setGestor(gestorService.obtenerGestor(securityService.getUserName()));
			} catch (SGEException ex) {
				logger.error(ex.getError());

			}
		}

		model.put("search", searchForm);
		try {
			model.put("prioridades", catalogoService.obtenerCodigosCatalogo(CATALOGO_PRIORIDADES));
			model.put("situaciones", catalogoService.obtenerCodigosCatalogo(CATALOGO_SITUACIONES));
		} catch (CatalogoNoExisteException e) {
			logger.error("Catalogo no existe");
		}
		return VIEW_LIST;
	}

	/**
	 * Obtiene los perceptores que cumplen unos criterios de búsqueda.
	 *
	 * @param criterias criterios de búsqueda.
	 * @param errors listado de errores.
	 * @param response respuesta http.
	 * @return json con el listado de perceptores.
	 */
	@RequestMapping(value = "/search/validation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Object searchAjax(@RequestBody SearchPerceptorCriteria criterias, BindingResult errors, HttpServletResponse response) {
		searchPerceptorCriteriaValidator.validate(criterias, errors);

		if (errors.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		return errors.getAllErrors();

	}

	/**
	 * Permitirá realizar la búsqueda de perceptores ajax.
	 *
	 * @param criterias a filtrar
	 * @param errors errores
	 * @param response http response
	 * @param model mapa modelo.
	 * @return Listado de perceptores ó errores
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@AjaxValidation(formName = "searchForm", validator = SearchPerceptorCriteria.class)
	public String searchAjax(@RequestBody SearchPerceptorCriteria criterias, BindingResult errors, HttpServletResponse response, ModelMap model) {
		searchPerceptorCriteriaValidator.validate(criterias, errors);
		if (errors.hasErrors()) {
			return VIEW_LIST;

		} else {
			// Hardcodeamos el valor para hospitales
			criterias.getId().setIdenPerceptor(PERCIDEN_HOSPITALES);
			model.put("perceptores", perceptorService.obtenerPerceptores(criterias));
			return VIEW_RESULTS;
		}
	}

	/**
	 * Formulario
	 *
	 * @param model Modelo.
	 * @return formulario
	 */
	@RequestMapping(value = "/new")
	public String hospitalForm(ModelMap model) {
		DetalleHospitalForm detalle = new DetalleHospitalForm();
		detalle.setId(new PerceptorId("H", null));
		detalle.setAction(ACTION_NEW);
		model.put("hospital", detalle);
		loadCodigos(model);
		return VIEW_ALTA_FORM;
	}

	/**
	 * Mostrará el formulario preparado para crear un nuevo hospital
	 *
	 * @param hospital perceptor a guardar.
	 * @param errors errores producidos.
	 * @param model mapa modelo.
	 * @param flashAttr atributos redirección.
	 * @return vista
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String perceptorSave(@Validated @ModelAttribute("hospital") DetalleHospitalForm hospital, BindingResult errors, ModelMap model, RedirectAttributes flashAttr) {
		if (errors.hasErrors()) {
			return VIEW_ALTA_FORM;
		} else {
			perceptorService.crearPerceptor(hospital);
			flashAttr.addFlashAttribute("mensaje", "El Perceptor ha sido creado correctamente");

			return REDIRECT_ACTION_LIST;
		}
	}

	/**
	 * Obtendrá los detalles de un perceptor y retornará a la vista del detalle
	 *
	 * @param idenPerceptor identificador del perceptor.
	 * @param codPerceptor código del peceptor.
	 * @param model modelo mapa.
	 * @return formulario
	 * @throws PerceptorNoExisteException perceptor no encontrado.
	 * @throws PerceptorProhibidoException perceptor no permitido.
	 */
	@RequestMapping(value = "/{idenPerceptor}/{codPerceptor}/edit")
	public String perceptorEditForm(@PathVariable String idenPerceptor, @PathVariable Integer codPerceptor, ModelMap model)
			throws PerceptorNoExisteException, PerceptorProhibidoException {
		DetalleHospitalForm hospital = new DetalleHospitalForm();
		hospital.copy(perceptorService.obtenerPerceptor(new PerceptorId(idenPerceptor, codPerceptor)));
		hospital.setAction(ACTION_UPDATE);
		model.put("hospital", hospital);
		loadCodigos(model);
		return VIEW_EDIT_FORM;
	}

	/**
	 * Metodo para actualizar el perceptor.
	 *
	 * @param errors listado de errores.
	 * @param model modelo.
	 * @param flashAttr atributos de redirección.
	 * @param hospital hospital con los datos a actualizar.
	 * @param response respuesta http.
	 * @throws PerceptorProhibidoException excepción perceptor no permitido.
	 * @throws PerceptorNoExisteException no se ha encontrado perceptor a editar.
	 * @return redirección vista.
	 */
	@AjaxValidation(formName = "hospitalForm", validator = HospitalValidator.class)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String perceptorUpdate(@Validated @ModelAttribute("hospital") DetalleHospitalForm hospital, BindingResult errors, ModelMap model, HttpServletResponse response,
			RedirectAttributes flashAttr) throws PerceptorNoExisteException, PerceptorProhibidoException {
		if (errors.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return VIEW_EDIT_FORM;
		} else {
			perceptorService.actualizarPerceptor(hospital);
			flashAttr.addFlashAttribute("mensaje", "El hospital ha sido actualizado correctamente");
			return REDIRECT_ACTION_LIST;
		}
	}

	/**
	 * Eliminará un perceptor.
	 *
	 * @param idenPerceptor identificador
	 * @param codPerceptor codigo
	 * @param flashAttr atributo redirección
	 * @return vista perceptores
	 * @throws PerceptorNoExisteException no existe
	 * @throws PerceptorProhibidoException no tiene permisos
	 */
	@RequestMapping(value = "/{idenPerceptor}/{codPerceptor}/baja", method = RequestMethod.GET)
	public String bajaPerceptor(@PathVariable String idenPerceptor, @PathVariable Integer codPerceptor, RedirectAttributes flashAttr)
			throws PerceptorNoExisteException, PerceptorProhibidoException {
		perceptorService.bajaPerceptor(new PerceptorId(idenPerceptor, codPerceptor));
		flashAttr.addFlashAttribute("mensaje", "El hospital ha sido dado de baja correctamente");
		return "redirect:/action/hospitales/" + idenPerceptor + "/" + String.valueOf(codPerceptor) + "/edit";
	}

	/**
	 * Eliminará un perceptor.
	 *
	 * @param idenPerceptor identificador.
	 * @param codPerceptor codigo.
	 * @param flashAttr atributo redirección.
	 * @return vista perceptores.
	 * @throws PerceptorNoExisteException no existe perceptor.
	 * @throws PerceptorProhibidoException no tiene permisos.
	 */
	@RequestMapping(value = "/{idenPerceptor}/{codPerceptor}/embargo", method = RequestMethod.GET)
	public String embargoPerceptor(@PathVariable String idenPerceptor, @PathVariable Integer codPerceptor, RedirectAttributes flashAttr)
			throws PerceptorNoExisteException, PerceptorProhibidoException {
		perceptorService.embargoPerceptor(new PerceptorId(idenPerceptor, codPerceptor));
		flashAttr.addFlashAttribute("mensaje", "El hospital ha sido dado de baja por embargo correctamente");
		return "redirect:/action/hospitales/" + idenPerceptor + "/" + String.valueOf(codPerceptor) + "/edit";
	}

	/**
	 * Eliminará un perceptor.
	 *
	 * @param idenPerceptor identificador
	 * @param codPerceptor codigo
	 * @param flashAttr atributo redirección
	 * @return vista perceptores
	 * @throws PerceptorNoExisteException no existe
	 * @throws PerceptorProhibidoException no tiene permisos
	 */
	@RequestMapping(value = "/{idenPerceptor}/{codPerceptor}/alta", method = RequestMethod.GET)
	public String altaPerceptor(@PathVariable String idenPerceptor, @PathVariable Integer codPerceptor, RedirectAttributes flashAttr)
			throws PerceptorNoExisteException, PerceptorProhibidoException {
		perceptorService.altaPerceptor(new PerceptorId(idenPerceptor, codPerceptor));
		flashAttr.addFlashAttribute("mensaje", "El hospital ha sido dado de alta correctamente");
		return "redirect:/action/hospitales/" + idenPerceptor + "/" + String.valueOf(codPerceptor) + "/edit";
	}

	/**
	 * Cargará en el modelo los codigos de referencia.
	 *
	 * @param model model map
	 */
	private void loadCodigos(ModelMap model) {
		try {
			model.put("prioridades", catalogoService.obtenerCodigosCatalogo(CATALOGO_PRIORIDADES));
			model.put("especialidades", catalogoService.obtenerCodigosCatalogo(CATALOGO_ESPECIALIDADES));
			model.put("actividades", catalogoService.obtenerCodigosCatalogo(CATALOGO_ACTIVIDADES));
			model.put("ramos", catalogoService.obtenerCodigosCatalogo(CATALOGO_RAMOS));
		} catch (CatalogoNoExisteException e) {
			logger.error("No puedo encontrar el catalogo de valores " + e.getMessage());
		}
	}

}
