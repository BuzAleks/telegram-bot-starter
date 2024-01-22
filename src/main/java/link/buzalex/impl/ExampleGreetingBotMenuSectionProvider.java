package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionProvider;
import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserContextImpl;
import link.buzalex.models.menu.BotStepBuilder;
import link.buzalex.models.menu.MenuSection;
import link.buzalex.models.menu.MenuSectionBuilder;
import one.util.streamex.StreamEx;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnMissingBean(value = BotMenuSectionProvider.class, ignored = ExampleGreetingBotMenuSectionProvider.class)
public class ExampleGreetingBotMenuSectionProvider implements BotMenuSectionProvider<UserContextImpl> {

    @Override
    public MenuSection provideMenuSection() {
        // TODO: 16.01.2024 Add keyboard row and col methods to add keyboard
        // TODO: 16.01.2024 add methods for operating with last message (remove, edit)
        // TODO: 16.01.2024 add steps reuse feature, rootStep as string and annotation for step adding
        // TODO: 16.01.2024 add method exceptionHandler for step
        // TODO: 16.01.2024 Try to generify UserMessageContainer to work with UserContext inheritance
        // TODO: 16.01.2024 add waitAnswer method with waiting time and do smth on timeout (maybe we need timestamp)
        // TODO: 16.01.2024 add method sleep() with sleep time as parameter
        return MenuSectionBuilder
                .name("first")
                .selector(s -> "/start".equals(s.text()))
                .steps(nextStep())
                .build();
    }

    public BotStepBuilder nextStep() {
        return BotStepBuilder
                .name("nextStep")
                .peek(s -> s.context().getData().put("initYear", "1995"))
                .message("What's your name?")
                .waitAnswer()
                .saveAs("name")
                .nextStep(nextStep2());
    }

    public BotStepBuilder nextStep2() {
        return BotStepBuilder
                .name("nextStep2")

                .message(s -> {
                    int initYear = Integer.parseInt(s.context().getData().get("initYear"));
                    return BotMessageReply.builder()
                            .text("Year of birth?")
                            .simpleKeyboard(List.of(
                                    List.of("<", initYear + 1, initYear + 2, initYear + 3, ">")
                            ))
                            .build();
                })
                .waitAnswer()
                .ifTrue(s -> s.message().text().equals("<")).nextStep(minus())
                .ifTrue(s -> s.message().text().equals(">")).nextStep(plus())
                .saveAs("year")
                .nextStep(month());
    }

    public BotStepBuilder minus() {
        return BotStepBuilder
                .name("minus")
                .peek(s -> {
                    int initYear = Integer.parseInt(s.context().getData().get("initYear")) - 3;
                    s.context().getData().put("initYear", Integer.toString(initYear));
                })
                .clearLast()
                .nextStep("nextStep2");
    }

    public BotStepBuilder plus() {
        return BotStepBuilder
                .name("plus")
                .peek(s -> {
                    int initYear = Integer.parseInt(s.context().getData().get("initYear")) + 3;
                    s.context().getData().put("initYear", Integer.toString(initYear));
                })
                .clearLast()
                .nextStep("nextStep2");
    }

    public BotStepBuilder month() {
        return BotStepBuilder
                .name("month")
                .clearLast()
                .message(s -> {
                    final List<Object> months = Arrays.stream(Month.values()).map(Enum::name).map(t -> (Object) t).collect(Collectors.toList());
                    return BotMessageReply.builder()
                            .text("Month of birth?")
                            .simpleKeyboard(StreamEx.ofSubLists(months, 2).toList())
                            .build();
                })
                .waitAnswer()
                .saveAs("month")
                .nextStep(day());
    }

    public BotStepBuilder day() {
        return BotStepBuilder
                .name("day")
                .clearLast()
                .message(s -> {
                    final Integer year = Integer.parseInt(s.context().getData().get("year"));
                    final String month = s.context().getData().get("month");
                    final LocalDate localDate = LocalDate.of(year, Month.valueOf(month), 1);
                    final List<Object> collect = localDate
                            .minusDays(localDate.getDayOfWeek().getValue() - 1)
                            .datesUntil(localDate.plusMonths(1))
                            .map(dt -> {
                                if (dt.getMonth().equals(localDate.getMonth())) {
                                    return dt.getDayOfMonth();
                                } else {
                                    return "-";
                                }
                            })
                            .collect(Collectors.toList());
                    return BotMessageReply.builder()
                            .text("Day of birth?")
                            .simpleKeyboard(
                                    StreamEx.ofSubLists(collect, 7).toList()
                            )
                            .build();
                })
                .waitAnswer()
                .clearLast()
                .ifTrue(s -> s.message().text().equals("-")).nextStep("month")
                .saveAs("day")
                .nextStep(greeting());
    }

    public BotStepBuilder greeting() {
        return BotStepBuilder
                .name("greeting")
                .clearLast()
                .message(s -> {
                    final String name = s.context().getData().get("name");
                    final int day = Integer.parseInt(s.context().getData().get("day"));
                    final int year = Integer.parseInt(s.context().getData().get("year"));
                    final Month month = Month.valueOf(s.context().getData().get("month"));
                    final long count = LocalDate.of(year, month, day).datesUntil(LocalDate.now()).count();
                    final String hi_ = new StringBuilder("Hi ")
                            .append(name)
                            .append("! You are ")
                            .append(count)
                            .append(" days old :)")
                            .toString();


                    return BotMessageReply.builder().text(hi_).build();
                })
                .waitAnswer()
                .finish();
    }

}
