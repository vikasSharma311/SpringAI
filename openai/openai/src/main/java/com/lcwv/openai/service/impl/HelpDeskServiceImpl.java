package com.lcwv.openai.service.impl;

import com.lcwv.openai.entity.HelpDeskTicket;
import com.lcwv.openai.model.TicketRequest;
import com.lcwv.openai.repository.HelpDeskRepository;
import com.lcwv.openai.service.HelpDeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskServiceImpl implements HelpDeskService {
    private final HelpDeskRepository helpDeskRepository;
    @Override
    public HelpDeskTicket createTicket(TicketRequest ticketInput, String username) {
        HelpDeskTicket ticket = HelpDeskTicket.builder()
                .issue(ticketInput.issue())
                .username(username)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .expectedToBeClosed(LocalDateTime.now().plusDays(7))
                .build();
        return helpDeskRepository.save(ticket);
    }
    @Override
    public List<HelpDeskTicket> getTicketsByUsername(String username) {
        return helpDeskRepository.findByUsername(username);
    }
}
