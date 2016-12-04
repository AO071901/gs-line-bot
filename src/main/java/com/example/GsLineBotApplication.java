package com.example;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
@LineMessageHandler
public class GsLineBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(GsLineBotApplication.class, args);
	}

//	@EventMapping
//	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
//		System.out.println("event: " + event);
//		System.out.println("reply: " + event.getMessage().getText());
//		return new TextMessage(event.getMessage().getText());
//	}

	//	@EventMapping
//	public void handleDefaultMessageEvent(Event event) {
//		System.out.println("event: " + event);
//	}

	@Autowired
	private LineMessagingService lineMessagingService;

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		System.out.println("event: " + event);
		final BotApiResponse apiResponse = lineMessagingService
				.replyMessage(new ReplyMessage(event.getReplyToken(),
						Collections.singletonList(new TextMessage(event.getSource().getUserId()))))
				.execute().body();
		System.out.println("Sent messages: " + apiResponse);
	}

	@EventMapping
	public void defaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}
}
