package re.jpayet.mentdb.editor;

import java.awt.Dimension;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

class SingleRowTabbedPaneUI extends BasicTabbedPaneUI {
	
	public static JButton moveRightButton = null;
	
	@Override
    protected JButton createScrollButton(int direction) {

        JButton b = new JButton();

        b.setPreferredSize(new Dimension(32, 32));

        if (direction == SOUTH) {
            b.setIcon(new ImageIcon("images"+File.separator+"level.png"));
        } else if (direction == NORTH) {
            b.setIcon(new ImageIcon("images"+File.separator+"level.png"));
        } else if (direction == WEST) {
            b.setIcon(new ImageIcon("images"+File.separator+"tableft.png"));
        } else {
            b.setIcon(new ImageIcon("images"+File.separator+"tabright.png"));
            moveRightButton = b;
        }

        b.setBorder(null);
        
        return b;
    }
	

	 
    public static void main(String[] args) {
        JTabbedPane pane = new JTabbedPane();
        //pane.setUI(new SingleRowTabbedPaneUI());
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        for(int i=0; i<40; i++) {
            pane.addTab("tab "+i, new JLabel(""+i));
        }
 
        JFrame f = new JFrame();
        f.add(pane);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
 
    }

}
