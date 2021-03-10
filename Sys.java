import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Sys {
    final static String READPATH="data.txt";
    final static String HISTORYPATH="history.txt";
    final static String SOLDPATH="sold.txt";

    public static void main(String[] args) {
        List<Share> shares=new ArrayList<>();

        try {
            shares=read();
        }
        catch (FileNotFoundException fine)
        {
            //Nothing to see here
        }

        new MainWin(shares);
    }

    //Reading shares from txt file//
    public static List<Share> read() throws FileNotFoundException {
        List<Share> shares=new ArrayList<>();
        Scanner readB = new Scanner(new File(READPATH));

        while(readB.hasNext()){
            String id = readB.next();
            int amount = Integer.parseInt(readB.next());
            double price = Double.parseDouble(readB.next());
            String link = readB.next();
            String date=readB.nextLine();
            shares.add(new Share(id, amount, price, link,date));
        }
        return shares;
    }



    //Saving shares into txt file//
    public static void save(List<Share> shares) throws IOException {
        FileWriter file = new FileWriter(READPATH, false);
        BufferedWriter out = new BufferedWriter(file);
        for (Share share : shares) {
            out.write(share.name + " " + share.amount + " " + share.price + " " + share.link + " " + share.date);
            out.newLine();
        }
        out.close();
    }

    public static void saveHistory(List<Share> shares,int i,String value,String totalValue,String profit,String date) throws IOException {
            FileWriter file = new FileWriter(HISTORYPATH, true);
            BufferedWriter out = new BufferedWriter(file);
            out.write(shares.get(i).name + " " + shares.get(i).amount + " " + shares.get(i).price + " "+value+" "+totalValue+" "+profit+" "+date);
            out.newLine();
            out.close();
    }

    public static void saveSold(List<Share> shares,int i,String amount,String value,String date) throws IOException {
        FileWriter file = new FileWriter(SOLDPATH, true);
        BufferedWriter out = new BufferedWriter(file);
        out.write(shares.get(i).name + " " + amount + " " + shares.get(i).price + " "+value+" "+" "+ (Integer.parseInt(amount) * Double.parseDouble(value) - Share.TotalPrice(Integer.parseInt(amount), shares.get(i).price)) +" "+shares.get(i).date+" "+date);
        out.newLine();
        out.close();
    }

    /*Checking is data actual*/
    public static boolean isNew() throws IOException {
        Scanner readB = new Scanner(new File(HISTORYPATH));
        String id="";
        while(readB.hasNext()) {
             id = readB.next();
        }

        return !id.equals(String.valueOf(LocalDate.now()));
    }

    /*Data approximation*/
    public static double myRound(double a)
    {
        a*=100;
        a=Math.round(a);
        a/=100;
        return a;
    }
}