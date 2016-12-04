package com.example;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

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

//	@EventMapping
//	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
//		System.out.println("event: " + event);
//		final BotApiResponse apiResponse = lineMessagingService
//				.replyMessage(new ReplyMessage(event.getReplyToken(),
//						Collections.singletonList(new TextMessage(event.getSource().getUserId()))))
//				.execute().body();
//		System.out.println("Sent messages: " + apiResponse);
//	}

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
		TextMessageContent message = event.getMessage();
		handleTextContent(event.getReplyToken(), event, message);
	}

	@EventMapping
	public void defaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

	private void reply(@lombok.NonNull String replyToken, @lombok.NonNull Message message) {
		reply(replyToken, java.util.Collections.singletonList(message));
	}

	private void reply(@lombok.NonNull String replyToken, @lombok.NonNull java.util.List<com.linecorp.bot.model.message.Message> messages) {
		try {
			retrofit2.Response<com.linecorp.bot.model.response.BotApiResponse> apiResponse = lineMessagingService
					.replyMessage(new ReplyMessage(replyToken, messages))
					.execute();
//			log.info("Sent messages: {}", apiResponse);
		} catch (IOException e) {
			throw new java.io.UncheckedIOException(e);
		}
	}

	private void replyText(@lombok.NonNull String replyToken, @lombok.NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "……";
		}
		this.reply(replyToken, new TextMessage(message));
	}

	private void handleTextContent(String replyToken, Event event, TextMessageContent content)
			throws IOException {
		String text = content.getText();

//		log.info("Got text message from {}: {}", replyToken, text);
		switch (text) {
			default:
//				log.info("Returns echo message {}: {}", replyToken, text);
				this.replyText(
						replyToken,
						text
				);
				break;
		}
	}
}
