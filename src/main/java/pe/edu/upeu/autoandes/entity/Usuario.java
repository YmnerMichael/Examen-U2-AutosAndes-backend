package pe.edu.upeu.autoandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity @Table(name="usuarios") @Getter @Setter
public class Usuario {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
@Column(nullable=false,unique=true,length=30) private String username;
@Column(length=100) private String password;
@Column(name="nombrecompleto",nullable=false,length=120) private String nombreCompleto;
@Convert(converter=org.hibernate.type.NumericBooleanConverter.class)
@Column(nullable=false) private Boolean activo;
@ManyToMany(fetch=FetchType.EAGER) @JoinTable(name="usuario_roles",joinColumns=@JoinColumn(name="usuario_id"),inverseJoinColumns=@JoinColumn(name="rol_id")) private java.util.Set<Rol> roles = new java.util.HashSet<>();
}
