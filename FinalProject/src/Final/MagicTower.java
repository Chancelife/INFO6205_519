package Final;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.management.Query;
import javax.sound.midi.Track;

public class MagicTower {
	class Position {
		int x;
		int y;
		boolean walkable;
		Position pathFromGate;
		Position pathFromKey;
		Position pathFromEnd;
		int disFromGate;
		int disFromKey;
		int disFromEnd;
		
		Position(int x, int y, boolean walkable) {
			this.x = x;
			this.y = y;
			this.walkable = walkable;	// position can be walked through, not a Wall nor FIRE
			this.pathFromGate = null;		// previous node from Gate
			this.pathFromKey = null;		// previous node from Key
			this.pathFromEnd = null;		// previous node from End
			this.disFromGate = 0;
			this.disFromKey = 0;
			this.disFromEnd = 0;
		}
	}
	
	public static final short P = 0; //	PATH
	public static final short W = 1; 
	public static final short F = 3; // FIRE
	public static final short C = 5; // CHECKPOINT
	public static final short G = 6; // GATE
	public static final short K = 7; // KEY
	public static final short S = 8; // START
	public static final short D = 9; // END
	public static final int MAZESIZE = 20;
	
	private int X;
	private int Y;
	private int step;
	private boolean hasKey = false;
	private boolean foundGate = false;
	private boolean openGate = false;
	private boolean success = false;
	
	private int CHECKPOINTS = 0;
	
	// no checkpoints, 5 is useless
	private static short[][] MAP 
	= {{W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
	   {W,0,F,D,F,0,F,0,F,K,W,0,0,0,0,0,W,F,F,W},
	   {W,0,F,0,W,0,F,0,W,0,W,0,W,W,W,0,W,F,F,W},
	   {W,0,F,6,F,0,F,0,F,0,W,0,0,0,W,0,W,0,0,W},
	   {W,0,W,0,0,0,0,0,0,0,W,0,W,0,W,0,0,W,0,W},
	   {W,0,W,W,W,W,5,W,W,W,W,0,W,0,W,0,0,W,0,W},
	   {W,0,0,W,0,0,0,0,0,0,0,0,W,0,W,W,0,W,0,W},
	   {W,W,0,0,0,F,F,0,F,F,0,0,W,W,W,F,0,F,0,W},
	   {W,0,0,W,0,F,F,0,F,F,0,0,0,0,0,0,0,0,0,W},
	   {W,W,W,W,W,W,W,0,W,W,0,0,W,W,0,W,W,W,W,W},
	   {W,0,0,0,0,0,0,0,W,W,0,W,W,0,0,0,0,0,0,W},
	   {W,W,5,W,W,W,W,W,0,0,0,0,0,W,W,W,0,W,0,W},
	   {W,0,0,0,0,0,0,W,0,0,0,0,0,W,0,W,0,W,0,W},
	   {W,0,W,0,W,W,0,W,0,0,0,0,0,W,0,W,0,W,0,W},
	   {W,0,W,0,W,0,0,W,W,W,W,W,W,W,0,W,0,0,0,W},
	   {W,0,F,0,W,0,W,W,W,F,W,W,F,W,0,W,W,W,W,W},
	   {W,0,F,0,W,0,W,0,0,0,0,0,5,0,0,0,0,0,0,W},
	   {W,0,F,0,0,5,W,W,W,5,W,0,W,0,W,W,W,W,0,W},
	   {W,0,0,0,W,0,0,0,0,0,W,0,W,0,0,0,0,0,S,W},
	   {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}};
	
//	private static short[][] MAP 
//	= {{W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
//	   {W,0,F,D,F,0,F,0,F,0,W,0,0,0,0,0,W,F,F,W},
//	   {W,0,F,0,W,0,F,0,W,0,W,0,W,W,W,0,W,F,F,W},
//	   {W,0,F,0,F,0,F,0,F,0,W,0,0,0,W,0,W,0,0,W},
//	   {W,0,W,0,0,0,0,0,0,0,W,0,W,0,W,0,0,W,0,W},
//	   {W,0,W,W,W,W,5,W,W,W,W,0,W,0,W,0,0,W,0,W},
//	   {W,0,0,W,0,0,0,0,0,0,0,0,W,0,W,W,0,W,0,W},
//	   {W,W,0,0,0,F,F,0,F,F,0,0,W,W,W,F,0,F,0,W},
//	   {W,0,0,W,0,F,F,0,F,F,0,0,0,0,0,0,0,0,0,W},
//	   {W,W,W,W,W,W,W,0,W,W,0,0,W,W,0,W,W,W,W,W},
//	   {W,0,0,0,0,0,0,0,W,W,0,W,W,0,0,0,0,0,0,W},
//	   {W,W,5,W,W,W,W,W,0,0,0,0,0,W,W,W,0,W,0,W},
//	   {W,0,0,0,0,0,0,W,0,0,0,0,0,W,0,W,0,W,0,W},
//	   {W,0,W,0,W,W,0,W,0,0,0,0,0,W,0,W,0,W,0,W},
//	   {W,0,W,0,W,0,0,W,W,W,W,W,W,W,0,W,0,0,0,W},
//	   {W,0,F,0,W,0,W,W,W,F,W,W,F,W,0,W,W,W,W,W},
//	   {W,0,F,0,W,0,W,0,0,0,0,0,5,0,0,0,0,0,0,W},
//	   {W,0,F,0,0,5,W,W,W,5,W,0,W,0,W,W,W,W,0,W},
//	   {W,0,0,0,W,0,0,0,0,0,W,0,W,0,0,0,0,0,S,W},
//	   {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W}};
	
	private static Position[][] TRACE;
	
	public int getX() {
		return this.X;
	}
	public int getY() {
		return this.Y;
	}
	public boolean foundGate() {
		return foundGate;
	}
	public boolean openGate() {
		return openGate;
	}
	public boolean getKey() {
		return hasKey;
	}
	public final short[][] getMap() {
		return MAP;
	}
	public final Position[][] getTrace() {
		return TRACE;
	}
	public final int getStep() {
		return step;
	}
	
	public MagicTower() {
		// statr position
		this.X = 18;
		this.Y = 18;
		this.step = 0;
		TRACE = new Position[MAZESIZE][MAZESIZE];
		for(int i=0;i<MAZESIZE;i++) {
			for(int j=0;j<MAZESIZE;j++) {
				if(MAP[i][j]==W||MAP[i][j]==F)
					TRACE[i][j] = new Position(i, j, false);
				else {
					TRACE[i][j] = new Position(i, j, true);
				}
			}
		}
		bfs(1,3,D);
		bfs(1,9,K);
		bfs(3,3,G);
		
//		for(int i=0;i<MAZESIZE;i++) {
//			System.out.println("");
//			for(int j=0;j<MAZESIZE;j++) {
//				System.out.printf("%3d ",TRACE[i][j].disFromGate);
//			}
//		}
	}
	
	/**
	 * 
	 * @param sx
	 * @param sy
	 * @param mark	// 6 GATE, 7 KEY, 9 END
	 * @return
	 */
	private int bfs(int sx, int sy, int mark) {
		int pathLength = 0;
		Queue<Position> q = new LinkedList<>();
		ArrayList<Position> adjP = new ArrayList<>();
		if(mark==6)	TRACE[sx][sy].pathFromGate = TRACE[sx][sy];
		if(mark==7)	TRACE[sx][sy].pathFromKey = TRACE[sx][sy];
		if(mark==9)	TRACE[sx][sy].pathFromEnd = TRACE[sx][sy];
		q.add(TRACE[sx][sy]);
		while(!q.isEmpty()) {
			Position lastP = q.poll();
			adjP = adj(lastP, mark);
			for(Position tempp: adjP) {
				if(mark==6) {
					tempp.pathFromGate = lastP;
					tempp.disFromGate = lastP.disFromGate+1;
				} else if(mark==7) {
					tempp.pathFromKey = lastP;
					tempp.disFromKey = lastP.disFromKey+1;
				} else if(mark==9) {
					tempp.pathFromEnd = lastP;
					tempp.disFromEnd = lastP.disFromEnd+1;
				}
				q.add(tempp);
			}
		}
		return pathLength;
	}
	
//	private boolean adj(int x, int y) {
//		
//		
//		return ;
//	}
	
	private ArrayList<Position> adj(Position p, int mark) {
		ArrayList<Position> positions = new ArrayList<>();
		if(mark == 6) {
			if(TRACE[p.x+1][p.y].walkable && TRACE[p.x+1][p.y].pathFromGate == null) {
				positions.add(TRACE[p.x+1][p.y]);
			}
			if(TRACE[p.x-1][p.y].walkable && TRACE[p.x-1][p.y].pathFromGate == null) {
				positions.add(TRACE[p.x-1][p.y]);
			}
			if(TRACE[p.x][p.y+1].walkable && TRACE[p.x][p.y+1].pathFromGate == null) {
				positions.add(TRACE[p.x][p.y+1]);
			}
			if(TRACE[p.x][p.y-1].walkable && TRACE[p.x][p.y-1].pathFromGate == null) {
				positions.add(TRACE[p.x][p.y-1]);
			}
		} else if(mark == 7) {
			if(TRACE[p.x+1][p.y].walkable && TRACE[p.x+1][p.y].pathFromKey == null) {
				positions.add(TRACE[p.x+1][p.y]);
			}
			if(TRACE[p.x-1][p.y].walkable && TRACE[p.x-1][p.y].pathFromKey == null) {
				positions.add(TRACE[p.x-1][p.y]);
			}
			if(TRACE[p.x][p.y+1].walkable && TRACE[p.x][p.y+1].pathFromKey == null) {
				positions.add(TRACE[p.x][p.y+1]);
			}
			if(TRACE[p.x][p.y-1].walkable && TRACE[p.x][p.y-1].pathFromKey == null) {
				positions.add(TRACE[p.x][p.y-1]);
			}
		} else if(mark == 9) {
			if(TRACE[p.x+1][p.y].walkable && TRACE[p.x+1][p.y].pathFromEnd == null) {
				positions.add(TRACE[p.x+1][p.y]);
			}
			if(TRACE[p.x-1][p.y].walkable && TRACE[p.x-1][p.y].pathFromEnd == null) {
				positions.add(TRACE[p.x-1][p.y]);
			}
			if(TRACE[p.x][p.y+1].walkable && TRACE[p.x][p.y+1].pathFromEnd == null) {
				positions.add(TRACE[p.x][p.y+1]);
			}
			if(TRACE[p.x][p.y-1].walkable && TRACE[p.x][p.y-1].pathFromEnd == null) {
				positions.add(TRACE[p.x][p.y-1]);
			}
		}
		return positions;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return status
	 * return status: 0 alive; -1 died; 1 find gate without key; 2 find key; F complete
	 */
	public int getStatus(int dx, int dy) {
		if(this.X+dx <0 || this.X+dx>=MAZESIZE || this.Y+dy>=MAZESIZE || this.Y+dy<0) {
			return -1;
		}
		if(moveToNext(this.X+dx,this.Y+dy)) {
			this.step++;
			this.X += dx;
			this.Y += dy;
			if(MAP[this.X][this.Y]==0) return 0;
			if(MAP[this.X][this.Y]==F) {
				this.X -= dx;
				this.Y -= dy;
				return -1;
			}
			if(MAP[this.X][this.Y]==5) {
				this.CHECKPOINTS++;
				return 0;
			}
			if(MAP[this.X][this.Y]==6) {
				if(this.hasKey) {
					this.foundGate = true;
					this.openGate = true;
					return 0;
				} else {
					this.X -= dx;
					this.Y -= dy;
					foundGate = true;
					return 1;
				}
			}
			if(MAP[this.X][this.Y]==7)  {
				this.hasKey = true;
				return 2;
			}
			if(MAP[this.X][this.Y]==9) {
				this.success = true;
				return 3; 
			}
		} else {
			return 0;
		}
		
		return 0;
	}
	
	
	private boolean moveToNext(int x, int y) {
		switch (MAP[x][y]) {
			case 0: case F: case 5: case 9:
				return true;
			case 7:
				return true;
			case W:
				return false;
			case 6:
				return true;
		}
		return false;
	}
	
	public double fitnessFunction() {
		double score = 0.00;
		// score += this.CHECKPOINTS * 20;
		score += this.foundGate ? 1000 : 500*(1/(double)(TRACE[this.X][this.Y].disFromGate+1));
		if(this.foundGate) {
			score += this.hasKey ? 1000 : 500*(1/(double)(TRACE[this.X][this.Y].disFromKey+1));
		}
		if(this.hasKey) {
			score += this.openGate ? 1000 : 500*(1/(double)(TRACE[this.X][this.Y].disFromGate+1));
		}
		if(this.openGate) {
			score += this.success ? 1000 : 500*(1/(double)(TRACE[this.X][this.Y].disFromEnd+1));
		}
		return score;
	}
	
//	public static void main(String[] args) {
//		MagicTower mt = new MagicTower();
//	}
}
