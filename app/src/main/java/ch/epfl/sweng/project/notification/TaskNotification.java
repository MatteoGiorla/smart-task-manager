package ch.epfl.sweng.project.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.EntryActivity;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.receiver.NotificationReceiver;
import ch.epfl.sweng.project.Task;

public class TaskNotification extends AsyncTask<Integer, Void, Void> {
    private List<Task> taskList;
    private Context mContext;

    public TaskNotification(ArrayList<Task> taskList, Context context) {
        this.taskList = new ArrayList<>(taskList);
        mContext = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        clearAllNotifications(params[0]);
        createAllNotifications(params[1]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void clearAllNotifications(int numberOfIds) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < numberOfIds; i++) {

            Intent notificationIntent = new Intent(mContext, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, i, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

            try {
                alarmManager.cancel(pendingIntent);
            } catch (Exception e) {
                Log.e("Alarm", "AlarmManager update was not canceled. " + e.toString());
            }
            pendingIntent.cancel();
        }
    }

    private void createAllNotifications(int numberOfIds) {
        for (int i = 0; i < numberOfIds; i++) {
            Task task = taskList.get(i);
            scheduleNotification(buildNotification(task.getName()), setDelayToNotify(task), i);
        }
    }

    private long setDelayToNotify(Task task) {
        Date dueDate = task.getDueDate();
        long timeNeeded = task.getDuration();
        long delay = dueDate.getTime() - System.currentTimeMillis();

        switch ((int) timeNeeded) {
            case 5:
            case 15:
            case 30:
            case 60: {
                delay -= 23.98 * 60 * 60 * 1000L;
                break;
            }
            case 120:
            case 240: {
                delay -= 48 * 60 * 60 * 1000L;
                break;
            }
            case 480: {
                delay -= 96 * 60 * 60 * 1000L;
                break;
            }
            case 960:
            case 1920: {
                delay -= 168 * 60 * 60 * 1000L;
                break;
            }
            case 2400: {
                delay -= 336 * 60 * 60 * 1000L;
                break;
            }
            case 4800: {
                delay -= 672 * 60 * 60 * 1000L;
                break;
            }
            case 9600: {
                delay -= 672 * 60 * 60 * 1000L;
                break;
            }
        }

        return Math.max(0, delay);
    }

    public void scheduleNotification(Notification notification, long delay, int id) {
        if (delay > 0) {
            Intent notificationIntent = new Intent(mContext, NotificationReceiver.class);
            notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_KEY, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

            long futureInMillis = SystemClock.elapsedRealtime() + delay;
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }
    }

    public Notification buildNotification(String title) {
        Intent startActivityIntent = new Intent(mContext, EntryActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        startActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle(mContext.getString(R.string.notification_title_task));
        builder.setContentText(title + mContext.getString(R.string.notification_content_task));
        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }

    public void createUniqueNotification(int id) {
        Task task = taskList.get(id);
        scheduleNotification(buildNotification(task.getName()), setDelayToNotify(task), id);
    }
}
