package com.lcwv.openai.tools;

import com.lcwv.openai.entity.HelpDeskTicket;
import com.lcwv.openai.model.TicketRequest;
import com.lcwv.openai.service.HelpDeskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {
    private static final Logger LOGGER= LoggerFactory.getLogger(HelpDeskTools.class);
    private final HelpDeskService helpDeskService;

    //ToolContext : its like session , can store info in it and can use it later during tool execution.
     @Tool(name="createTicket",description = "Create the Support Ticket")
     String createTicket(@ToolParam(description = "Details to create a support ticket")
                         TicketRequest ticketRequest, ToolContext toolContext){
         String username = (String) toolContext.getContext().get("username");
         HelpDeskTicket ticket = helpDeskService.createTicket(ticketRequest, username);
         return "Ticket# "+ticket.getId()+" created successfully for user "+ticket;
     }

    @Tool(name="getTicketStatus",description = "Fetch the status of the open ticket base on given username")
    List<HelpDeskTicket> getTicketStatus(ToolContext toolContext){
        String username = (String) toolContext.getContext().get("username");
        return helpDeskService.getTicketsByUsername(username);
    }

}
