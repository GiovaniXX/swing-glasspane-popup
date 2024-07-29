
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class GuiActionRunner {

    // Método para executar uma ação no EDT e retornar o resultado
    public static <T> T execute(GuiQuery<T> query) {
        if (SwingUtilities.isEventDispatchThread()) {
            return query.executeInEDT();
        } else {
            final T[] result = (T[]) new Object[1];
            try {
                SwingUtilities.invokeAndWait(() -> result[0] = query.executeInEDT());
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return result[0];
        }
    }

    // Método para executar uma ação no EDT sem retornar um resultado
    public static void execute(GuiTask task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.executeInEDT();
        } else {
            try {
                SwingUtilities.invokeAndWait(task::executeInEDT);
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Interface para consultas que retornam um resultado
    public interface GuiQuery<T> {

        T executeInEDT();
    }

    // Interface para tarefas que não retornam um resultado
    public interface GuiTask {

        void executeInEDT();
    }
}
