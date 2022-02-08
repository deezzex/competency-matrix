package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.ItemDto;
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
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Label must be filled")
    @Size(min = 5, max = 50, message = "Size of Label must be between 5 and 50")
    private String label;

    @ManyToOne
    private Topic topic;

    @OneToMany
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private List<Resource> resources = new ArrayList<>();

    public Item(Long id, String label, List<Resource> resources) {
        this.id = id;
        this.label = label;
        this.resources = resources;
    }

    public static Item from(ItemDto itemDto) {
        Item item = new Item();

        item.setLabel(itemDto.getLabel());

        return item;
    }

    public void addResource(Resource resource){
        resources.add(resource);
    }

    public void removeResource(Resource resource){
        resources.remove(resource);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
