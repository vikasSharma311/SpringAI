package com.lcwv.openai.service;

import com.lcwv.openai.entity.HelpDeskTicket;
import com.lcwv.openai.model.TicketRequest;

import java.util.List;

public interface HelpDeskService {
    public HelpDeskTicket createTicket(TicketRequest ticketInput, String username);
    public List<HelpDeskTicket> getTicketsByUsername(String username);
}
