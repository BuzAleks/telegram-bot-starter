package link.buzalex.impl;

import link.buzalex.annotation.EntryPoint;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.menu.BotEntryPointBuilder;
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
                .fKeyboard("Year of birth?", """
                        < | #{#initYear} |  #{#initYear+1} | #{#initYear+2} | #{#initYear+3} | >
                        BACK | CANCEL
                        """)
                .waitAnswer()
                .ifKeyboardPressed("<").modifyContextDataAsInt("initYear", year -> year - 4).repeatCurrentStep()
                .ifKeyboardPressed(">").modifyContextDataAsInt("initYear", year -> year + 4).repeatCurrentStep()
                .ifKeyboardPressed("BACK").removeLastMessage().previousStep()
                .ifKeyboardPressed("CANCEL").removeLastMessage().finish()
                .putMessageTextToContext("year")
                .nextStep(askMonth());
    }

    public BotStepsChain askMonth() {
        return BotStepsChain.builder()
                .name("askMonth")
                .keyboard("Month of birth?", s -> {
                    List<Object> months = Arrays.stream(Month.values())
                            .map(m -> (Object) m.name())
                            .collect(Collectors.toList());
                    months.add("BACK");
                    months.add("CANCEL");
                    return StreamEx.ofSubLists(months, 3).toList();
                })
                .waitAnswer()
                .ifKeyboardPressed("BACK").previousStep()
                .ifKeyboardPressed("CANCEL").removeLastMessage().finish()
                .putMessageTextToContext("month")
                .nextStep(askDay());
    }

    public BotStepsChain askDay() {
        return BotStepsChain.builder()
                .name("askDay")
                .keyboard("Day of birth?", this::getCalendarKeyboard)
                .waitAnswer()
                .ifKeyboardPressed("-").repeatAnswerWaiting()
                .ifKeyboardPressed("BACK").previousStep()
                .ifKeyboardPressed("CANCEL").removeLastMessage().finish()
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

    private List<List<Object>> getCalendarKeyboard(UserMessageContainer s) {
        final Integer year = s.context().getAsInt("year").get();
        final String month = s.context().getAsString("month").get();
        LocalDate startDate = LocalDate.of(year, Month.valueOf(month), 1);
        int offset = startDate.getDayOfWeek().getValue() - 1;
        int calendarDays = (offset + startDate.lengthOfMonth()) > 35 ? 42 : 35;
        List<Object> calendarGrid = startDate.minusDays(offset)
                .datesUntil(startDate.plusDays(calendarDays - offset))
                .map(date -> date.getMonth().equals(startDate.getMonth()) ? date.getDayOfMonth() : "-")
                .collect(Collectors.toList());
        calendarGrid.add("BACK");
        calendarGrid.add("CANCEL");
        return StreamEx.ofSubLists(calendarGrid, 7).toList();
    }
}
