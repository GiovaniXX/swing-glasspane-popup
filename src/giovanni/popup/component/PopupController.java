package giovanni.popup.component;

public abstract class PopupController {

    private boolean consume;

    public boolean getConsume() {
        return this.consume;
    }

    public void consume() {
        this.consume = true;
    }

    public abstract void closePopup();
}
