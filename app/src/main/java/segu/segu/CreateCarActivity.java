package segu.segu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCarActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.create_car_button)
    public void createOnClick(){
        Intent intent = new Intent(this, ShareFriendsActivity.class);
        startActivity(intent);
    }
}
