import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class HistoryWin extends JFrame implements ActionListener {
    List<JLabel> labels=new ArrayList<>();
    List<Share> sold;
    List<Share> shares;
    JPanel cat,cont,sum;
    JLabel title,jSumValue,jSumTotalValue,jSumProfit,jPercent;
    JButton back;
    Border border =BorderFactory.createLineBorder(Color.BLACK);
    final int FRAME_WIDTH=1100;
    final int FRAME_HEIGHT=550;
    private int rows=1;
    private double sTotalValue;
    private double sTotalProfit;


    public HistoryWin(List<Share> shares)
    {
        this.shares=shares;
        try {
            this.sold=readHistory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setTitle("Rachunek maklerski (Historia)");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        setLocationRelativeTo(null);
        Image icon = Toolkit.getDefaultToolkit().getImage("TkyHAZ_3.jpg");
        setIconImage(icon);



        /*Title*/
        title=new JLabel("Rachunek maklerski (Historia)",SwingConstants.CENTER);
        title.setBounds(FRAME_WIDTH/2-100,0,200,50);
        add(title);


        /*Creating Panels*/
        createNamePanel();
        createDataPanel();
        createSumPanel(sold);
        updateShares(sold);
        back=new JButton("Wróć do główego okna");
        back.addActionListener(this);
        back.setBounds(FRAME_WIDTH/2-100,430,200,50);
        add(back);

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            readHistory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        if(source==back){
            new MainWin(shares);
            setVisible(false);
        }
    }

    private void createNamePanel() {
        /*Categories Panel*/
        cat=new JPanel(new GridLayout(1,10));
        cat.setBounds(50,50,FRAME_WIDTH-100,30);
        cat.setBackground(Color.lightGray);
        cat.setBorder(border);
        add(cat);
        cat.add(new JLabel("ID",SwingConstants.CENTER));
        cat.add(new JLabel("Ilość",SwingConstants.CENTER));
        cat.add(new JLabel("Cena kupna [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Wkład [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Cena sprzedaży [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Wartość [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Zysk [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Procent zysku",SwingConstants.CENTER));
        cat.add(new JLabel("Data kupna",SwingConstants.CENTER));
        cat.add(new JLabel("Data sprzedaży",SwingConstants.CENTER));
    }

    private void createDataPanel() {
        cont=new JPanel(new GridLayout(rows,7));
        cont.setBounds(50,80,FRAME_WIDTH-100,300);
        cont.setBackground(Color.lightGray);
        cont.setBorder(border);
        add(cont);
    }

    private void createSumPanel(List<Share> shares) {
        sum=new JPanel(new GridLayout(rows,10));
        sum.setBounds(50,380,FRAME_WIDTH-100,30);
        sum.setBackground(Color.lightGray);
        sum.setBorder(border);
        add(sum);
        sum.add(new JLabel("",SwingConstants.CENTER));
        sum.add(new JLabel(String.valueOf(Share.sumAmount(shares)),SwingConstants.CENTER));
        sum.add(new JLabel("",SwingConstants.CENTER));
        sum.add(new JLabel(String.valueOf(Sys.myRound(Share.sumTotalPrice(shares))),SwingConstants.CENTER));
        jSumValue = new JLabel("",SwingConstants.CENTER);
        sum.add(jSumValue);
        jSumTotalValue= new JLabel("",SwingConstants.CENTER);
        sum.add(jSumTotalValue);
        jSumProfit = new JLabel("",SwingConstants.CENTER);
        sum.add(jSumProfit);
        jPercent = new JLabel("",SwingConstants.CENTER);
        sum.add(jPercent);
        sum.add(new JLabel("",SwingConstants.CENTER));
        sum.add(new JLabel("",SwingConstants.CENTER));
    }

    private List<Share> readHistory() throws FileNotFoundException {
        List<Share> sold=new ArrayList<>();
        Scanner readB=new Scanner(new File(Sys.SOLDPATH));
        while(readB.hasNext()){
            String id = readB.next();
            int amount = readB.nextInt();
            double price = Double.parseDouble(readB.next());
            double value = Double.parseDouble(readB.next());
            String date1=readB.next();
            String date2=readB.next();
            sold.add(new Share(id,amount,price,value,date1,date2));
        }
        return sold;
    }

    public void addJ(int rows,List<Share> sold,int i)
    {
        labels.add(new JLabel(sold.get(i).name,SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(sold.get(i).amount),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(Sys.myRound(sold.get(i).price)),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).price))),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(Sys.myRound(sold.get(i).value)),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).value))),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).value))-Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).price))),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(String.valueOf(Sys.myRound((Share.TotalPrice(sold.get(i).amount,sold.get(i).value)-Share.TotalPrice(sold.get(i).amount,sold.get(i).price))/Share.TotalPrice(sold.get(i).amount,sold.get(i).price)*100)),SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(sold.get(i).date1,SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));
        labels.add(new JLabel(sold.get(i).date2,SwingConstants.CENTER));
        cont.add(labels.get(labels.size()-1));

        sTotalValue+=Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).value));
        jSumTotalValue.setText(String.valueOf(Sys.myRound(sTotalValue)));

        sTotalProfit+=Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).value))-Sys.myRound(Share.TotalPrice(sold.get(i).amount,sold.get(i).price));
        jSumProfit.setText(String.valueOf(Sys.myRound(sTotalProfit)));

        double sPercent = Sys.myRound(sTotalProfit / Sys.myRound(Share.sumTotalPrice(sold)) * 100);
        jPercent.setText(String.valueOf(sPercent));
        cont.setLayout(new GridLayout(rows,10));
        revalidate();
    }

    private void updateShares(List<Share> shares) {
        //Showing existing shares//
        for(int i=0;i<shares.size();i++)
        {
            addJ(rows++,shares,i);
        }
    }

}
