package com.lcwv.openai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor  // Added this for JPA
@AllArgsConstructor  // Required by @Builder if NoArgsConstructor is present
@Entity
@Table(name = "helpdesk_ticket")
public class HelpDeskTicket {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String username;
    private String issue;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expectedToBeClosed;


}
