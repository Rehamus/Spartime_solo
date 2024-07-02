package com.sparta.spartime.entity;

import com.sparta.spartime.entity.common.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "users")
public class User extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String intro;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private String recentPassword;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long socialId;

    public void changeRole(Role role) {
        this.role = role;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public enum Role {
        USER,
        ADMIN
    }

    public enum Status {
        ACTIVITY,
        INACTIVITY,
        BLOCKED
    }

    @Transient
    public boolean isActivity() {
        return this.status == Status.ACTIVITY;
    }

    public boolean isBlocked() {
        return this.status == Status.BLOCKED;
    }

    public void addRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public User socialIdUpdate(Long socialId ) {
        this.refreshToken = null;
        this.socialId = socialId;
        return this;
    }

    public void withdraw() {
        this.status = Status.INACTIVITY;
        deleteRefreshToken();
        delete();
    }

    public void updateRecentPassword(String updatePasswordList) {
        this.recentPassword = updatePasswordList;
    }

    public void editProfile(String newPassword, String newNickname, String newIntro) {
        if (newPassword != null && !newPassword.isEmpty() && !this.password.equals(newPassword)) {
            this.password = newPassword;
        }
        if (newNickname != null && !newNickname.isEmpty() && !this.nickname.equals(newNickname)) {
            this.nickname = newNickname;
        }
        if (newIntro != null && !newIntro.isEmpty() && !this.intro.equals(newIntro)) {
            this.intro = newIntro;
        }
    }
}
