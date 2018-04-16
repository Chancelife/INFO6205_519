package Final;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class Demo {
	//public  static int  GENERATION = 0;
	public static final int MAXGENERATION = 20000;
	public static final int genericInGroup = 100;
	private static Generic bestOfGeneration;
	static PriorityBlockingQueue<Generic> pq = new PriorityBlockingQueue<>();
	public static int GENERATION=0;
	public volatile double hightestScore = 0.00;
	public class OddGeneTask implements Runnable{
		
		private ArrayList<Generic> gs;
		public OddGeneTask() {
			
		}
		@Override
		public void run() {
			for(int i =0;i<gs.size();i+=2) {
				gs.get(i).Move();
				pq.add(gs.get(i));
			}
		}
	}
	
	public class EvenGeneTask implements Runnable{
		private ArrayList<Generic> gs;
		public EvenGeneTask() {
			
		}
		@Override
		public void run() {
			for(int j =1;j<gs.size();j+=2) {
				gs.get(j).Move();
				pq.add(gs.get(j));
			}		
		}
	}

	public class GeneTask implements Runnable{
		
		int lo;
		int hi;
		public GeneTask(int i, int j) {
			lo=i;
			hi=j;
		}
		@Override
		public void run() {
			while(lo<hi) {
				Generic generic = new Generic();
				Gene[] genes = new Gene[150];
				// System.out.println("No."+i+" status:");
				for(int j=0;j<generic.getGenearr().length;j++) {
					genes[j] = new Gene();
				}
				generic.setGenearr(genes);
				generic.Mutate();
				generic.Move();
				pq.add(generic);
				lo++;
			}
			GENERATION += 1;

		do {
			bestOfGeneration = pq.peek();
//			System.out.println("Generation "+GENERATION+", Position: "
//				+bestOfGeneration.mt.getX()+","+bestOfGeneration.mt.getY()
//				+" Status:"+bestOfGeneration.getState() + " Score:"+bestOfGeneration.getScore()+" GetKey:"+bestOfGeneration.mt.getKey());
			
			if(bestOfGeneration!=null) {
				if(hightestScore < bestOfGeneration.getScore()) {
					hightestScore = bestOfGeneration.getScore();
					doLog(bestOfGeneration, "utf-8");
				}
				System.out.print("Thread: "+Thread.currentThread().getName());
				System.out.printf(" Generation %5d, Position: %2d,%2d Status:%3d Score:%7.2f FoundGate:%b GetKey:%b OpenGate:%b\n",
						GENERATION, bestOfGeneration.mt.getX(), bestOfGeneration.mt.getY(), bestOfGeneration.getState(), bestOfGeneration.getScore(), 
						bestOfGeneration.mt.foundGate(), bestOfGeneration.mt.getKey(), bestOfGeneration.mt.openGate());
				doNextGen();	
			}else
				break;
			
			
		}while(bestOfGeneration.getState()!=3);
	}
}

	public static void main(String[] args) { 
		ExecutorService e = Executors.newFixedThreadPool(2);
		Demo d = new Demo();
		GeneTask task1 =  d.new GeneTask(0,100);
		//task1.name="ThreadA....";
		GeneTask task2 =  d.new GeneTask(0,100);
		//task2.name="ThreadB....";
		e.execute(task1);
		e.execute(task2);
		e.shutdown();
		e.shutdownNow();
	}
	
	public static void doNextGen() {
		ArrayList<Generic> parents = new ArrayList<>();
		//ArrayList<Generic> children = new ArrayList<>();
		Generic first = pq.peek();
		for(int i=0;i<5;i++) {
			try {
				parents.add(pq.poll());
			}catch(Exception e) {
				break;
			}
			
		}
		//pq.clear();
		pq.add(first);
//		System.out.println(first.getScore());
		for(Generic f:parents) { 
			for(Generic m:parents) {
				 for(int j=0;j<4;j++) {
					 try {
						 Generic generic = new Generic(f, m);
							generic.Mutate();
							//children.add(generic);
							generic.Move();
							pq.add(generic);
					 }catch(Exception e) {
							break;
						}
					
				 }
			}
		}
		GENERATION+=1;
		
	}
	
	public static void doLog(Generic generic, String code) {
		String outputfile = ".\\history\\"+GENERATION+".txt";
		OutputStreamWriter osw = null;
		String result = "";
		for(Gene g: generic.getGenearr()) {
			result += g.getGene()+",";
			
		}
		try {
			File file = new File(outputfile);
			if(!file.exists()){
				file = new File(file.getParent());
				if(!file.exists()){
					file.mkdirs();
				}
			}
			if("asci".equals(code)){
				code = "utf-8";
			}
			osw = new OutputStreamWriter(new FileOutputStream(outputfile),code);
			osw.write(result);
			osw.write("\n");
			osw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(osw != null){
					osw.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
