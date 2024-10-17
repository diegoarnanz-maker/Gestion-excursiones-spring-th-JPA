package excursionweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import excursionweb.modelo.model.Excursion;

import java.util.List;

public interface IExcursionRepository extends JpaRepository<Excursion, Integer> {
    
}
