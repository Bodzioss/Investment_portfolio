import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sys {

    public static void main(String[] args) {
        List<Share> shares=new ArrayList<>();

        try {
            shares=read();
        }
        catch (FileNotFoundException fine)
        {
            //Nothing to show here
        }
        MainWin mainWin=new MainWin(shares);
    }

    //Reading shares from txt file//
    public static List<Share> read() throws FileNotFoundException {
        List<Share> shares=new ArrayList<>();
        Scanner readB = new Scanner(new File("file.txt"));

        while(readB.hasNext()) {
            String id = readB.next();
            int amount = Integer.parseInt(readB.next());
            double price = Double.parseDouble(readB.next());
            String link = readB.next();
            shares.add(new Share(id, amount, price, link));
        }

        return shares;
    }

    //Saving shares into txt file//
    public static List<Share> save(List<Share> shares) throws IOException {

        for(int i=0;i<shares.size();i++)
        {
            FileWriter file=new FileWriter("file.txt",true);
            BufferedWriter out = new BufferedWriter(file);
            out.write(shares.get(i).name+" "+shares.get(i).amount+" "+shares.get(i).price+" "+shares.get(i).link);
            out.newLine();
            out.close();
        }
        return shares;
    }
}
