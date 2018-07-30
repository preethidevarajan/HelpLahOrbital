package com.example.preethidevarajan.helplahorbital;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    TextView UserInput;

    RecyclerView recyclerView;
    List<Question> questionList;
    List<Answer> answerList;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public DatabaseReference mDatabase;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(ForumActivity.this, MainActivity.class);
            startActivity(intent);
        }

        //initialise widget
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForumActivity.this, AskActivity.class);
                startActivity(intent);
            }
        });

        //CRUD Display

        questionList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, questionList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getFirebaseData(new QuestionCallBack() {
            @Override
            public void onCallBack(Question question) {
                questionList.add(question);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public interface QuestionCallBack {
        void onCallBack(Question question);
    }


    private void getFirebaseData(final QuestionCallBack questionCallBack) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference qnref = reference.child("Question");

        qnref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //result held here
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    Question question = new Question();
                    String qn = String.valueOf(dataSnap.child("question").getValue());
                    //String username = String.valueOf(dataSnap.child("username").getValue());
                    question.setQuestion(qn);
                    questionCallBack.onCallBack(question);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle db error
            }
        });

    }

}

    /*protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Question, QuestionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Question, QuestionViewHolder>
                (Question.class, R.layout.row, QuestionViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(QuestionViewHolder viewHolder, Question model, int position) {
                viewHolder.setQuestion(model.getQuestion());
                viewHolder.setUsername(model.getUsername());
                //viewHolder.setAnswer(model.getAnswer());

            }
        };

        //recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

//for initializing textview, get and set text
    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setQuestion(String question) {
            TextView post_question = (TextView) mView.findViewById(R.id.questionview);
            post_question.setText(question);
        }
        public void setUsername(String username) {

        }

        public void setAnswer(String answer) {

        }
    }
    */



