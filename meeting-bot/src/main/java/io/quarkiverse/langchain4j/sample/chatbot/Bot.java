package io.quarkiverse.langchain4j.sample.chatbot;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.SessionScoped;

@RegisterAiService(tools = Tools.class)
@SessionScoped
public interface Bot {

    @SystemMessage("""
       You are an AI answering questions about team's meeting.
       You will try to answer by summarizing the meeting with short sentences, when action needs to be taken you add a task to the system.
            """)
    String chat(@UserMessage String question);
}
