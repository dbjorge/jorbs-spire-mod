package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.function.Consumer;

public class ConsumerGameAction<T> extends AbstractGameAction {
    private Consumer<T> consumer;
    private T input;

    public ConsumerGameAction(Consumer<T> consumer, T input) {
        this.consumer = consumer;
        this.input = input;
    }

    @Override
    public void update() {
        consumer.accept(input);
        isDone = true;
    }
}
