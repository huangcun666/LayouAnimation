package example.com.mymenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity{
    private MainUI mainUI;
    private Fragment1 fragment1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainUI=new MainUI(this);
        setContentView(mainUI);
        fragment1=new Fragment1();
        getSupportFragmentManager().beginTransaction().add(mainUI.LEFT_ID,fragment1).commit();
    }
}
