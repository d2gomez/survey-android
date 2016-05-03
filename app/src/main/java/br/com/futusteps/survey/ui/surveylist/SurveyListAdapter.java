package br.com.futusteps.survey.ui.surveylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.R;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.ViewHolder> {

    private Context mContext;
    private List<Survey> mSurveys;
    private SurveyItemListener mItemListener;

    public SurveyListAdapter(Context context, List<Survey> surveys, SurveyItemListener itemListener) {
        mContext = context;
        setList(surveys);
        mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_survey, parent, false);

        return new ViewHolder(view, mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Survey survey = mSurveys.get(position);

        viewHolder.title.setText(survey.getName());
        viewHolder.questions.setText(mContext.getString(R.string.questions, survey.getQuestions().size()));
    }

    public void replaceData(List<Survey> surveys) {
        setList(surveys);
        notifyDataSetChanged();
    }

    private void setList(List<Survey> surveys) {
        mSurveys = surveys;
    }

    @Override
    public int getItemCount() {
        return mSurveys.size();
    }

    public Survey getItem(int position) {
        return mSurveys.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;

        public TextView questions;
        private SurveyItemListener mItemListener;

        public ViewHolder(View itemView, SurveyItemListener listener) {
            super(itemView);
            mItemListener = listener;
            title = (TextView) itemView.findViewById(R.id.surveyName);
            questions = (TextView) itemView.findViewById(R.id.surveyQuestions);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Survey survey = getItem(position);
            mItemListener.onSurveyClick(survey);

        }
    }

    public interface SurveyItemListener {

        void onSurveyClick(Survey clickedSurvey);
    }
}
