
import javax.swing.JButton;


public class JButtonMatcher {

    private String text;

    // Construtor privado para forçar o uso do método estático 'withText'
    private JButtonMatcher(String text) {
        this.text = text;
    }

    // Método estático para criar uma instância de JButtonMatcher
    public static JButtonMatcher withText(String text) {
        return new JButtonMatcher(text);
    }

    // Método para verificar se um botão corresponde ao texto esperado
    public boolean matches(JButton button) {
        return button.getText().equals(text);
    }

    @Override
    public String toString() {
        return "JButtonMatcher{text='" + text + '\'' + '}';
    }
}
