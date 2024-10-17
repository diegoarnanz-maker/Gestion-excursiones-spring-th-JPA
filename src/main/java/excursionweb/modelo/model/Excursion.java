package excursionweb.modelo.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todo
@Getter 
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "excursiones")
public class Excursion {

    @EqualsAndHashCode.Include //para que solo tenga en cuenta el idExcursion
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_excursion")
    private int idExcursion;

    private String descripcion;
    private String origen;
    private String destino;

    @Column(name = "fecha_excursion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaExcursion;
    private int duracion;
    private String estado;
    private String destacado;

    @Column(name = "aforo_maximo")
    private int aforoMaximo;

    @Column(name = "minimo_asistentes")
    private int minimoAsistentes;

    @Column(name = "precio_unitario")
    private double precioUnitario;
    private String imagen;

    @Column(name = "fecha_alta")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
}

