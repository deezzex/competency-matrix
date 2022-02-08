package com.nerdysoft.competencymatrix.entity;

import com.nerdysoft.competencymatrix.entity.dto.TopicProgressDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "progress")
public class TopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255) default 'Topic does not have any comment yet'")
    private String comment;

    @Column(columnDefinition = "integer default 0")
    private Integer mark;

    @Column(columnDefinition = "boolean default false")
    private boolean finished;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Topic topic;

    public static TopicProgress from(TopicProgressDto progressDto) {
        TopicProgress topicProgress = new TopicProgress();

        topicProgress.setComment(progressDto.getComment());
        topicProgress.setMark(progressDto.getMark());
        topicProgress.setFinished(progressDto.isFinished());

        return topicProgress;
    }

    public TopicProgress(Long id, String comment, Integer mark, boolean finished) {
        this.id = id;
        this.comment = comment;
        this.mark = mark;
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TopicProgress that = (TopicProgress) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
