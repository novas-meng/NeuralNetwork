import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by novas on 2016/5/7.
 */
public class train {
    /*
    xorexample 函数功能介绍
      public void train(String snetname,String filename,double Rate,double Momentum,int datanumber,int traincount,int datacolumnnumber,int inputLinearNumber,int hiddenLinearNumber,int outputLinearNumber)
      参数解释
      snetname snet文件名称

      filename 训练数据所在文件名称

      Rate 神经网络学习率

      Momentum 冲量

      datanumber 训练数据的样本数

      traincount 迭代次数

      datacolumnnumber 样本的总维数（包括输出）

      int inputLinearNumber,int hiddenLinearNumber,int outputLinearNumber

      表示输入层，隐藏层，输出层个数

     */
    public static void test(String testfilename,XorExample xorExample)throws IOException
    {
        BufferedReader bufferedReader=new BufferedReader(new FileReader(testfilename));
        String line=bufferedReader.readLine();
        ArrayList<double[]> list=new ArrayList<double[]>();
        while (line!=null)
        {
            String[] var=line.split(" ");
            double[] var1=new double[var.length];
            for(int i=0;i<var1.length;i++)
            {
                var1[i]=Double.parseDouble(var[i]);
            }
            list.add(var1);
            line=bufferedReader.readLine();
        }
        bufferedReader.close();
        for(int i=0;i<list.size();i++)
        {
            double res=xorExample.startPredict("train.snet",list.get(i),900);
            System.out.println(res);
        }
    }

    public static void main(String[] args)throws Exception
    {

        XorExample xorExample=new XorExample();
       // xorExample.train("train.snet", "train", 0.8,0.3,164,20000,5,4,8,1);
        test("test",xorExample);
      }
}
