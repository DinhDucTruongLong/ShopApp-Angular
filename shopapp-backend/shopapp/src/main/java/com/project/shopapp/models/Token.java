package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token" , length = 255)
    private String token; // la 1 chuoi dang nhap rat dai

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name ="expiration_date")// ngay gioi giay het han dang nhap chua
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

}
