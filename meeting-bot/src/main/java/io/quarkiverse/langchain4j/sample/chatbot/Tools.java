package io.quarkiverse.langchain4j.sample.chatbot;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Tools {
    
    @Tool("create a task for the assignee")
    public void createTask(String assignee, String content) {
        Log.info("Creating a task assigned to " + assignee + " with current content : " + content);
    }
}
