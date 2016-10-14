package ch.epfl.sweng.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EditTaskActivity extends AppCompatActivity {

    public static final String returnedTask = "ch.epfl.sweng.NewTaskActivity.EDIT_TASK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Intent intent = getIntent();
        Bundle b = this.getIntent().getExtras();

        if (b != null) {
            Task t = b.getParcelable(TaskFragment.TASK_EDIT);
            TextView title = (TextView) findViewById(R.id.txt_title);
            TextView description = (TextView) findViewById(R.id.txt_description);
            title.setText(t.getName());
            description.setText(t.getDescription());

        }
    }
}
