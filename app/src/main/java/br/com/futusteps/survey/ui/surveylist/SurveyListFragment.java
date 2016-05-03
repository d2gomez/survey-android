package br.com.futusteps.survey.ui.surveylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.R;
import br.com.futusteps.survey.data.remote.MockService;
import br.com.futusteps.survey.data.repository.SurveyRepositories;
import br.com.futusteps.survey.data.repository.UserRepositories;
import br.com.futusteps.survey.ui.base.BaseFragment;
import br.com.futusteps.survey.ui.questions.QuestionsActivity;
import butterknife.Bind;
import butterknife.ButterKnife;


public class SurveyListFragment extends BaseFragment implements SurveyListContract.View{

    @Bind(R.id.surveyList)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.empty)
    TextView empty;

    private SurveyListContract.UserActionsListener mActionsListener;
    private SurveyListAdapter mListAdapter;

    public SurveyListFragment() {
        // Requires empty public constructor
    }

    public static SurveyListFragment newInstance() {
        return new SurveyListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurveyListAdapter.SurveyItemListener mItemListener = new SurveyListAdapter.SurveyItemListener() {
            @Override
            public void onSurveyClick(Survey clickedSurvey) {
                mActionsListener.startSurvey(clickedSurvey);
            }
        };

        mListAdapter = new SurveyListAdapter(getContext(), new ArrayList<Survey>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        String userId = UserRepositories.getInMemoryRepoInstance().getUser().getId();
        mActionsListener.loadSurveys(userId, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        dependencyInjection();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_survey_list, container, false);
        ButterKnife.bind(this, root);

        recyclerView.setAdapter(mListAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pull-to-refresh
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String userId = UserRepositories.getInMemoryRepoInstance().getUser().getId();
                mActionsListener.loadSurveys(userId, true);
            }
        });
        return root;
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }

        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showSurveys(List<Survey> surveys) {
        if(surveys.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
        mListAdapter.replaceData(surveys);

    }

    @Override
    public void showSurvey() {
        Intent intent = new Intent(getContext(), QuestionsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error_surveys, Toast.LENGTH_LONG).show();
    }

    private void dependencyInjection() {
        MockService service = MockService.Builder.build();
        SurveyRepository repository = SurveyRepositories.getInMemoryRepoInstance(service);
        mActionsListener = new SurveyListPresenter(this, repository);
    }
}
