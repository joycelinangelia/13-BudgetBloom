import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StatsFrame extends JFrame {
    private JLabel titleLabel,monthLabel;
    private JButton prev,next,main,stats;
    private JPanel header,titlePanel,monthPanel,chart,expPanel,footer;
    public StatsFrame(){
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int currentMonth = LocalDate.now().getMonthValue()-1;
        int currentYear = LocalDate.now().getYear();

        header = new JPanel();
        titlePanel = new JPanel();
        titleLabel = createColoredLabel("Expenses Stats", Color.GREEN);
        monthPanel = new JPanel();
        prev = new JButton("<");
        String monthText = months[currentMonth]+currentYear;
        monthLabel = new JLabel(monthText);
        next = new JButton(">");
        chart = new JPanel();
        expPanel = new JPanel();
        footer = new JPanel();
        main = createBorderlessButton("MAIN");
        stats = createBorderlessButton("STATS");

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel,BorderLayout.CENTER);
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        groupPanel.add(prev);
        groupPanel.add(monthLabel);
        groupPanel.add(next);
        monthPanel.setLayout(new BorderLayout());
        monthPanel.add(groupPanel,BorderLayout.CENTER);
        header.setLayout(new GridLayout(2,1));
        header.add(titlePanel);
        header.add(monthPanel);

        footer.setLayout(new GridLayout(1,2));
        footer.add(main);
        footer.add(stats);

        addLineSpacingToPanel(titlePanel,10,10,10,10);
        addLineSpacingToPanel(monthPanel,0,10,10,10);
        addLineSpacingToPanel(footer,10,10,10,10);

        setLayout(new BorderLayout());
        add(header,BorderLayout.NORTH);
        add(chart, BorderLayout.CENTER);
        add(expPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }
    private JLabel createColoredLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setOpaque(true); // Make the label opaque
        label.setBackground(color); // Set the background color
        return label;
    }
    private JButton createBorderlessButton(String text) {
        JButton button = new JButton(text);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(true);
        button.setBackground(Color.RED);
        button.setOpaque(true);
        button.setFocusPainted(false);
        return button;
    }
    private void addLineSpacingToPanel(JPanel panel,int t,int l,int b,int r) {
        panel.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
    }
    private void setFontForPanel(JPanel panel, int fontSize) {
        Font font = new Font("Comic Sans MS", Font.PLAIN, fontSize);
        setFontRecursively(panel, font);
    }
    private void setFontRecursively(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setFontRecursively(child, font);
            }
        }
    }
}
