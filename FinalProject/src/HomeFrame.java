import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class HomeFrame extends JFrame {

    private JPanel calendarPanel, header, footer;
    private JLabel monthLabel, incomeLabel, expenseLabel, totalLabel, titleLabel;
    private JButton addLog, next, prev, main, stats;
    private JComboBox<String> cur;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int currentMonth = LocalDate.now().getMonthValue() - 1;
    int currentYear = LocalDate.now().getYear();
    DatabaseConnection db = new DatabaseConnection();

    public HomeFrame() {
        db.initConnection();
        setTitle("Budget Bloom");
        setupUI();
    }

    private void setupUI() {
        header = new JPanel();
        footer = new JPanel();
        titleLabel = createColoredLabel("Bloom Budget",Color.decode("#FE77B2"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(0, 100));
        prev = createRoundedButton("<", Color.decode("#8F8F8F"), 50, 50);
        String monthText = months[currentMonth] +" " + currentYear;
        monthLabel = new JLabel(monthText);
        monthLabel.setPreferredSize(new Dimension(150,60));
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        next = createRoundedButton(">", Color.decode("#8F8F8F"), 50, 50);
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        groupPanel.add(prev);
        groupPanel.add(monthLabel);
        groupPanel.add(next);
        JPanel monthPanel = new JPanel();
        monthPanel.setLayout(new BorderLayout());
        monthPanel.add(groupPanel, BorderLayout.CENTER);
        JPanel financialPanel = new JPanel(new GridLayout(1, 6));
        JLabel incomeTextLabel = new JLabel("Income: ", SwingConstants.RIGHT);
        incomeLabel = new JLabel();
        JLabel expenseTextLabel = new JLabel("Expenses: ", SwingConstants.RIGHT);
        expenseLabel = new JLabel();
        JLabel totalTextLabel = new JLabel("Total: ", SwingConstants.RIGHT);
        totalLabel = new JLabel();
        financialPanel.add(incomeTextLabel);
        financialPanel.add(incomeLabel);
        financialPanel.add(expenseTextLabel);
        financialPanel.add(expenseLabel);
        financialPanel.add(totalTextLabel);
        financialPanel.add(totalLabel);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(titlePanel);
        header.add(monthPanel);
        header.add(financialPanel);

        JLabel curLabel = new JLabel("Base currency: ");
        CurrencyConverter cc = new CurrencyConverter();
        String[] curList = cc.getCurrencyList();
        cur = new JComboBox<>(curList);
        cur.setSelectedItem(db.getBaseCurrency());
        ImageIcon img = new ImageIcon(getClass().getResource("/infoBtn.png"));
        Image scaledImage = img.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JButton infoBtn = new JButton(scaledIcon);
        infoBtn.setBorderPainted(false);
        infoBtn.setContentAreaFilled(false);
        infoBtn.setFocusPainted(false);
        infoBtn.setOpaque(false);
        JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        curPanel.add(curLabel);
        curPanel.add(cur);
        curPanel.add(infoBtn);

        addLog = createRoundedButton("+", Color.decode("#575EFC"), 60, 60);
        JPanel btn = new JPanel();
        btn.setLayout(new BorderLayout());
        btn.add(curPanel,BorderLayout.WEST);
        btn.add(addLog, BorderLayout.EAST);
        main = createBorderlessButton("HOME",getContentPane().getBackground());
        stats = createBorderlessButton("STATS",getContentPane().getBackground());
        JPanel groupbtn = new JPanel();
        groupbtn.setLayout(new GridLayout(1, 2));
        groupbtn.add(main);
        groupbtn.add(stats);
        groupbtn.setPreferredSize(new Dimension(0, 60));
        footer.setLayout(new GridLayout(2, 1));
        footer.add(btn);
        footer.add(groupbtn);

        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(7,7));
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        updateCalendar(currentMonth+1,currentYear);

        addLineSpacingToPanel(titlePanel, 10, 10, 10, 10);
        addLineSpacingToPanel(monthPanel, 5, 10, 0, 10);
        addLineSpacingToPanel(financialPanel, 10, 10, 10, 10);
        addLineSpacingToPanel(calendarPanel, 0, 10, 0, 10);
        addLineSpacingToPanel(btn, 10, 10, 0, 10);
        addLineSpacingToPanel(groupbtn, 10, 8, 10, 8);

        setFontForPanel(titlePanel, 40);
        setFontForPanel(monthPanel, 20);
        setFontForPanel(financialPanel, 20);
        setFontForPanel(btn, 30);
        setFontForPanel(curPanel,20);
        setFontForPanel(groupbtn, 20);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        infoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CurInfo curInfo = new CurInfo();
                curInfo.setLocation(getLocation());
                curInfo.setSize(new Dimension(220,320));
                curInfo.setVisible(true);
                curInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        cur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCurrency = (String) cur.getSelectedItem();
                db.updateBaseCurrency(selectedCurrency);
                updateCalendar(currentMonth+1,currentYear);
            }
        });

        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentMonth == 0) {
                    currentMonth = 11;
                    currentYear--;
                } else currentMonth--;
                updateMonthLabel();
                updateCalendar(currentMonth+1,currentYear);
            }
        });

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentMonth == 11) {
                    currentMonth = 0;
                    currentYear++;
                } else currentMonth++;
                updateMonthLabel();
                updateCalendar(currentMonth+1,currentYear);
            }
        });

        main.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentMonth = LocalDate.now().getMonthValue() - 1;
                currentYear = LocalDate.now().getYear();
                updateMonthLabel();
                updateCalendar(currentMonth+1,currentYear);
            }
        });

        stats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StatsFrame statsFrame = new StatsFrame();
                statsFrame.setSize(getSize());
                statsFrame.setLocation(getLocation());
                statsFrame.setVisible(true);
                dispose();
            }
        });

        addLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogFrame logFrame = new LogFrame();
                logFrame.setSize(getSize());
                logFrame.setVisible(true);
                logFrame.setLocation(getLocation());
                dispose();
            }
        });
    }
    private JLabel createColoredLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return label;
    }
    private JButton createRoundedButton(String text, Color color, int width, int height) {
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
    private void addLineSpacingToPanel(JPanel panel, int t, int l, int b, int r) {
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
    private JButton createBorderButton(String text) {
        JButton button = new JButton(text);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        return button;
    }
    private void updateMonthLabel() {
        String monthText = months[currentMonth] + " " + currentYear;
        monthLabel.setText(monthText);
    }
    private double getTotal(Log[] list){
        CurrencyConverter cc = new CurrencyConverter();
        double sum=0;
        for(Log log: list)sum+=log.getAmount();
        return sum;
    }
    private void updateCalendar(int m,int y) {
        int z=0;
        Log[] expense = db.selectTopQuery(m,y);
        Log[] income = db.selectInQuery(m,y);
        CurrencyConverter cc = new CurrencyConverter();
        double ex = cc.convert(getTotal(expense), db.getBaseCurrency(), db.getBaseCurrency());
        double in = cc.convert(getTotal(income), db.getBaseCurrency(), db.getBaseCurrency());
        double total = in - ex;
        DecimalFormat df = new DecimalFormat("#,###.##");
        incomeLabel.setText(df.format(in));
        expenseLabel.setText(df.format(ex));
        totalLabel.setText(df.format(total));
        calendarPanel.removeAll();
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = createColoredLabel(day,Color.decode("#93E9BE"));
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayLabel.setBorder(new LineBorder(Color.BLACK));
            calendarPanel.add(dayLabel);
            z++;
        }

        YearMonth yearMonth = YearMonth.of(y, m);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        int daysInMonth = yearMonth.lengthOfMonth();
        firstDayOfWeek = (firstDayOfWeek == 7) ? 0 : firstDayOfWeek;
        for (int i = 0; i < firstDayOfWeek; i++) {
            JLabel emptyLabel = new JLabel("", SwingConstants.CENTER);
            emptyLabel.setBorder(new LineBorder(Color.BLACK));
            calendarPanel.add(emptyLabel);
            z++;
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = createBorderButton(String.valueOf(day));
            int d = day;
            dayButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    InfoFrame infoFrame = new InfoFrame(d,m,y);
                    infoFrame.setSize(getSize());
                    infoFrame.setLocation(getLocation());
                    infoFrame.setVisible(true);
                    dispose();
                }
            });
            calendarPanel.add(dayButton);
            z++;
        }

        if(z>42){
            for (int i = z; i < 49; i++) {
                JLabel emptyLabel = new JLabel("", SwingConstants.CENTER);
                emptyLabel.setBorder(new LineBorder(Color.BLACK));
                calendarPanel.add(emptyLabel);
            }

        } else if (z>35) {
            for (int i = z; i < 49; i++) {
                JLabel emptyLabel = new JLabel("", SwingConstants.CENTER);
                if(i<42)emptyLabel.setBorder(new LineBorder(Color.BLACK));
                calendarPanel.add(emptyLabel);
            }
        } else{
            for (int i = z; i < 49; i++) {
                JLabel emptyLabel = new JLabel("", SwingConstants.CENTER);
                if(i<42)emptyLabel.setBorder(new LineBorder(Color.BLACK));
                calendarPanel.add(emptyLabel);
            }
        }

        setFontForPanel(calendarPanel, 16);
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}

