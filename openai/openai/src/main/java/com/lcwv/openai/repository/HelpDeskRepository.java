package com.lcwv.openai.repository;

import com.lcwv.openai.entity.HelpDeskTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpDeskRepository extends JpaRepository<HelpDeskTicket,Long> {
    List<HelpDeskTicket> findByUsername(String userName);
}
