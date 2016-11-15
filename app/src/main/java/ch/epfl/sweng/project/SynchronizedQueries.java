package ch.epfl.sweng.project;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that allow us to retrieve the user's data before executing the remaining code of the app.
 */
class SynchronizedQueries {

    private final DatabaseReference reference;
    private final HashMap<DatabaseReference, DataSnapshot> refsToSnaps = new HashMap<>();
    private ValueEventListener listener;

    SynchronizedQueries(final DatabaseReference reference) {
        this.reference = reference;
    }

    public Task<Map<DatabaseReference, DataSnapshot>> start() {
        // Create a Task<DataSnapshot> to trigger in response to each database listener.
        final Task<DataSnapshot> task;
        final TaskCompletionSource<DataSnapshot> source = new TaskCompletionSource<>();
        listener = new MyValueEventListener(reference, source);
        reference.addListenerForSingleValueEvent(listener);
        task = source.getTask();


        // Return a single Task that triggers when all queries are complete.  It contains
        // a map of all original DatabaseReferences originally given here to their resulting
        // DataSnapshot.
        return Tasks.whenAll(task).continueWith(new Continuation<Void, Map<DatabaseReference, DataSnapshot>>() {
            @Override
            public Map<DatabaseReference, DataSnapshot> then(@NonNull Task<Void> task) throws Exception {
                task.getResult();
                return new HashMap<>(refsToSnaps);
            }
        });
    }

    void stop() {
        reference.removeEventListener(listener);
        refsToSnaps.clear();
    }

    /**
     * ValueEventListener that add the DataSnapshot in the refsToSnaps map.
     * It associates all the DataSnapshot, associated with the current user,
     * to the current user's reference.
     */
    private class MyValueEventListener implements ValueEventListener {
        private final DatabaseReference ref;
        private final TaskCompletionSource<DataSnapshot> taskSource;

        MyValueEventListener(DatabaseReference ref, TaskCompletionSource<DataSnapshot> taskSource) {
            this.ref = ref;
            this.taskSource = taskSource;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            refsToSnaps.put(ref, dataSnapshot);
            taskSource.setResult(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            taskSource.setException(databaseError.toException());
        }
    }
}