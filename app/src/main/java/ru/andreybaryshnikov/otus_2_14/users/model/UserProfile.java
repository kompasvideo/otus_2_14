package ru.andreybaryshnikov.otus_2_14.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "avatar_uri")
    private String avatar_uri;

    @Column(name = "age")
    private String age;

    public UserProfile() {
        this.id = 0L;
        this.avatar_uri = "";
        this.age = "0";
    }

    public UserProfile(Long id, @NonNull String avatarUri, String age) {
        this.id = id;
        this.avatar_uri = avatarUri;
        this.age = age;
    }
}
