/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.db;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class DBTest {

    private static DB db;

    public DBTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        db = MysqlDB.getInstance();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testInvalidateCachedQueries() throws Exception {
        QuerySet qs = new QuerySet();
        qs.select("1+1");
        qs.from("DUAL");
        qs.limit(1);
        db.getList(qs);
        String key = qs.compileSelect() + qs.getData().toString();
        Assert.assertNotNull(db.getCache().get(key));
        for (int i = 0; i < db.getCache().getCacheConfiguration().getMaxElementsInMemory()+100; i++) {
            QuerySet qs2 = new QuerySet();
            qs2 = new QuerySet();
            qs2.select("1+"+i);
            qs2.from("DUAL");
            qs2.limit(1);
            db.getList(qs2);
        }
        qs = new QuerySet();
        qs.update("DUAL");
        qs.set("1", "1");
        db.invalidateCachedQueries(qs);
        Assert.assertNull(db.getCache().get(key));
    }

    @Test
    public void testStoreCacheKeys() throws Exception {
    }
}