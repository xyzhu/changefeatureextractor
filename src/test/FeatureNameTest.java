package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;

import main.Extractor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseManager;

public class FeatureNameTest {

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
	public void testInitialFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750",false, false, false, false, false, false, false);
		String featurename = ext.extractFeatureName();
		assertEquals("commit,file,author,hour,day,loglen,changecount,bugcount,type,newloc,addloc,deleteloc", featurename);
	}

	@Test 
	public void testComplexityFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750", true, false, false, false, false, false, false);
		Extractor.featureno = 0;
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String complexityname = ext.fn.getComplexityName(ext.complexities, ext.complexities_pre);
		assertEquals(",AvgCyclomatic,AvgCyclomatic_delta,AvgCyclomaticModified,AvgCyclomaticModified_delta,AvgCyclomaticStrict,AvgCyclomaticStrict_delta,AvgEssential,AvgEssential_delta,AvgLine,AvgLine_delta,AvgLineBlank,AvgLineBlank_delta,AvgLineCode,AvgLineCode_delta,AvgLineComment,AvgLineComment_delta,CountDeclClass,CountDeclClass_delta,CountDeclClassMethod,CountDeclClassMethod_delta,CountDeclClassVariable,CountDeclClassVariable_delta,CountDeclFunction,CountDeclFunction_delta,CountDeclInstanceMethod,CountDeclInstanceMethod_delta,CountDeclInstanceVariable,CountDeclInstanceVariable_delta,CountDeclMethod,CountDeclMethod_delta,CountDeclMethodDefault,CountDeclMethodDefault_delta,CountDeclMethodPrivate,CountDeclMethodPrivate_delta,CountDeclMethodProtected,CountDeclMethodProtected_delta,CountDeclMethodPublic,CountDeclMethodPublic_delta,CountLine,CountLine_delta,CountLineBlank,CountLineBlank_delta,CountLineCode,CountLineCode_delta,CountLineCodeDecl,CountLineCodeDecl_delta,CountLineCodeExe,CountLineCodeExe_delta,CountLineComment,CountLineComment_delta,CountSemicolon,CountSemicolon_delta,CountStmt,CountStmt_delta,CountStmtDecl,CountStmtDecl_delta,CountStmtExe,CountStmtExe_delta,MaxCyclomatic,MaxCyclomatic_delta,MaxCyclomaticModified,MaxCyclomaticModified_delta,MaxCyclomaticStrict,MaxCyclomaticStrict_delta,MaxNesting,MaxNesting_delta,RatioCommentToCode,RatioCommentToCode_delta,SumCyclomatic,SumCyclomatic_delta,SumCyclomaticModified,SumCyclomaticModified_delta,SumCyclomaticStrict,SumCyclomaticStrict_delta,SumEssential,SumEssential_delta", complexityname);
	}

	@Test
	public void testMessageBowFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750", false, false, false, false, true, false, false);
		Extractor.featureno = 0;
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ext.fn.getBowFeatureName(ext.mbf.bowmaps);
		assertEquals(999, Extractor.featureno);
	}

	@Test
	public void testPathBowFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750", false, false, false, false, false, true, false);
		Extractor.featureno = 0;
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ext.fn.getBowFeatureName(ext.pbf.bowmaps);
		assertEquals(263, Extractor.featureno);
	}

	@Test
	public void testAddBowFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750", false, false, true, false, false, false, false);
		Extractor.featureno = 0;
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ext.fn.getBowFeatureName(ext.abf.bowmaps);
		assertEquals(5546, Extractor.featureno);
	}

	@Test
	public void testDelBowFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750", false, false, false, true, false, false, false);
		Extractor.featureno = 0;
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ext.fn.getBowFeatureName(ext.dbf.bowmaps);
		assertEquals(4070, Extractor.featureno);
	}

	@Test
	public void testSrcBowFeatureName(){
		Extractor ext = new Extractor("voldemort", "500-750", false, true, false, false, false, false, false);
		Extractor.featureno = 0;
		try {
			ext.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ext.fn.getBowFeatureName(ext.sbf.bowmaps);
		assertEquals(8104, Extractor.featureno);
	}
}
