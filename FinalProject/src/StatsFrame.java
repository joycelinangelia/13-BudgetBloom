import java.awt.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StatsFrame extends JFrame {
    private JLabel titleLabel,monthLabel;
    private JButton prev,next,main,stats;
    private JPanel header,titlePanel,monthPanel,expPanel,footer;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int currentMonth = LocalDate.now().getMonthValue()-1;
    int currentYear = LocalDate.now().getYear();
    DatabaseConnection db = new DatabaseConnection();
    public StatsFrame() {
        db.initConnection();
        setTitle("Expenses Stats");
        setupUI();
    }
    private void setupUI(){
        header = new JPanel();
        titlePanel = new JPanel();
        titleLabel = createColoredLabel("Expenses Stats", Color.decode("#1ECC28"),getWidth());
        titleLabel.setPreferredSize(new Dimension(0,80));
        monthPanel = new JPanel();
        prev = createRoundedButton("<",Color.decode("#8F8F8F"),50,50);
        String monthText = months[currentMonth]+" "+currentYear;
        monthLabel = new JLabel(monthText);
        monthLabel.setPreferredSize(new Dimension(150,60));
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        next = createRoundedButton(">",Color.decode("#8F8F8F"),50,50);
        expPanel = new JPanel();
        footer = new JPanel();
        main = createBorderlessButton("HOME",getContentPane().getBackground());
        stats = createBorderlessButton("STATS",getContentPane().getBackground());

        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel,BorderLayout.CENTER);
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        groupPanel.add(prev);
        groupPanel.add(monthLabel);
        groupPanel.add(next);
        monthPanel.setLayout(new BorderLayout());
        monthPanel.add(groupPanel,BorderLayout.CENTER);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(titlePanel);
        header.add(monthPanel);

        footer.setLayout(new GridLayout(1,2));
        footer.add(main);
        footer.add(stats);
        footer.setPreferredSize(new Dimension(0,70));

        addLineSpacingToPanel(titlePanel,10,10,10,10);
        addLineSpacingToPanel(monthPanel,5,10,10,10);
        addLineSpacingToPanel(expPanel,10,10,10,10);
        addLineSpacingToPanel(footer,10,8,10,8);

        setFontForPanel(titlePanel, 40);
        setFontForPanel(monthPanel, 20);
        setFontForPanel(footer, 20);

        JScrollPane scrollPane = new JScrollPane(expPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(13);
        scrollPane.setBorder(null);

        setLayout(new BorderLayout());
        add(header,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        topExpenses(currentMonth,currentYear);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentMonth == 0) {
                    currentMonth = 11;
                    currentYear--;
                } else currentMonth--;
                updateMonthLabel();
                topExpenses(currentMonth,currentYear);
            }
        });

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentMonth == 11) {
                    currentMonth = 0;
                    currentYear++;
                } else currentMonth++;
                updateMonthLabel();
                topExpenses(currentMonth,currentYear);
            }
        });

        main.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomeFrame homeFrame = new HomeFrame();
                homeFrame.setSize(getSize());
                homeFrame.setLocation(getLocation());
                homeFrame.setVisible(true);
                dispose();
            }
        });

        stats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentMonth = LocalDate.now().getMonthValue()-1;
                currentYear = LocalDate.now().getYear();
                updateMonthLabel();
                topExpenses(currentMonth,currentYear);
            }
        });
    }
    private void updateMonthLabel() {
        String monthText = months[currentMonth] + " " + currentYear;
        monthLabel.setText(monthText);
    }
    private JLabel createColoredLabel(String text, Color color,int width) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        label.setPreferredSize(new Dimension(width*5,0));
        return label;
    }
    private JButton createRoundedButton(String text,Color color,int width,int height) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                g.setColor(color);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 100, 100));
                super.paintComponent(g);
            }
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(color);
        return button;
    }
    private JButton createBorderlessButton(String text,Color frameBgColor) {
        JButton button = new JButton(text);
        button.setBorder(BorderFactory.createLineBorder(frameBgColor,2));
        button.setBackground(Color.decode("#54B3F6"));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setFocusPainted(false);
        return button;
    }
    private void addLineSpacingToPanel(JPanel panel,int t,int l,int b,int r) {
        panel.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
    }
    private void setFontForPanel(JPanel panel, int fontSize) {
        Font font = new Font("Britannic Bold", Font.PLAIN, fontSize);
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
    private double getTotalExp(Log[] list){
        CurrencyConverter cc = new CurrencyConverter();
        double sum=0;
        for(Log log: list)sum+=log.getAmount();
        return sum;
    }
    private void topExpenses(int m, int y) {
        Log[] list = db.selectTopQuery(m+1,y);
        double totalExp = getTotalExp(list);
        expPanel.removeAll();
        expPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0,0,5,50);
        int yPos = 0;

        for (Log log : list) {
            if (log.getAmount() > 0) {
                int p = (int) (log.getAmount() * 100 / totalExp);
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                JLabel cat = new JLabel(log.getCategory());
                JLabel percent = createColoredLabel("",Color.decode("#D82626"),p);
                JLabel per = new JLabel(p+"%");
                DecimalFormat df = new DecimalFormat("#,###.##");
                JLabel am = new JLabel(db.getBaseCurrency()+" "+df.format(log.getAmount()));
                panel.add(percent, BorderLayout.WEST);
                panel.add(per, BorderLayout.CENTER);
                panel.add(am, BorderLayout.EAST);
                JPanel container = new JPanel(new GridLayout(2,1));
                container.add(cat);
                container.add(panel);
                gbc.gridy = yPos++;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                expPanel.add(container, gbc);
                setFontForPanel(expPanel, 20);
            }
        }
        revalidate();
        repaint();
    }
}
