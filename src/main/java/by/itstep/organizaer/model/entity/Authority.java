package by.itstep.organizaer.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Data
@Builder
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Roles authority;

    @ManyToOne
    @JoinColumn(name = "org_user")
    private User orgUser;

    @Override
    @Transient
    public String getAuthority() {
        return authority.name();
    }
}
