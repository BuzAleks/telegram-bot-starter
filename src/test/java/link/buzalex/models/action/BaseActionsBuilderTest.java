package link.buzalex.models.action;

import link.buzalex.models.actions.*;
import link.buzalex.models.step.BotStepsChain;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BaseActionsBuilderTest {

    @ParameterizedTest
    @MethodSource("stepProvider")
    void testStepChainBuilder(List<ExpectedStep> expectedSteps) {
        BotStepsChain stepsChain = buildTestBotStepsChain();
        ActionsContainer actionsContainer = stepsChain.stepActions();
        for (ExpectedStep expectedStep : expectedSteps) {
            testStep(expectedStep, actionsContainer);
            if (expectedStep.conditions!=null){
                ConditionalAction action = (ConditionalAction) actionsContainer.getAction();
                ActionsContainer cActionsContainer = action.conditionalActions();
                for (ExpectedStep condition : expectedStep.conditions) {
                    testStep(condition, cActionsContainer);
                    cActionsContainer = cActionsContainer.getNextAction();
                }
            }
            actionsContainer = actionsContainer.getNextAction();

        }
    }

    private static void testStep(ExpectedStep expectedStep, ActionsContainer actionsContainer) {
        assertEquals(expectedStep.name, actionsContainer.getName());
        assertEquals(expectedStep.currentActionClass, actionsContainer.getAction().getClass());
        if (expectedStep.nextActionClass == null) {
            assertNull(actionsContainer.getNextAction());
        } else {
            assertEquals(expectedStep.nextActionClass, actionsContainer.getNextAction().getAction().getClass());
        }
    }

    private static Stream<List<ExpectedStep>> stepProvider() {
        return Stream.of(
                List.of(
                        new ExpectedStep("putContextData", ExecuteAction.class, SendMessageAction.class),
                        new ExpectedStep("message", SendMessageAction.class, SendMessageAction.class),
                        new ExpectedStep("message#1", SendMessageAction.class, ExecuteAction.class),
                        new ExpectedStep("execute", ExecuteAction.class, WaitAnswerAction.class),
                        new ExpectedStep("waitAnswer", WaitAnswerAction.class, SendMessageAction.class),
                        new ExpectedStep("message#2", SendMessageAction.class, ConditionalAction.class),
                        new ExpectedStep("ifKeyboardPressed", ConditionalAction.class, ConditionalAction.class,
                                List.of(
                                        new ExpectedStep("message", SendMessageAction.class, SendMessageAction.class),
                                        new ExpectedStep("message#1", SendMessageAction.class, null)
                                )),
                        new ExpectedStep("ifKeyboardPressed#1", ConditionalAction.class, ConditionalAction.class,
                                List.of(
                                        new ExpectedStep("putContextData", ExecuteAction.class, SendMessageAction.class),
                                        new ExpectedStep("message", SendMessageAction.class, FinishStepAction.class),
                                        new ExpectedStep("finish", FinishStepAction.class, null)
                                )),
                        new ExpectedStep("ifTrue", ConditionalAction.class, ConditionalAction.class,
                                List.of(
                                        new ExpectedStep("message", SendMessageAction.class, ExecuteAction.class),
                                        new ExpectedStep("putContextData", ExecuteAction.class, PreviousStepAction.class),
                                        new ExpectedStep("previousStep", PreviousStepAction.class, null)
                                )),
                        new ExpectedStep("ifTrue#1", ConditionalAction.class, SendMessageAction.class,
                                List.of(
                                        new ExpectedStep("repeatAnswerWaiting", RepeatAnswerWaitingStepAction.class, null)
                                )),
                        new ExpectedStep("message#3", SendMessageAction.class, FinishStepAction.class),
                        new ExpectedStep("finish", FinishStepAction.class, null)
                )
        );
    }

    BotStepsChain buildTestBotStepsChain() {
        return BotStepsChain.builder()
                .name("testName")
                .putContextData("key", "data")
                .message("message")
                .message("message2")
                .execute(s -> System.out.println("test"))
                .waitAnswer()
                .message("message")
                .ifKeyboardPressed("key").message("message").message("message2").endIf()
                .ifKeyboardPressed("key2").putContextData("key", "data").message("message").finish()
                .ifTrue(s -> s.message().text().equals("test")).message("message").putContextData("key", "data").previousStep()
                .ifTrue(s -> s.message().text().equals("test")).repeatAnswerWaiting()
                .message("message")
                .finish();
    }

    private record ExpectedStep(String name, Class<?> currentActionClass, Class<?> nextActionClass,
                                List<ExpectedStep> conditions) {
        ExpectedStep(String name, Class<?> currentActionClass, Class<?> nextActionClass) {
            this(name, currentActionClass, nextActionClass, null);
        }
    }
}