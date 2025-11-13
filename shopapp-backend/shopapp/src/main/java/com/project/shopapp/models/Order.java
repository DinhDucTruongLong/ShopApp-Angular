package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity // để biet là thực thể trong javaspring
@Table(name ="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @Column(name = "fullname", nullable = false, length = 200)
    private String fullname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "note", nullable = false, length = 100)
    private String note;

    @Column(name ="order_date")
    private Date orderDate;

    @Column(name="status")
    private String status;

    @Column(name= "total_money")
    private Integer totalMoney;

    @Column(name= "shipping_method")
    private String shippingMethod;

    @Column(name= "shipping_address")
    private String shippingAddress;

    @Column(name= "shipping_date")
    private LocalDate shippingDate;

    @Column(name= "tracking_number")
    private String trackingNumber;

    @Column(name= "payment_method")
    private String paymentMethod;


    @Column(name= "active")
    private Boolean active;// thuoc ve admin

}
