package com.qaing.springai.springaidemo.controller;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {
    @Resource
    private OpenAiChatModel chatModel;
    /**
     *  我在这里使用一个成员变量来保存对话历史记录，以便在每次请求时都能保持上下文。
     */
    private final List<Message> chatHistory = new ArrayList<>();

    /**
     * 初始化对话历史记录，添加系统消息
     */
    @PostConstruct
    public void init(){
        chatHistory.add(new SystemMessage("You are a helpful assistant"));
    }

    /**
     * 测试聊天接口
     * @param message 用户输入的信息
     * @return ChatResponse
     */
    @GetMapping("/chat")
    public ChatResponse testChat(String message){
        chatHistory.add(new UserMessage(message));
        Prompt prompt = new Prompt(chatHistory);
        ChatResponse chatResponse = chatModel.call(prompt);
        if (chatResponse.getResult()!=null&&chatResponse.getResult().getOutput()!=null){
            chatHistory.add(chatResponse.getResult().getOutput());
        }
        return chatResponse;
    }

    /**
     * 发起新的会话
     * 清空对话历史记录并重新初始化
     * */
    @GetMapping("/new-session")
    public String startNewSession() {
        chatHistory.clear();
        init(); // 重新初始化对话历史记录
        return "New session started.";
    }
}
