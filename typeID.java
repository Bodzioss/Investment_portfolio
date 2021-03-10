import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class typeID extends JFrame implements ActionListener {
    final int WIDTH=400;
    final int HEIGHT=140;
    List<Share> shares;
    JLabel lID;
    JTextField tID;

    public typeID(List<Share> shares,MainWin win,boolean sell)
    {
        this.shares=shares;
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        if(!sell) {
            lID = new JLabel("PODAJ ID AKCJI KTÓRĄ CHCESZ DOKUPIĆ", SwingConstants.CENTER);
        }else
        {
            lID = new JLabel("PODAJ ID AKCJI KTÓRĄ CHCESZ SPRZEDAĆ", SwingConstants.CENTER);
        }
        lID.setBounds(WIDTH/2-150,10,300,25);
        add(lID);

        tID=new JTextField();
        tID.setBounds(WIDTH/2-150,50,300,25);
        add(tID);
        tID.addActionListener(e -> {
            boolean isFound=false;
            for(int i=0;i<shares.size();i++){
                if(shares.get(i).name.equals(tID.getText())){
                    setVisible(false);
                    isFound=true;
                    new AddShareWin(shares,i,sell);
                }
            }
            if(!isFound) {
                JOptionPane.showMessageDialog(win, "Akcja o podanym ID nie znajduje się w systemie");
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //NOTHING TO SHOW
    }
}