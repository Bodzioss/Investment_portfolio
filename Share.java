import java.util.List;

public class Share {
    String name,link;
    double price;
    int amount;

    Share(String name,int amount,double price,String link)
    {
        this.name=name;
        this.price=price;
        this.amount=amount;
        this.link=link;

    }

    public static void addShare(List<Share> shares,String name,int amount,double price,String link) throws NumberFormatException
    {
        shares.add(new Share(name,amount,price,link));
    }

    public static double TotalPrice(int amount,double price)
    {
        return price*amount;
    }
    public static double TotalValue(int amount,double value)
    {
        return value*amount;
    }

    public static Double sumPrice(List<Share> shares) {
        double sum=0;
        for(int i=0;i<shares.size();i++)
        {
            sum+=shares.get(i).price;
        }
        return sum;
    }
    public static Double sumTotalPrice(List<Share> shares) {
        double sum=0;
        for(int i=0;i<shares.size();i++)
        {
            sum+=shares.get(i).price*shares.get(i).amount;
        }
        return sum;
    }


    public static int sumAmount(List<Share> shares) {
        int sum=0;
        for(int i=0;i<shares.size();i++)
        {
            sum+=shares.get(i).amount;
        }
        return sum;
    }
}
