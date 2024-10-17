package excursionweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import excursionweb.modelo.dao.IExcursionDao;
import excursionweb.modelo.model.Excursion;

import java.util.Date;
import java.util.NoSuchElementException;
import java.text.SimpleDateFormat;

@Controller
public class HomeController {

    @Autowired
    private IExcursionDao excursionDao;

    @GetMapping({ "/", "/home" })
    public String homePage() {
        return "home";
    }

    // FindAll
    @GetMapping({ "/excursiones" })
    public String home(Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        return "getAll";
    }

    // FindById
    @GetMapping("/excursion")
    public String getExcursionById(@RequestParam("id") int id, Model model) {
        Excursion excursion = excursionDao.findById(id);
        if (excursion != null) {
            model.addAttribute("excursion", excursion);
            return "getById";
        } else {
            // si no encuentra redirijo a error
            model.addAttribute("error", "Excursión no encontrada");
            return "error";
        }
    }

    // findFeatures
    @GetMapping("/destacadas")
    public String getDestacados(Model model) {
        model.addAttribute("excursionDestacada", excursionDao.findFeatures());
        return "getFeatures";
    }

    // findActived
    @GetMapping("/activas")
    public String getActivas(Model model) {
        model.addAttribute("excursionActiva", excursionDao.findActived());
        return "getActived";
    }

    // findCancelled
    @GetMapping("/canceladas")
    public String getCanceladas(Model model) {
        model.addAttribute("excursionCancelada", excursionDao.findCancelled());
        return "getCancelled";
    }

    // findfinished
    @GetMapping("/finalizadas")
    public String getFinalizadas(Model model) {
        model.addAttribute("excursionFinalizada", excursionDao.findFinished());
        return "getFinished";
    }

    // delete(idExcursion)
    @GetMapping("/delete")
    public String deleteExcursionId(@RequestParam("id") int id, Model model) {
        Excursion excursion = excursionDao.findById(id);
        if (excursion != null) {
            excursionDao.delete(id);
            model.addAttribute("eliminada", "Excursion eliminada");
        } else {
            model.addAttribute("error", "Excursion no encontrada");
        }
        // redirijo a getAll
        model.addAttribute("excursiones", excursionDao.findAll());
        return "getAll";
    }

    @GetMapping("/detalles/{id}")
    public String showDetails(@PathVariable("id") int id, Model model) {
        Excursion excursion = excursionDao.findById(id);
        model.addAttribute("excursion", excursion);
        return "getById";
    }

    @GetMapping("/eliminar/{id}")
    public String deleteExcursion(@PathVariable("id") int id, Model model) {
        Excursion excursion = excursionDao.findById(id);
        if (excursion != null) {
            excursionDao.delete(id);
            model.addAttribute("eliminada", "Excursión eliminada");
        }
        // redirijo a lista
        model.addAttribute("excursiones", excursionDao.findAll());
        return "getAll";
    }

    @GetMapping("/postExcursion")
    public String mostrarFormulario(Model model) {
        model.addAttribute("nuevaExcursion", new Excursion());
        return "formExcursion";
    }

    @PostMapping("/postExcursion")
    public String addExcursion(@ModelAttribute Excursion nuevaExcursion,
            BindingResult result, Model model) {
        nuevaExcursion.setFechaAlta(new Date());
        excursionDao.create(nuevaExcursion);

        model.addAttribute("successMessage", "Excursión añadida exitosamente");

        return "redirect:/excursiones";
    }

    // Para mostrar el formulario de update excursion
    @GetMapping("/editar/{id}")
    public String editarExcursion(@PathVariable("id") int id, Model model) {
        Excursion excursion = excursionDao.findById(id);
        model.addAttribute("excursion", excursion);
        return "updateForm";
    }

    // Utilizado para convertir la fecha de String a Date en el campo fechaAlta
    // (Tuve que investigar en GTP, no daba con ello)
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    // Publicamos la actualización de la excursión
    @PostMapping("/editar")
    public String actualizarExcursion(@ModelAttribute("excursion") Excursion excursion) {
        excursionDao.update(excursion);
        return "redirect:/excursiones";
    }

    // Vamos a controlar los errores para manejarlos bien si el objeto es null al no
    // encontrarse el id. De esta forma puedo personalizar la paginna de error y que
    // no casque del todo
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(NoSuchElementException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", "No se ha encontrado la excursión solicitada.");
        return mav;
    }

    // Controllers para las mejoras propuestas en la actividad
    @GetMapping("/buscarPorPrecioMayor")
    public String buscarPorPrecioMayor(@RequestParam("precio") double precio, Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        // En angular podriamos hacer una validacion con reactive forms... pero lo mando a html de error si introduce un precio negativo
        if(precio < 0) {
            model.addAttribute("error", "El precio no puede ser negativo");
            return "error";
        }
        
        model.addAttribute("excursionesPrecioMayor", excursionDao.findByPrecioUnitarioGreaterThan(precio));
        //Se lo meto al modelo para mostrarlo en el html, una pijada pero bueno
        model.addAttribute("precioIngresado", precio);
        return "getByPrecioMayor";
    }

    @GetMapping("/buscarPorPrecioMenor")
    public String buscarPorPrecioMenor(@RequestParam("precio") double precio, Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        // En angular podriamos hacer una validacion con reactive forms... pero lo mando a html de error si introduce un precio negativo
        if(precio < 0) {
            model.addAttribute("error", "El precio no puede ser negativo");
            return "error";
        }
        
        model.addAttribute("excursionesPrecioMenor", excursionDao.findByPrecioUnitarioLessThan(precio));
        //Se lo meto al modelo para mostrarlo en el html, una pijada pero bueno
        model.addAttribute("precioIngresado", precio);
        return "getByPrecioMenor";
    }
    
    @GetMapping("/buscarPorPrecioIntervalo")
    public String buscarPorPrecioIntervalo(@RequestParam("minPrecio") double minPrecio,@RequestParam("maxPrecio") double maxPrecio , Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        // En angular podriamos hacer una validacion con reactive forms... pero lo mando a html de error si introduce un precio negativo
        if(minPrecio < 0 || maxPrecio < 0) {
            model.addAttribute("error", "El precio no puede ser negativo");
            return "error";
        }
        
        model.addAttribute("buscarPorPrecioIntervalo", excursionDao.findByPrecioUnitarioBetween(minPrecio, maxPrecio));

        //Se lo meto al modelo para mostrarlo en el html, una pijada pero bueno
        model.addAttribute("precioIngresadoMin", minPrecio);
        model.addAttribute("precioIngresadoMax", maxPrecio);

        return "getByPrecioIntervalo";
    }

    @GetMapping("/buscarOrigen")
    public String buscarPorOrigen(@RequestParam("origen") String origen, Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        model.addAttribute("excursionesOrigen", excursionDao.findByOrigen(origen));
        model.addAttribute("origenIngresado", origen);
        return "getByOrigen";
    }

    @GetMapping("/buscarDestino")
    public String buscarPorDestino(@RequestParam("destino") String destino, Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        model.addAttribute("excursionesDestino", excursionDao.findByDestino(destino));
        model.addAttribute("destinoIngresado", destino);
        return "getByDestino";
    }

    @GetMapping("/buscarOrigenDestino")
    public String buscarPorOrigenDestino(@RequestParam("origen") String origen, @RequestParam("destino") String destino, Model model) {
        model.addAttribute("excursiones", excursionDao.findAll());
        model.addAttribute("excursionesOrigenDestino", excursionDao.findByOrigenAndDestino(origen, destino));
        model.addAttribute("origenIngresado", origen);
        model.addAttribute("destinoIngresado", destino);
        return "getByOrigenDestino";
    }

}
