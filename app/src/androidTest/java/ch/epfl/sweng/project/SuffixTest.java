package ch.epfl.sweng.project;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SuffixTest {

    private String title;
    private String creator;
    private String sharer;
    private String taskTitle;
    private String suffix;

    @Before
    public void setup(){
        title = "test";
        creator = "Alice";
        sharer = "Bob";
        taskTitle = "test@@Alice@@Bob";
        suffix = "@@Alice@@Bob";
    }

    @Test
    public void constructSharedTitleTest(){
        String result = Utils.constructSharedTitle(title, creator, sharer);
        assertTrue(result.equals(taskTitle));
    }

    @Test
    public void separateTitleAndSuffixTest(){
        String[] result = Utils.separateTitleAndSuffix(taskTitle);
        assertTrue(result[0].equals(title));
        assertTrue(result[1].equals(suffix));

        result = Utils.separateTitleAndSuffix(title);
        assertTrue(result[0].equals(title));
        assertTrue(result[1].equals(""));

    }

    @Test
    public void getCreatorAndSharerTest(){
        String[] result = Utils.getCreatorAndSharer(suffix);
        assertTrue(result[0].equals(creator));
        assertTrue(result[1].equals(sharer));
    }

    @Test
    public void identityCarriedByAllUtilsOperation(){
        String resultTitle = Utils.constructSharedTitle(title, creator, sharer);
        String[] result = Utils.separateTitleAndSuffix(resultTitle);
        String[] suffixResult = Utils.getCreatorAndSharer(result[1]);
        assertTrue(result[0].equals(title));
        assertTrue(suffixResult[0].equals(creator));
        assertTrue(suffixResult[1].equals(sharer));
    }
}
