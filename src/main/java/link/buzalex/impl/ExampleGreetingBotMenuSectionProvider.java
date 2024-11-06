package link.buzalex.impl;

import link.buzalex.annotation.EntryPoint;
import link.buzalex.annotation.StepsChain;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.menu.BotEntryPointBuilder;
import link.buzalex.models.message.BotMessageReply;
import link.buzalex.models.step.BotStepsChain;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExampleGreetingBotMenuSectionProvider {

    @EntryPoint
    public BotEntryPoint example() {
        return BotEntryPointBuilder
                .name("first")
                .selector(s -> "/start".equals(s.text()))
                .stepsChain(rootStep())
                .build();
    }

    public BotStepsChain rootStep() {
        return BotStepsChain.builder()
                .name("rootStep")
                .putContextData("initYear", 1995)
                .message("What's your name?")
                .waitAnswer()
                .putMessageTextToContext("name")
                .nextStep(askYear());
    }

    public BotStepsChain askYear() {
        return BotStepsChain.builder()
                .name("askYear")
                .removeLastMessage()
                .fKeyboard("Year of birth?", """
                        < | #{#initYear} |  #{#initYear+1} | #{#initYear+2} | #{#initYear+3} | >
                        """)
                .waitAnswer()
                .ifKeyboardPressed("<").modifyContextDataAsInt("initYear", year -> year - 3).repeatCurrentStep()
                .ifKeyboardPressed(">").modifyContextDataAsInt("initYear", year -> year + 3).repeatCurrentStep()
                .putMessageTextToContext("year")
                .nextStep(askMonth());
    }

    public BotStepsChain askMonth() {
        return BotStepsChain.builder()
                .name("askMonth")
                .removeLastMessage()
                .message(s -> {
                    List<Object> months = Arrays.stream(Month.values())
                            .map(m -> (Object) m.name())
                            .collect(Collectors.toList());
                    return BotMessageReply.builder()
                            .text("Month of birth?")
                            .simpleKeyboard(StreamEx.ofSubLists(months, 3).toList())
                            .build();
                })
                .waitAnswer()
                .putMessageTextToContext("month")
                .nextStep(askDay());
    }

    public BotStepsChain askDay() {
        return BotStepsChain.builder()
                .name("askDay")
                .removeLastMessage()
                .message(s -> {
                    final Integer year = s.context().getAsInt("year").get();
                    final String month = s.context().getAsString("month").get();
                    final LocalDate localDate = LocalDate.of(year, Month.valueOf(month), 1);
                    final List<Object> collect = localDate
                            .minusDays(localDate.getDayOfWeek().getValue() - 1)
                            .datesUntil(localDate.plusMonths(1))
                            .map(dt -> dt.getMonth().equals(localDate.getMonth()) ? dt.getDayOfMonth() : "-")
                            .collect(Collectors.toList());
                    return BotMessageReply.builder()
                            .text("Day of birth?")
                            .simpleKeyboard(
                                    StreamEx.ofSubLists(collect, 7).toList()
                            )
                            .build();
                })
                .waitAnswer()
                .removeLastMessage()
                .ifKeyboardPressed("-").repeatCurrentStep()
                .putMessageTextToContext("day")
                .nextStep(greeting());
    }

    public BotStepsChain greeting() {
        return BotStepsChain.builder()
                .name("greeting")
                .removeLastMessage()
                .execute(s -> {
                    final int day = s.context().getAsInt("day").get();
                    final int year = s.context().getAsInt("year").get();
                    final Month month = Month.valueOf(s.context().getAsString("month").get());
                    final long count = LocalDate.of(year, month, day).datesUntil(LocalDate.now()).count();
                    s.context().put("count", count);
                })
                .message("Hi #{#name}! You are #{#count} days old :)")
                .finish();
    }
}
