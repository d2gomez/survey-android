package br.com.futusteps.survey.ui.createsurvey;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.survey.data.remote.MockService;
import br.com.futusteps.survey.data.repository.SurveyRepositories;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.ui.base.BaseActivity;
import br.com.futusteps.survey.ui.view.Alert;

public class CreateSurveyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        initFragment(R.id.mainLayout, NewSurveyFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        leave();
    }

    private void leave() {
        Alert.showConfirm(this, R.string.questions_leave_survey, R.string.yes,
                R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.questions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_leave:
                leave();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
