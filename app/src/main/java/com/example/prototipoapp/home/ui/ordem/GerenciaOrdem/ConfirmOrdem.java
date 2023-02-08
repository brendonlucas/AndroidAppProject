package com.example.prototipoapp.home.ui.ordem.GerenciaOrdem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototipoapp.R;
import com.shuhart.stepview.StepView;

public class ConfirmOrdem extends AppCompatActivity {
    StepView stepView;
    //    TextView StepTExtView, descriptionVoiew;
    Button btn_nxt, btn_previs;

    ConstraintLayout box1, box2, box3, box4, box5;

    int index = 0;
    String[] stepstexts = {"Passo1", "Passo 2", "Passo 3", "Passo 4", "Passo 5"};
    String[] descricao = {"dddddddddddd", "ssssssssssssssss", "aaaaaaaaaaaaaaaaaaaaaaa", "iiiiiiiiii"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ordem);
        setTitle("Confirm Ordem");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        box1 = findViewById(R.id.content_dados_inicial);

        box2 = findViewById(R.id.content_form_1);
        box3 = findViewById(R.id.content_form_2);
        box4 = findViewById(R.id.content_select_data);
        box5 = findViewById(R.id.content_resumo);

        box2.setVisibility(View.GONE);
        box3.setVisibility(View.GONE);
        box4.setVisibility(View.GONE);
        box5.setVisibility(View.GONE);


//        StepTExtView = findViewById(R.id.yyy);
        stepView = findViewById(R.id.step_view);
        stepView.getState().animationType(StepView.ANIMATION_ALL)
                .stepsNumber(5)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();


        btn_nxt = findViewById(R.id.next_btn);
        btn_previs = findViewById(R.id.previs_btn);

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < stepstexts.length && index < 4) {
                    index++;
                    stepView.go(index, true);

                    setGoneVisible(index);

                    Toast.makeText(getApplicationContext(), index + "", Toast.LENGTH_SHORT).show();
                    if (index == 4) {
                        btn_nxt.setVisibility(View.INVISIBLE);
                    }
                    if (index > 0) {
                        btn_previs.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btn_previs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < stepstexts.length && index >= 1) {
                    index--;
                    stepView.go(index, true);

                    setGoneVisible(index);

                    Toast.makeText(getApplicationContext(), index + "", Toast.LENGTH_SHORT).show();
                    if (index < 4) {
                        btn_nxt.setVisibility(View.VISIBLE);
                    }
                    if (index == 0) {
                        btn_previs.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }

    public void setGoneVisible(int page) {
        switch (page) {
            case 0:
                box1.setVisibility(View.VISIBLE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 1:
                box2.setVisibility(View.VISIBLE);
                box1.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 2:
                box3.setVisibility(View.VISIBLE);
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 3:
                box4.setVisibility(View.VISIBLE);
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box5.setVisibility(View.GONE);
                break;
            case 4:
                box5.setVisibility(View.VISIBLE);
                box1.setVisibility(View.GONE);
                box2.setVisibility(View.GONE);
                box3.setVisibility(View.GONE);
                box4.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}