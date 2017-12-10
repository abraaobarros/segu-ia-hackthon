package segu.segu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.create_car_button)
    public void createOnClick(){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }
}
