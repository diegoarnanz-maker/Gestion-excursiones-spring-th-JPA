package excursionweb.modelo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import excursionweb.modelo.model.Excursion;
import excursionweb.repository.IExcursionRepository;

@Service
public class ExcursionDaoImpl implements IExcursionDao {

    @Autowired
    private IExcursionRepository excursionRepository;

    @Override
    public int create(Excursion excursion) {
        excursionRepository.save(excursion);
        return 1;
    }

    @Override
    public int update(Excursion excursion) {
        Excursion excursionModificar = findById(excursion.getIdExcursion());
        if (excursionModificar != null) {
            excursionModificar.setDescripcion(excursion.getDescripcion());
            excursionModificar.setOrigen(excursion.getOrigen());
            excursionModificar.setDestino(excursion.getDestino());
            excursionModificar.setFechaExcursion(excursion.getFechaExcursion());
            excursionModificar.setDuracion(excursion.getDuracion());
            excursionModificar.setEstado(excursion.getEstado());
            excursionModificar.setDestacado(excursion.getDestacado());
            excursionModificar.setAforoMaximo(excursion.getAforoMaximo());
            excursionModificar.setMinimoAsistentes(excursion.getMinimoAsistentes());
            excursionModificar.setPrecioUnitario(excursion.getPrecioUnitario());
            excursionModificar.setImagen(excursion.getImagen());
            excursionModificar.setFechaAlta(excursion.getFechaAlta());
            excursionRepository.save(excursionModificar);
            return 1;
        } else {
            throw new NoSuchElementException(
                    "Excursion con id " + excursion.getIdExcursion() + " no se ha podido modificar.");
        }
    }

    @Override
    public int delete(Excursion excursion) {
        excursionRepository.delete(excursion);
        return 1;
    }

    @Override
    public int delete(int idExcursion) {
        Excursion excursionEliminar = findById(idExcursion);
        if (excursionEliminar != null) {
            excursionRepository.delete(excursionEliminar);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Excursion findById(int idExcursion) {
        return excursionRepository.findById(idExcursion).orElse(null);
    }

    @Override
    public List<Excursion> findAll() {
        List<Excursion> excursiones = new ArrayList<>();
        excursionRepository.findAll().forEach(excursiones::add);
        return excursiones; //o un cast a List<Excursion>, pero prefiero crear una lista nueva y devolverla del mismo tipo que el método
    }

    @Override
    public List<Excursion> findFeatures() {
        List<Excursion> excursionesDestacadas = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()) {
            if (excursion.getDestacado().equals("S")) {
                excursionesDestacadas.add(excursion);
            }
        }
        return excursionesDestacadas;
    }

    @Override
    public List<Excursion> findActived() {
        List<Excursion> excursionesActivas = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()) {
            if (excursion.getEstado().equals("CREADO")) {
                excursionesActivas.add(excursion);
            }
        }
        return excursionesActivas;
    }

    @Override
    public List<Excursion> findCancelled() {
        List<Excursion> excursionesCanceladas = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()) {
            if (excursion.getEstado().equals("CANCELADO")) {
                excursionesCanceladas.add(excursion);
            }
        }
        return excursionesCanceladas;
    }

    @Override
    public List<Excursion> findFinished() {
        List<Excursion> excursionesFinalizadas = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()) {
            if (excursion.getEstado().equals("TERMINADO")) {
                excursionesFinalizadas.add(excursion);
            }
        }
        return excursionesFinalizadas;
    }

    @Override
    public List<Excursion> findByPrecioUnitarioGreaterThan(Double precio) {
        List<Excursion> excursionesPrecioMayor = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()) {
            if (excursion.getPrecioUnitario() > precio) {
                excursionesPrecioMayor.add(excursion);
            }
        }
        return excursionesPrecioMayor;
    }

    @Override
    public List<Excursion> findByPrecioUnitarioLessThan(Double precio) {
        List<Excursion> excursionesPrecioMenor = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()) {
            if (excursion.getPrecioUnitario() < precio) {
                excursionesPrecioMenor.add(excursion);
            }
        }
        return excursionesPrecioMenor;
    }

    @Override
    public List<Excursion> findByPrecioUnitarioBetween(Double minPrecio, Double maxPrecio) {
        List<Excursion> excursionesPrecioEntre = new ArrayList<Excursion>();
        for (Excursion excursion : excursionRepository.findAll()){
            if (excursion.getPrecioUnitario() >= minPrecio && excursion.getPrecioUnitario() <= maxPrecio){
                excursionesPrecioEntre.add(excursion);
            }
        }
        return excursionesPrecioEntre;
    }

    @Override
    public List<Excursion> findByOrigen(String origen) {
        List<Excursion> excursionesOrigen = new ArrayList<Excursion>();
        for(Excursion excursion : excursionRepository.findAll()) {
            //uso equalsIgnoreCase para que no sea case sesitive y facilite la búsqueda al usuario
            if(excursion.getOrigen().equalsIgnoreCase(origen)) {
                excursionesOrigen.add(excursion);
            }
        }
        return excursionesOrigen;
    }

    @Override
    public List<Excursion> findByDestino(String destino) {
        List<Excursion> excursionesDestino = new ArrayList<Excursion>();
        for(Excursion excursion : excursionRepository.findAll()) {
            if(excursion.getDestino().equalsIgnoreCase(destino)) {
                excursionesDestino.add(excursion);
            }
        }
        return excursionesDestino;
    }

    @Override
    public List<Excursion> findByOrigenAndDestino(String origen, String destino) {
        List<Excursion> excursionesOrigenDestino = new ArrayList<Excursion>();
        for(Excursion excursion : excursionRepository.findAll()) {
            if(excursion.getOrigen().equalsIgnoreCase(origen) && excursion.getDestino().equals(destino)) {
                excursionesOrigenDestino.add(excursion);
            }
        }
        return excursionesOrigenDestino;
    }

}
