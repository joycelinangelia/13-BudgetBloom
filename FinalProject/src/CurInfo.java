import javax.swing.*;
import java.util.*;
import java.awt.*;

public class CurInfo extends JFrame{
    public CurInfo(){
        setTitle("Info");
        CurrencyConverter cc = new CurrencyConverter();
        String[] cur = cc.getCurrencyList();
        String[] info = {"Australian Dollar","Chinese Yuan Renminbi","Euro","Hong Kong Dollar","Indonesian Rupiah","Japanese Yen","South Korean Won","Burmese Kyat","Malaysian Ringgit","Philippine Peso","Singapore Dollar","Taiwan New Dollar","US Dollar","Vietnamese Dong","Thai Baht","Macau Pataca"};
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        for(int i=0;i<16;++i){
            JLabel txt = new JLabel(cur[i]+" : "+info[i]);
            main.add(txt);
        }
        addLineSpacingToPanel(main,10,10,10,10);
        setLayout(new BorderLayout());
        add(main,BorderLayout.NORTH);
    }
    private void addLineSpacingToPanel(JPanel panel, int t, int l, int b, int r) {
        panel.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
    }
}
