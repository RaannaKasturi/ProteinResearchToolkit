package Backend;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class VisualizeButton extends JButton{
    public void ActionButton(){
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(3,3,3,3));
    }
}
