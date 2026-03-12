package com.lcwv.openai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {
    private static Logger Logger= LoggerFactory.getLogger(TimeTools.class);
    @Tool(name = "getCurrentLocalTime",description = "Get the current Time in the user's  TimeZone")
    String getCurrentLocalTime(){
        Logger.info("Returning current Time in user's timeZone");
        return LocalTime.now().toString();
    }
    @Tool(name = "getCurrentTime",description = "Get the current Time in the specified TimeZone" )
    String getCurrentTime(@ToolParam(description = "The value representing the time zone") String timeZone){
        Logger.info("Returning current Time in timeZone {}",timeZone);
        return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}
