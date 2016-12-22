package ch.epfl.sweng.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.facebook.FacebookSdk;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.data.FirebaseTaskHelper;
import ch.epfl.sweng.project.data.LocalTaskHelper;
import ch.epfl.sweng.project.data.TaskHelper;
import ch.epfl.sweng.project.data.TaskProvider;
import ch.epfl.sweng.project.data.UserProvider;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;


public class DatabaseTest extends SuperTest{
    private SharedPreferences prefs;

    @BeforeClass
    public static void setUserProvider() {
        UserProvider.setProvider(Utils.TEST_PROVIDER);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class){
        //Override to be able to change the SharedPreferences effectively
        @Override
        protected void beforeActivityLaunched(){
            Context actualContext = InstrumentationRegistry.getTargetContext();
            prefs = actualContext.getSharedPreferences(actualContext.getString(R.string.application_prefs_name), Context.MODE_PRIVATE);
            prefs.edit().putBoolean(actualContext.getString(R.string.first_launch), false).apply();
            prefs.edit().putBoolean(actualContext.getString(R.string.new_user), false).apply();
            FacebookSdk.sdkInitialize(actualContext);
            super.beforeActivityLaunched();
        }
    };

    private TaskHelper mDatabase;

    @Before
    public void initLocalDB()
    {
        TaskListAdapter mTaskAdapter;
        ArrayList<Task> taskList = new ArrayList<>();
        mTaskAdapter = new TaskListAdapter(mActivityRule.getActivity(), taskList);
        TaskProvider provider = new TaskProvider(mActivityRule.getActivity(), mTaskAdapter, taskList);
        mDatabase = provider.getTaskProvider();
    }


    @Test
    public void inTestInitLocalDB(){

        initLocalDB();
        assertThat(mDatabase, instanceOf(LocalTaskHelper.class));
    }

    @Test
    public void initFirebaseDb(){

        TaskListAdapter mTaskAdapter;
        ArrayList<Task> taskList = new ArrayList<>();

        mTaskAdapter = new TaskListAdapter(mActivityRule.getActivity(), taskList);
        mDatabase = new FirebaseTaskHelper(mActivityRule.getActivity(), mTaskAdapter, taskList);
        assertThat(mDatabase, instanceOf(FirebaseTaskHelper.class));
    }

    @Test
    public void retrieveAllDataFromFirebase() {

        // User
        String email = "trixyfinger@gmail.com";
        Location location1 = new Location("Office", 80, 89);
        Location location2 = new Location("Home", 80, 89);
        List<Location> listLocations = new ArrayList<>();
        listLocations.add(location2);
        listLocations.add(location1);
        User testUser1 = new User(email);

        initFirebaseDb();
        mDatabase.retrieveAllData(testUser1, false);
    }

    @Test
    public void addTaskToFirebase() {
        ArrayList<Task> taskList = new ArrayList<>(10);

        // TASK
        String nameTest = "(title@@cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String descriptionTest = "new description test";
        String locationNameTest = "locationName test workplace";
        Date dueDateTest = new Date(0);
        long durationTest = 55;
        Task.Energy energyTest = Task.Energy.LOW;
        String authorTest = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String authorTest2 = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        List<String> listContributorsTest = new ArrayList<>();
        listContributorsTest.add(authorTest);
        listContributorsTest.add(authorTest2);

        Task newTaskTest = new Task(nameTest, descriptionTest, locationNameTest, dueDateTest, durationTest, energyTest.toString(), listContributorsTest, 0L, false);
        taskList.add(0, newTaskTest);

        initFirebaseDb();
        mDatabase.addNewTask(newTaskTest, 0, false);
    }


    @Test
    public void deleteTaskFromFirebase() {
        ArrayList<Task> taskList = new ArrayList<>(10);

        // TASK
        String nameTest = "(title@@cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String descriptionTest = "new description test";
        String locationNameTest = "locationName test workplace";
        Date dueDateTest = new Date(0);
        long durationTest = 55;
        Task.Energy energyTest = Task.Energy.LOW;
        String authorTest = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String authorTest2 = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        List<String> listContributorsTest = new ArrayList<>();
        listContributorsTest.add(authorTest);
        listContributorsTest.add(authorTest2);

        Task newTaskTest = new Task(nameTest, descriptionTest, locationNameTest, dueDateTest, durationTest, energyTest.toString(), listContributorsTest, 0L, false);
        taskList.add(0, newTaskTest);

        initFirebaseDb();
        mDatabase.addNewTask(newTaskTest, 0, false);
        mDatabase.deleteTask(newTaskTest, 0);
    }

    @Test
    public void updateTaskFromFirebase() {
        ArrayList<Task> taskList = new ArrayList<>(10);

        // TASK
        String nameTest = "(title@@cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String descriptionTest = "old description test";
        String locationNameTest = "locationName test workplace";
        Date dueDateTest = new Date(0);
        long durationTest = 55;
        Task.Energy energyTest = Task.Energy.LOW;
        String authorTest = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String authorTest2 = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        List<String> listContributorsTest = new ArrayList<>();
        listContributorsTest.add(authorTest);
        listContributorsTest.add(authorTest2);

        String nameTest2 = "new name Test";
        String descriptionTest2 = "new description test";
        String locationNameTest2 = "locationName test workplace";
        Date dueDateTest2 = new Date(0);
        long durationTest2 = 55;
        Task.Energy energyTest2 = Task.Energy.LOW;
        String authorTest21 = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        String authorTest22 = "cedric.viaccoz@gmail.com@@cirdec3961@gmail.com";
        List<String> listContributorsTest2 = new ArrayList<>();
        listContributorsTest.add(authorTest21);
        listContributorsTest.add(authorTest22);

        Task oldTaskTest = new Task(nameTest, descriptionTest, locationNameTest, dueDateTest, durationTest, energyTest.toString(), listContributorsTest, 0L, false);
        Task newTaskTest = new Task(nameTest2, descriptionTest2, locationNameTest2, dueDateTest2, durationTest2, energyTest2.toString(), listContributorsTest2, 0L, false);
        taskList.add(0, oldTaskTest);

        initFirebaseDb();
        mDatabase.addNewTask(oldTaskTest, 0, false);
        mDatabase.updateTask(oldTaskTest, newTaskTest, 0);
    }



}

