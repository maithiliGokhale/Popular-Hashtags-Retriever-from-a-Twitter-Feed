import java.io.*;
import java.security.KeyStore;
import java.util.*;

public class HashtagCounter {

    public static void main(String[] args) {
     

        FileInputStream in =null;
        BufferedWriter buffWriter =null;
        BufferedReader buffReader = null;
        FileWriter fileWriter= null;

        String line = new String();
        Hashtable<String, FibonacciHeap.Node> table = new Hashtable();
        FibonacciHeap maxFiboHeap = new FibonacciHeap();
        try{
            //fileWriter=new FileWriter("D:/Fall 2016/Advanced Data Structures/Project/InputOutput/myOutput_sampleInput_Million.txt");
            //in = new FileInputStream("D:/Fall 2016/Advanced Data Structures/Project/InputOutput/sampleInput_Million.txt");
            fileWriter=new FileWriter(args[1]);
            		in = new FileInputStream(args[0]);
            buffWriter= new BufferedWriter(fileWriter);
            buffReader = new BufferedReader(new InputStreamReader(in));
            while ((line = buffReader.readLine()) != null) {
            	if(line.equals("STOP"))
                {
                    System.out.println("DONE...");
            		return;
                }
            	
            	if(line.charAt(0)!='#') //if the first character is not a hashtag, then it has to be a number
                {
                    int countMax = Integer.parseInt(line);
                    if(countMax>maxFiboHeap.totalNodes)
                    	countMax = maxFiboHeap.totalNodes;
                    ArrayList<FibonacciHeap.Node> nodeList = new ArrayList<>();
                    //System.out.println("PRINTING TOP  " + countMax);
                    
                    for(int i=0; i< countMax; i++ )
                    {
                        
                        FibonacciHeap.Node node = maxFiboHeap.removeMax();
                        String str  = new String();
                        //extracting the key for the particular node
                        for(Map.Entry<String, FibonacciHeap.Node> e: table.entrySet())  
                        {
                            if(e.getValue()==node) {
                                str = e.getKey();
                                buffWriter.write(e.getKey()+""+node.frequency);
                                break;

                            }
                        }
                        if(i<countMax-1)
                        buffWriter.write(',');
                        nodeList.add(node);
                        //System.out.println("Frequency : "+ nodeList.get(i).frequency + "  Word:  "+ str + " Degree :  "+ nodeList.get(i).degree);
                    }
                    buffWriter.write('\n');
                    for(int i=0; i<countMax; i++ )
                    {
                        //System.out.println("Frequency : "+ nodeList.get(i).frequency + "  Word:  "+  " Degree :  "+ nodeList.get(i).degree);
                        //nodeList.get(i).degree = 0;
                        maxFiboHeap.insert(nodeList.get(i));
                        //System.out.println("Frequency : "+ nodeList.get(i).frequency + "  Word:  "+  " Degree :  "+ nodeList.get(i).degree);
                        //System.out.println("Total number of nodes : " +maxFiboHeap.totalNodes);
                    }
                    
                    continue;
                }
                //splitting the line on the basis of space, the first string will be the hashtagged word, and the second will be the number
                String [] input = line.split(" ");
                //the words will be the the string without the #, hence we take the substring from 1st index
                String word = input[0].substring(1);
                int frequency = Integer.parseInt(input[1]);
                //System.out.println(Word +" : " +input[1]);
                if(table.containsKey(word))
                {
                    
                    FibonacciHeap.Node n = table.get(word);
                    maxFiboHeap.increaseKey(n,frequency+n.frequency);
                    //System.out.println("Increased frequency : "+ n.frequency + "  Word:  "+ word + " Degree :  "+ n.degree);
                }
                else{
                    FibonacciHeap.Node n1 =  maxFiboHeap.insert(new FibonacciHeap.Node(frequency));
                    table.put(word,n1);
                    //System.out.println("Inserted frequency : "+ n1.frequency + "  Word:  "+ word+ " Degree :  "+ n1.degree);
                }

            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                in.close();
                buffReader.close();
                buffWriter.close();

            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }


    }
    
}
