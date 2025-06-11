# MCP with Quarkus Mini Lab

## Goal of the lab

In this lab you will learn how to create a MCP Server from scratch and extract `Tools` from an existing app to the MCP Server.

## Pre-requisites 

* Java 17/22
* Maven
* Ollama

## Step 1 : Running the initial application

Go to the `meeting-bot` folder and run the app : `mvn quarkus:dev` or `quarkus dev` and open your browser to `localhost:8080`. 

Play around with the chat bot, try to make the bot calling the Tools, add also a tool to discover how it works. 

## Step 2 : Creating your MCP Server

Go to the `mcp-server` folder and open the project in your editor. Create a new class that will contains your Tools, call it whatever you want, in the package that you want. 

Check the documentation [here](https://docs.quarkiverse.io/quarkus-mcp-server/dev/). 

Start your MCP Server :  `mvn quarkus:dev` or `quarkus dev` , it will be running on port `8082`. 

## Step 3 : Refactor your initial app to a MCP Client

Add the MCP Client Extension : 

```
   <dependency>
        <groupId>io.quarkiverse.langchain4j</groupId>
        <artifactId>quarkus-langchain4j-mcp</artifactId>
        <version>0.27.0.CR1</version>
    </dependency>
```

Remove the `Tools.java` class and fix your Ai Service regarding the Tools annotation paramater.

Update your `application.properties` : 

```
quarkus.langchain4j.mcp.workshop.transport-type=HTTP
quarkus.langchain4j.mcp.workshop.url=http://localhost:8082/mcp/sse

```

Try the app again and make sure that this time the Tools are called on the MCP Server side. 

## Step 4 : Extracting the prompt (expiremental)

MCP can also manage your prompt, let's update our MCP Server by adding a prompt definition : 

```
    @Prompt(name="meeting_bot")
    PromptMessage codeAssist() {
        return PromptMessage.withAssistantRole("""
       You are an AI answering questions about team's meeting.
       You will try to answer by summarizing the meeting with short sentences, when action needs to be taken you add a task to the system.
                """);
    }

```

Back to your meeting-bot app first modify the AI Service to add a parameter to the method signature and reference it in the prompt template annotation : 

```
@RegisterAiService()
@SessionScoped
public interface Bot {

    @SystemMessage("""
      {systemMessage}
            """)
    String chat(String systemMessage, @UserMessage String question);
}

```

And finally modify the Websocket resource (`ChatBotWebSocket`) : 

First inject the MCPClient : 

```
  @Inject
  @McpClientName("workshop")
  McpClient mcpClient;
```

And finally update the method calling the AI Service : 

```
    @OnTextMessage
    public String onMessage(String message) {
        McpGetPromptResult prompt =  mcpClient.getPrompt("meeting_bot",new HashMap<>());
        return bot.chat(prompt.messages().get(0).content().toString() ,message);
    }
```

Restart your apps and amke sure that the prompt is now served by the MCP Server.

## Bonus tracks 

### Secure your MCP Server and Client



