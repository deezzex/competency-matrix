package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.TopicDto;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.validation.annotation.PriorityAllow;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name must be filled")
    @Size(min = 3, max = 20, message = "Size of Name must be between 3 and 20")
    private String name;

    @NotEmpty(message = "Description must be filled")
    @Size(min = 5, max = 50, message = "Size of Description must be between 5 and 50")
    private String description;

    @NotNull(message = "required must be filled")
    private boolean required;

    @Enumerated(EnumType.STRING)
    @PriorityAllow
    private Priority priority;

    @ManyToOne
    private Category category;

    @OneToMany
    @JoinColumn(name = "topic_id")
    @ToString.Exclude
    private List<Item> items = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "topic_id")
    @ToString.Exclude
    private List<Resource> resources = new ArrayList<>();

    public static Topic from(TopicDto topicDto) {
        Topic topic = new Topic();

        topic.setName(topicDto.getName());
        topic.setDescription(topicDto.getDescription());
        topic.setRequired(topicDto.isRequired());
        topic.setPriority(topicDto.getPriority());

        return topic;
    }

    public void addResource(Resource resource){
        resources.add(resource);
    }

    public void removeResource(Resource resource){
        resources.remove(resource);
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Topic topic = (Topic) o;
        return id != null && Objects.equals(id, topic.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
