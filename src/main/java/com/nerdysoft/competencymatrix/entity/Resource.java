package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.ResourceDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name must be filled")
    @Size(min = 3, max = 20, message = "Size of Name must be between 3 and 20")
    private String name;

    @NotEmpty(message = "Description must be filled")
    @Size(min = 5, max = 50, message = "Size of Description must be between 5 and 50")
    private String description;

    @NotEmpty(message = "URL must be filled")
    private String url;

    public static Resource from(ResourceDto resourceDto) {
        Resource resource = new Resource();

        resource.setName(resourceDto.getName());
        resource.setDescription(resourceDto.getDescription());
        resource.setUrl(resourceDto.getUrl());

        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Resource resource = (Resource) o;
        return id != null && Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
