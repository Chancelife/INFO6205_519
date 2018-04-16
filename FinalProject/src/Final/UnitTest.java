package Final;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("ALL")
public class UnitTest {

	@Test
	public void mt_ConTest1() throws Exception {
		MagicTower mt1 = new MagicTower();
		// bfs test
		assertEquals(9, mt1.getMap()[1][3]);
		assertEquals(46, mt1.getTrace()[18][18].disFromKey);
		assertEquals(25, mt1.getTrace()[3][18].disFromGate);
	}
	
	@Test
	public void mt_MoveTest() throws Exception {
		MagicTower mt2 = new MagicTower();
		mt2.getStatus(0, -1);
		mt2.getStatus(0, -1);
		mt2.getStatus(0, -1);
		mt2.getStatus(0, -1);
		mt2.getStatus(0, -1);
		assertEquals(18, mt2.getX());
		assertEquals(13, mt2.getY());
		mt2.getStatus(-1, 0);
		mt2.getStatus(-1, 0);
		mt2.getStatus(0, -1);
		// move position test
		assertEquals(16, mt2.getX());
		assertEquals(12, mt2.getY());
	}
	
	@Test
	public void mt_StatusTest() throws Exception {
		MagicTower mt3 = new MagicTower();
		mt3.getStatus(0, -1);
		mt3.getStatus(0, -1);
		mt3.getStatus(0, -1);
		mt3.getStatus(0, -1);
		mt3.getStatus(0, -1);
		assertEquals(18, mt3.getX());
		assertEquals(13, mt3.getY());
		mt3.getStatus(-1, 0);
		mt3.getStatus(-1, 0);
		mt3.getStatus(0, -1);
		// death test, return -1 when walk into fire
		assertEquals(-1, mt3.getStatus(-1, 0));
		mt3.getStatus(-1, 0); 
		// although this hero is died, fire is not walkable, so he will stay at position X=16
		assertEquals(16, mt3.getX());
	}
	
	@Test
	public void generic_Test() throws Exception {
		Generic generic = new Generic();
		assertNotNull(generic);
		assertEquals(150, generic.getGenearr().length);
	}
	
	@Test
	public void generic_CompareTest() throws Exception {
		Generic generic1 = new Generic();
		generic1.setScore(1000.00);
		Generic generic2 = new Generic();
		generic2.setScore(888.88);
		assertEquals(-1, generic1.compareTo(generic2));
	}
	
	@Test
	public void generic_Test2() throws Exception {
		Gene[] genes = new Gene[150];
		for (int i = 0; i < 150; i++) {
			genes[i] = new Gene(2);
		}
		Generic generic1 = new Generic();
		generic1.setGenearr(genes);
		generic1.setDiedStep(150);
		Generic generic2 = new Generic();
		generic2.setDiedStep(150);
		generic2.setGenearr(genes);
		
		assertEquals(2, generic1.getGenearr()[20].getGene());
		assertEquals(2, generic2.getGenearr()[20].getGene());
	}
	
	@Test
	public void generic_CrossOverTest() throws Exception {
		Gene[] genes = new Gene[150];
		for (int i = 0; i < 150; i++) {
			genes[i] = new Gene(2);
		}
		Generic generic1 = new Generic();
		generic1.setGenearr(genes);
		generic1.setDiedStep(150);
		Generic generic2 = new Generic();
		generic2.setDiedStep(150);
		generic2.setGenearr(genes);
		
		assertEquals(2, generic1.getGenearr()[20].getGene());
		assertEquals(2, generic2.getGenearr()[20].getGene());
		Generic offspring = new Generic(generic1, generic2);
		assertEquals(2, offspring.getGenearr()[20].getGene());
	}
    
	@Test
	public void gene_Test() throws Exception {
		Gene gene = new Gene();
		assertEquals(2, gene.UP);
		assertEquals(3, gene.RIGHT);
	}
}
