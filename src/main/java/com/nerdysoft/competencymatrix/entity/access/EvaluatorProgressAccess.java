package com.nerdysoft.competencymatrix.entity.access;

import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.dto.EvaluatorProgressAccessDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "access_ep")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EvaluatorProgressAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private User evaluator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private TopicProgress progress;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EvaluatorProgressAccess that = (EvaluatorProgressAccess) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
