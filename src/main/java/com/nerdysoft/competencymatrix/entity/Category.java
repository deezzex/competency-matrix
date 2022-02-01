package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.CategoryDto;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.validation.annotation.PriorityAllow;
import com.nerdysoft.competencymatrix.validation.annotation.TypeAllow;
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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name must be filled")
    @Size(min = 3, max = 20, message = "Size of name must be between 3 and 20")
    private String name;

    @NotEmpty(message = "Description must be filled")
    @Size(min = 5, max = 50, message = "Size of Description must be between 5 and 50")
    private String description;

    @Enumerated(EnumType.STRING)
    @TypeAllow
    private Type type;

    @Enumerated(EnumType.STRING)
    @PriorityAllow
    private Priority priority;

    @OneToMany
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private List<Topic> topics = new ArrayList<>();

    public static Category from(CategoryDto categoryDto) {
        Category category = new Category();

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setType(categoryDto.getType());
        category.setPriority(categoryDto.getPriority());

        return category;
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
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
