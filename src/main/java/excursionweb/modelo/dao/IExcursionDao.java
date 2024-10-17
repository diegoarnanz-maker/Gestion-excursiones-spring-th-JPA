package excursionweb.modelo.dao;

import java.util.List;

import excursionweb.modelo.model.Excursion;

public interface IExcursionDao {

    int create(Excursion excursion);
    int update(Excursion excursion);
    int delete(Excursion excursion);
    int delete(int idExcursion);
    Excursion findById(int idExcursion);
    List<Excursion> findAll();
    List<Excursion> findFeatures();
    List<Excursion> findActived();
    List<Excursion> findCancelled();
    List<Excursion> findFinished();

    List<Excursion> findByPrecioUnitarioGreaterThan(Double precio);
    List<Excursion> findByPrecioUnitarioLessThan(Double precio);
    List<Excursion> findByPrecioUnitarioBetween(Double minPrecio, Double maxPrecio);

    List<Excursion> findByOrigen(String origen);
    List<Excursion> findByDestino(String destino);
    List<Excursion> findByOrigenAndDestino(String origen, String destino);
  
}
