package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;

import main.Extractor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseManager;

public class FeatureValueTest {

	private static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			conn = DatabaseManager.getTestConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (conn != null) {
			conn.close();
		}
	}

	@Before
	public void setUp() throws Exception {
		//        TestHelper.handleSetUpOperation();
	}

	@After
	public void tearDown() throws Exception {
		//        TestHelper.cleanDatabase();
	}

	@Test
	public void testInitalFeatureValue(){
		Extractor ext = new Extractor("eclipse", false, false, false, false, false, false, false);
		String featurevalue = ext.fv.getMetaValue("489", "512");
		assertEquals(",1,hour17,Tuesday,0,499,46,M,1928,1,1", featurevalue);
		featurevalue = ext.fv.getMetaValue("677","2751");
		assertEquals(",1,hour19,Wednesday,39,689,55,A,129,129,0",featurevalue);
	}

	@Test
	public void testComplexityFeatureValue(){
		Extractor ext = new Extractor("eclipse", true, false, false, false, false, false, false);
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String featurevalue = ext.fv.getComplexityValue(ext.complexities, ext.complexities_pre, "489", "512");
		assertEquals(",3,0,3,-3,3,-3,2,-2,17,-17,1,-1,14,-14,1,-1,1,-1,4,-4,1,-1,84,-84,80,-80,1,-1,84,-84,2,-2,6,-6,20,-20,56,-56,1927,-1927,230,-230,1276,-1276,379,-379,826,-826,480,-480,577,-577,853,-853,320,-320,533,-533,21,-21,17,-17,23,-23,6,-6,0.38,-0.38,291,-291,281,-281,314,-314,170,-170", featurevalue);
		featurevalue = ext.fv.getComplexityValue(ext.complexities, ext.complexities_pre, "677","2751");
		assertEquals(",2,-2,2,-2,2,-2,1,-1,9,-9,0,0,9,-9,0,0,1,-1,0,0,0,0,10,-10,10,-10,4,-4,10,-10,0,0,1,-1,0,0,9,-9,128,-128,18,-18,98,-98,32,-32,61,-61,16,-16,65,-65,89,-89,34,-34,55,-55,4,-4,4,-4,4,-4,2,-2,0.16,-0.16,24,-24,24,-24,24,-24,18,-18",featurevalue);
	}
	
	@Test
	public void testMessageFeatureValue(){
		Extractor ext = new Extractor("eclipse", false, false, false, false, true, false, false);
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String featurevalue = ext.mbf.getBowFeatureValue(ext.mbf.maplist.get(0));
		assertEquals(",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", featurevalue);
		featurevalue = ext.mbf.getBowFeatureValue(ext.mbf.maplist.get(548));
		assertEquals(",1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",featurevalue);
	}
	
	@Test
	public void testPathFeatureValue(){
		Extractor ext = new Extractor("eclipse", false, false, false, false, false, true, false);
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String featurevalue = ext.pbf.getBowFeatureValue(ext.pbf.maplist.get(0));
		assertEquals(",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", featurevalue);
		featurevalue = ext.pbf.getBowFeatureValue(ext.pbf.maplist.get(548));
		assertEquals(",0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",featurevalue);
	}
	
	@Test
	public void testAddFeatureValue(){
		Extractor ext = new Extractor("eclipse", false, false, true, false, false, false, false);
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String featurevalue = ext.abf.getBowFeatureValue(ext.abf.maplist.get(0));
		assertEquals(",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,0,0,0,1,0,1,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0", featurevalue.substring(0, 100));
		featurevalue = ext.abf.getBowFeatureValue(ext.abf.maplist.get(548));
		assertEquals("0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,",featurevalue.substring(2530, 2630));
	}
	
	@Test
	public void testDelFeatureValue(){
		Extractor ext = new Extractor("eclipse", false, false, false, true, false, false, false);
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String featurevalue = ext.dbf.getBowFeatureValue(ext.dbf.maplist.get(0));
		assertEquals(",0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", featurevalue.substring(1150, 1200));
		featurevalue = ext.dbf.getBowFeatureValue(ext.dbf.maplist.get(548));
		assertFalse(featurevalue.contains("1"));;
	}
	
	@Test
	public void testSrcFeatureValue(){
		Extractor ext = new Extractor("eclipse", false, true, false, false, false, false, false);
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String featurevalue = ext.sbf.getBowFeatureValue(ext.sbf.maplist.get(0));
		assertEquals(27626, featurevalue.length());
		featurevalue = ext.sbf.getBowFeatureValue(ext.sbf.maplist.get(548));
		System.out.println(featurevalue.length());
		assertEquals(27501, featurevalue.length());
	}
}
