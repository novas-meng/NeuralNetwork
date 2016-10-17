import org.joone.engine.*;
import org.joone.engine.learning.*;
import org.joone.io.*;
import org.joone.net.NeuralNet;
import org.joone.net.NeuralNetLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

/**
 * Example: The XOR Problem with JOONE
 *
 * @author Ming Sun
 * @version 1.0
 */
public class XorExample implements NeuralNetListener {

FullSynapse t1,t2;

  /**
   * The train button.
   */


  /**
   * Constructor. Set up the components.
   */
  public XorExample()
  {

  }
  /**
   * Called when the user clicks one of the three
   * buttons.
   * 
   * @param e The event.
   */
  private static final long serialVersionUID = -3472219226214066504L;
  private NeuralNet nnet = null;
  private String NNet_Path = null;
    public String xornetname=null;
    public void Init_BPNN(String NNet_Path,int InputNum,int HiddenNum,int OutputNum) {
      this.NNet_Path = NNet_Path;
      LinearLayer input = new LinearLayer();
      SigmoidLayer hidden = new SigmoidLayer();
      SigmoidLayer output = new SigmoidLayer();
      input.setRows(InputNum);
      hidden.setRows(HiddenNum);
      output.setRows(OutputNum);
      FullSynapse synapse_IH = new FullSynapse();
      FullSynapse synapse_HO = new FullSynapse();
      input.addOutputSynapse(synapse_IH);
      hidden.addInputSynapse(synapse_IH);
      hidden.addOutputSynapse(synapse_HO);
      output.addInputSynapse(synapse_HO);
      nnet = new NeuralNet();
      nnet.addLayer(input, NeuralNet.INPUT_LAYER);
      nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
      nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
  }
  public void Train_BPNN(String TrainFile,int TrainLength,double Rate,double Momentum,int TrainCicles,int numberColumns){
      Layer input = nnet.getInputLayer();
      FileInputSynapse trains = new FileInputSynapse();
      trains.setInputFile(new File(TrainFile));
      StringBuilder sb=new StringBuilder();
      String trainColumnStr=null;
      for(int i=1;i<=numberColumns-1;i++)
      {
          sb.append(i+",");
      }
      if(sb.length()>1)
      {
          trainColumnStr=sb.toString();
          trainColumnStr=trainColumnStr.substring(0,trainColumnStr.length()-1);
        //  System.out.println("选择的列数为  "+trainColumnStr);
      }
      trains.setAdvancedColumnSelector(trainColumnStr);
      Layer output = nnet.getOutputLayer();
      FileInputSynapse target = new FileInputSynapse();
      target.setInputFile(new File(TrainFile));
      target.setAdvancedColumnSelector(numberColumns+"");
      TeachingSynapse trainer = new TeachingSynapse();
      trainer.setDesired(target);
      input.addInputSynapse(trains);
      output.addOutputSynapse(trainer);
      nnet.setTeacher(trainer);
      Monitor monitor = nnet.getMonitor();
      monitor.setLearningRate(Rate);
      monitor.setMomentum(Momentum);
      monitor.addNeuralNetListener(this);
      monitor.setTrainingPatterns(TrainLength);
      monitor.setTotCicles(TrainCicles);
      monitor.setLearning(true);
      nnet.go();

  }
    //开始预测
    public double startPredict(String NNet_Path,double[] inputs,int TestLength){
        NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
        if (testBPNN != null) {

            Layer input = testBPNN.getInputLayer();
            Pattern pattern=new Pattern();
            pattern.setValues(inputs);
            Vector vector1=new Vector();
            vector1.add(pattern);
            FileInputSynapse inputStream = new FileInputSynapse();
            inputStream.setInputPatterns(vector1);
            input.removeAllInputs();
            input.addInputSynapse(inputStream);
            Layer output = testBPNN.getOutputLayer();
            final resultOut fileOutput = new resultOut();
            output.addOutputSynapse(fileOutput);
            Monitor monitor = testBPNN.getMonitor();
            monitor.setTrainingPatterns(TestLength);
            monitor.setTotCicles(1);
            monitor.setLearning(false);
            testBPNN.go(false,true);
            return fileOutput.getResult();
        }
        return -1;
    }

  /**
   * Called when the user clicks the train button.
   */
  public void train(String snetname,String filename,double Rate,double Momentum,int datanumber,int traincount,int datacolumnnumber,int inputLinearNumber,int hiddenLinearNumber,int outputLinearNumber)
  {
	Init_BPNN(snetname,inputLinearNumber,hiddenLinearNumber,outputLinearNumber);
	Train_BPNN(filename,datanumber,Rate ,Momentum ,traincount,datacolumnnumber);
      this.xornetname=snetname;
  }

  /**
   * JOONE Callback: called when the neural network
   * stops. Not used.
   * 
   * @param e The JOONE event
   */
  public void netStopped(NeuralNetEvent e) {
      saveNeuralNet(this.xornetname);
  }

  /**
   * JOONE Callback: called to update the progress
   * of the neural network. Used to update the
   * status line.
   * 
   * @param e The JOONE event
   */
  public void cicleTerminated(NeuralNetEvent e) {
    Monitor mon = (Monitor)e.getSource();
    long c = mon.getCurrentCicle();
    long cl = c / 1000;

  }
  
  public void saveNeuralNet(String fileName) {  
      try {  
          FileOutputStream stream = new FileOutputStream(fileName);  
          ObjectOutputStream out = new ObjectOutputStream(stream);  
          out.writeObject(nnet);
          out.close();  
      } catch (Exception excp) {  
          excp.printStackTrace();  
      }  
  }  
    
  NeuralNet restoreNeuralNet(String fileName) {  
      NeuralNetLoader loader = new NeuralNetLoader(fileName);  
      NeuralNet nnet = loader.getNeuralNet();  
      return nnet;  
      } 

  /**
   * JOONE Callback: Called when the network
   * is starting up. Not used.
   * 
   * @param e The JOONE event.
   */
  public void netStarted(NeuralNetEvent e) {
  }

@Override
public void errorChanged(NeuralNetEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void netStoppedError(NeuralNetEvent arg0, String arg1) {
	// TODO Auto-generated method stub
	
}

}
