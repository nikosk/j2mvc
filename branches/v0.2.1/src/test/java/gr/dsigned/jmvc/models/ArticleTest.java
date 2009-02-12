/*
 *  ArticleTest.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
 * 
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.types.Hmap;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class ArticleTest {

    private static String largeContent = "Goals:\n\nThis is an extremely brief  tutorial on JUnit. The goal is to introduce a new user to basics the JUnit framework.\n\nPrerequisites:\n\nIn order to use this tutorial you will need to have a Java compiler (http://java.sun.com/), Ant (http://ant.apache.org/), and JUnit (http://junit.org/) installed. You will also need JUnit.jar in your Ant's library folder (ANT_HOME/lib).\n\nI've also included a link to the code and Ant scripts needed to run this example.\n\nLet's Test Some Code\n\nFirst, you need a Java class to test. :) Here's a short example.\n\npublic class Math {\n        static public int add(int a, int b) {  \n                return a + b;\n        }\n}\n\nMy apologies for the lack of JavaDocs and comments, but it's a pretty simple bit of code. This class will add two numbers.  \n\nTo test this code, you need a second Java class that will\n\n    1) import junit.framework.*\n    2) extend TestCase.\n\nThat's it!\n\nHere's an example.\n\nimport junit.framework.*;\n\npublic class TestMath extends TestCase {\n\n  public void testAdd() {\n        int num1 = 3;\n        int num2 = 2;\n        int total = 5;\n        int sum = 0;\n        sum = Math.add(num1, num2);\n        assertEquals(sum, total);\n  }\n} \n\n\nThere are two important points to note in the sample. First, the routine is named testAddNumbers. This convention tells you that the routine is supposed to be a test and that it's targetting the \"add\" functionality.\n\nThe last step is how to run your JUnit tests. You can do this several ways, including your command line, Eclipse or the JUnit Test Runner, but I like plain Ant. To run a Junit test with an Ant script, add this to your Ant script:\n\n<junit printsummary=\"yes\" haltonfailure=\"yes\" showoutput=\"yes\" >\n        <classpath>\n                <pathelement path=\"${build}\"/>\n        </classpath>                   \n\n        <batchtest fork=\"yes\" todir=\"${reports}/raw/\">\n                <formatter type=\"xml\"/>\n                <fileset dir=\"${src}\">\n                        <include name=\"**/*Test*.java\"/>\n                </fileset>\n        </batchtest>\n</junit> \n     \nAnt will also do nice things like create nice HTML reports for you! I've linked to a simple Ant script that will compile all the code in your \"src\" folder, run all the tests named \"test*\" and then create an HTML report in your \"reports\\html\" folder. Just type \"ant test\".\n\nI've linked to a zip file (junit-sample.zip) that contains the build.xml file, the source code and the test class. (If you try to run it and get errors about JUnit not being found, remember to add that junit.jar to your Ant lib folder.)\n\nSummary\n\nThat's it! This should be all you need to understand the basics of Junit.\n\nIt a lightweight enough framework that this short introduction should let you do quite a bit. By taking this example you can create a number of basic, automated tests that can be run from your IDE or in a build verification system.\n\nFor more information, see the JUnit articles page or pick up a copy of Pragmatic Unit Testing in Java.\n";

    @Test
    public void countArticlesByCat() {
        try {
            Jmvc $ = new Jmvc();
            Article a = null;
            a = new Article();
            assertTrue((a != null));
            String id = a.insertArticle(getArticle());
            assertTrue(Integer.parseInt(id) > 0);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    public void testConcurrency() {
        for (int i = 0; i < 1; i++) {
            Runnable r = new inserters(i);
            Thread t = new Thread(r);
            t.start();
        }
        for (int i = 0; i < 40; i++) {
            Runnable r = new selectors(i);
            Thread t = new Thread(r);
            t.start();
        }
        long timr = new Date().getTime();
        long span = 1000 * 2;
        boolean run = true;
        while (run) {
            run = timr + span > new Date().getTime();
        }
        selectors.run = false;
        inserters.run = false;
    }

    public static Hmap getArticle() {
        Hmap testArt = new Hmap();
        testArt.put("title", "the title");
        testArt.put("real_title", "Real title");
        testArt.put("sub_title", "Subtitle");
        testArt.put("lead_in", "This is a lead in");
        testArt.put("content", largeContent);
        testArt.put("category_id", "1");
        testArt.put("published", "" + new Timestamp(new java.util.Date().getTime()));
        testArt.put("user_id", "1");
        return testArt;
    }
}

class inserters implements Runnable {

    public static boolean run = true;
    public int insertNum = 0;
    public int threadId = 0;

    public inserters(int id) {
        threadId = id;
    }

    @Override
    public void run() {
        Article a = null;
        try {
            Jmvc $ = new Jmvc();
            a = new Article();
        } catch (Exception e) {
            System.out.println(e);
        }
        while (run) {
            try {
                String id = a.insertArticle(ArticleTest.getArticle());
                insertNum++;
                //assertTrue(a.getArticleById(id).equals(ArticleTest.getArticle()));
                Hmap res = a.getArticleById(id);
                for (String s : res.keySet()) {
                    if (s.equals("title") || s.equals("real_title") || s.equals("sub_title") || s.equals("content") || s.equals("category_id")) {
                        assertTrue(res.get(s).equals(ArticleTest.getArticle().get(s)));
                    }
                }
                System.out.println(threadId + ": inserted: " + insertNum);
            } catch (Exception e) {
                System.out.println(e);
                fail();
                throw new AssertionError();
            }
        }
        return;
    }
}

class selectors implements Runnable {

    public static boolean run = true;
    public int threadId = 0;
    

    public selectors(int id) {
        threadId = id;
    }

    @Override
    public void run() {

        Article a = null;
        try {
            Jmvc $ = new Jmvc();
            a = new Article();
        } catch (Exception e) {
            System.out.println(e);
        }
        while (run) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                ArrayList<Hmap> id = a.getLatestPosts(100);
                for (Hmap h : id) {
                    System.out.println(threadId + ": Date published .:" + sdf.parse(h.get("published")));

                }
            } catch (Exception e) {
                System.out.println(e);
                fail();
                throw new AssertionError();
            }
        }
        return;
    }
}
