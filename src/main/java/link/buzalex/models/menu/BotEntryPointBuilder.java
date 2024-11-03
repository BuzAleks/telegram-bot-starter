package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.step.BotStepsChain;

import java.util.function.Predicate;

public final class BotEntryPointBuilder {
    private String name;
    private Predicate<BotMessage> selector;
    private int order;
    private String rootStepName;
    private BotStepsChain stepsChain;
    private BotEntryPointBuilder(String name) {
        this.name = name;
    }

    public static BotEntryPointBuilder name(String name) {
        return new BotEntryPointBuilder(name);
    }


    public BotEntryPointBuilder selector(Predicate<BotMessage> selector) {
        this.selector = selector;
        return this;
    }

    public BotEntryPointBuilder order(int order) {
        this.order = order;
        return this;
    }

    public BotEntryPointBuilder rootStep(String rootStepName) {
        this.rootStepName = rootStepName;
        return this;
    }

    public BotEntryPointBuilder stepsChain(BotStepsChain stepsChain) {
        this.stepsChain = stepsChain;
        return this;
    }

    public BotEntryPoint build() {
        return new BotEntryPoint(name, selector, order, rootStepName, stepsChain);
    }
}
