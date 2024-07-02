package com.sparta.spartime.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "likes")
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    private Long refId;

    public enum ReferenceType {
        POST,
        COMMENT
    }

    public Like(User user , Post post){
        this.user = user;
        this.referenceType = ReferenceType.POST;
        this.refId = post.getId();
    }

    public Like(User user , Comment comment){
        this.user = user;
        this.referenceType = ReferenceType.COMMENT;
        this.refId = comment.getId();
    }

}
