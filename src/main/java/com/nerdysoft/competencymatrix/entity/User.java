package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.UserDto;
import com.nerdysoft.competencymatrix.entity.privilege.Role;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "first name must be filled")
    @Size(min = 3, max = 25, message = "Size of first name must be between 3 and 25")
    private String firstName;

    @NotEmpty(message = "last name must be filled")
    @Size(min = 3, max = 25, message = "Size of last name must be between 3 and 25")
    private String lastName;

    @NotEmpty(message = "username must be filled")
    @Size(min = 3, max = 25, message = "Size of username must be between 3 and 25")
    private String username;

    @NotEmpty(message = "password must be filled")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Matrix> matrices = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<TopicProgress> topicProgressList = new ArrayList<>();

    public User(Long id, String firstName, String lastName, List<Matrix> matrices, List<TopicProgress> topicProgressList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matrices = matrices;
        this.topicProgressList = topicProgressList;
    }

    public static User from(UserDto userDto) {
        User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());

        return user;
    }

    public void addMatrix(Matrix matrix){matrices.add(matrix);}

    public void removeMatrix(Matrix matrix){matrices.add(matrix);}

    public void addProgress(TopicProgress progress){
        topicProgressList.add(progress);
    }

    public void removeProgress(TopicProgress progress){
        topicProgressList.remove(progress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
