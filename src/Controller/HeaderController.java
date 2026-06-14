package Controller;

import View.AdminProfileView;
import View.HeaderPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderController extends MouseAdapter {
    private HeaderPanel frame;
    public HeaderController(HeaderPanel frame) {
        this.frame = frame;
    }
    @Override
    public void mouseClicked (MouseEvent e){
        if (e.getSource() == frame.profilePanel) {
            AdminProfileView profile = new AdminProfileView(frame.model);
            profile.setVisible(true);
        }
    }

}
