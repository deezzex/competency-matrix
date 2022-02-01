package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.MatrixDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Matrix {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name must be filled")
    @Size(min = 3, max = 20, message = "Size of Name must be between 3 and 20")
    private String name;

    @NotEmpty(message = "Description must be filled")
    @Size(min = 5, max = 50, message = "Size of Description must be between 5 and 50")
    private String description;

    @OneToMany
    @JoinColumn(name = "matrix_id")
    @ToString.Exclude
    private List<Competency> competencies = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "matrix_id")
    @ToString.Exclude
    private List<Level> levels = new ArrayList<>();

    public static Matrix from(MatrixDto matrixDto) {
        Matrix matrix = new Matrix();

        matrix.setName(matrixDto.getName());
        matrix.setDescription(matrixDto.getDescription());

        return matrix;
    }

    public void addCompetency(Competency competency){competencies.add(competency);}

    public void removeCompetency(Competency competency){competencies.add(competency);}

    public void addLevel(Level level){
        levels.add(level);
    }

    public void removeLevel(Level level){
        levels.remove(level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Matrix matrix = (Matrix) o;
        return id != null && Objects.equals(id, matrix.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
