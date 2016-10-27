package ch.epfl.sweng.project;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public class TaskTest {
    @Rule
    public final ExpectedException thrownException = ExpectedException.none();

    private Task testTask;

    @Before
    public void initValidValues() {
        String taskName = "test task";
        String taskDescription = "Task built with all parameters";
        Location location = new Location("Office", Location.LocationType.WORKPLACE.toString(), 80, 89);
        Date dueDate = new Date(3);
        String author = "Arthur Rimbaud";
        Task.Energy energy = Task.Energy.HIGH;
        long duration = 60;
        List<String> listOfContributors = new ArrayList<>();
        listOfContributors.add(author);
        testTask = new Task(taskName, taskDescription, location, dueDate, duration, energy.toString(), listOfContributors);
    }

    /**
     * Test that the getters and setters work correctly with parameters
     */
    @Test
    public void testConstructorWithAllParameters() {
        String nameTest = "new name Test";
        String descriptionTest = "new description test";

        String locationNameTest = "location test workplace";
        Location.LocationType locationTypeTest = Location.LocationType.WORKPLACE;
        double latTest = 32;
        double longTest = 55;
        Location locationTest = new Location(locationNameTest, locationTypeTest.toString(), latTest, longTest);
        Date dueDateTest = new Date(0);
        long durationTest = 55;
        Task.Energy energyTest = Task.Energy.LOW;
        String authorTest = "A test author";
        List<String> listContributorsTest = new ArrayList<>();
        listContributorsTest.add(authorTest);

        Task newTaskTest = new Task(nameTest, descriptionTest, locationTest, dueDateTest, durationTest, energyTest.toString(), listContributorsTest);

        assertEquals(nameTest, newTaskTest.getName());
        assertEquals(descriptionTest, newTaskTest.getDescription());
        assertEquals(locationTest.getName(), newTaskTest.getLocation().getName());
        assertEquals(longTest, newTaskTest.getLocation().getLongitude());
        assertEquals(latTest, newTaskTest.getLocation().getLatitude());
        assertEquals(locationTest.getType(), newTaskTest.getLocation().getType());
        assertEquals(dueDateTest.getTime(), newTaskTest.getDueDate().getTime());
        assertEquals(durationTest, newTaskTest.getDuration());
        assertEquals(energyTest, newTaskTest.getEnergy());
    }

    @Test
    public void testConstructorWithTwoParameters() {
        String name = "task";
        String description = "The first task";
        Task task = new Task(name, description);

        assertEquals(name, task.getName());
        assertEquals(description, task.getDescription());
        assertEquals(new Location().getName(), task.getLocation().getName());
        assertEquals(new Location().getGPSCoordinates().longitude, task.getLocation().getGPSCoordinates().longitude);
        assertEquals(new Location().getGPSCoordinates().latitude, task.getLocation().getGPSCoordinates().latitude);
        assertEquals(new Location().getType(), task.getLocation().getType());
        assertEquals(0, task.getDueDate().getTime());
        assertEquals(30, task.getDuration());
        assertEquals(Task.Energy.NORMAL, task.getEnergy());
    }

    /**
     * Test that the setters modify correctly the Task
     */
    @Test
    public void testTaskSettersName() {
        String newName = "another name";
        testTask.setName(newName);
        assertEquals(newName, testTask.getName());
    }

    @Test
    public void testTaskSetDescription() {
        String newDescription = "This is a new description";
        testTask.setDescription(newDescription);
        assertEquals(newDescription, testTask.getDescription());
    }

    @Test
    public void testTaskSetLocation() {
        String newLocationName = "Home";
        Location.LocationType newLocationType = Location.LocationType.HOME;
        double newLat = 12;
        double newLong = 17;
        Location newLocation = new Location(newLocationName, newLocationType.toString(), newLat, newLong);

        testTask.setLocation(newLocation);

        assertEquals(newLocationName, newLocation.getName());
        assertEquals(newLocationType, newLocation.getType());
        assertEquals(newLat, newLocation.getLatitude());
        assertEquals(newLong, newLocation.getLongitude());
    }

    @Test
    public void testTaskSetDueDate() {
        Date newDueDate = new Date(0);
        testTask.setDueDate(newDueDate);
        assertEquals(0, testTask.getDueDate().getTime());
    }

    @Test
    public void testTaskSetEnergy() {
        testTask.setEnergyNeeded(Task.Energy.NORMAL);
        assertEquals(Task.Energy.NORMAL, testTask.getEnergy());
    }

    @Test
    public void testTaskSetDuration() {
        testTask.setDurationInMinutes(23);
        assertEquals(23, testTask.getDuration());
    }

    @Test
    public void testTaskSetAuthor() {
        testTask.addContributor("New author");
        assertTrue(testTask.getListOfContributors().contains("New author"));
    }

    /**
     * Test that the setName setter throws an IllegalArgumentException
     * when its argument is null
     */
    @Test
    public void testTaskSetNameException() {
        thrownException.expect(IllegalArgumentException.class);
        testTask.setName(null);
    }

    /**
     * Test that the setDescription setter throws an IllegalArgumentException
     * when its argument is null
     */
    @Test
    public void testTaskSetDescriptionException() {
        thrownException.expect(IllegalArgumentException.class);
        testTask.setDescription(null);
    }

    /**
     * Test that the public constructor throws an IllegalArgumentException
     * when its arguments are null
     */
    @Test
    public void testConstructorException() {
        thrownException.expect(IllegalArgumentException.class);
        new Task(null, null);
    }

    @Test
    public void testTaskSetDueDateException() {
        thrownException.expect(IllegalArgumentException.class);
        testTask.setDueDate(null);

    }

    @Test
    public void testTaskSetEnergyException() {
        thrownException.expect(IllegalArgumentException.class);
        testTask.setEnergyNeeded(null);
    }

    /**
     * Test the describeContents method
     */
    @Test
    public void testDescribeContents() {
        assertEquals(0, testTask.describeContents());
    }
    /**
     * Test that an added Task has been correctly deleted when clicking on Delete.
     */

}
