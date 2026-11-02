package ru.lovity.bot.model;

public record IncomingMessage(
        String userId,
        String text
) {
}