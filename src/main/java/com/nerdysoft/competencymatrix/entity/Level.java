package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.LevelDto;
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
public class Level {

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
    @JoinColumn(name = "level_id")
    @ToString.Exclude
    private List<Topic> topics = new ArrayList<>();

    public static Level from(LevelDto levelDto) {
        Level level = new Level();

        level.setName(levelDto.getName());
        level.setDescription(levelDto.getDescription());

        return level;
    }

    public void addTopic(Topic topic){
        topics.add(topic);
    }

    public void removeTopic(Topic topic){
        topics.remove(topic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Level level = (Level) o;
        return id != null && Objects.equals(id, level.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
