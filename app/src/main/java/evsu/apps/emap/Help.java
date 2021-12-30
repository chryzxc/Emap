package evsu.apps.emap;

import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Help extends AppCompatActivity {
    ImageView helpImage;
    ConstraintLayout next,back;
    Integer number = 0;
    Integer imagenumber;
    Integer last;
    List<Drawable> imageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helpImage = findViewById(R.id.helpImage);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        addImage();
        helpImage.setImageDrawable(imageViews.get(number));
        imagenumber = imageViews.size() - 1;
        back.setVisibility(View.GONE);
        Integer string = imageViews.size();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number < imagenumber) {
                    number = number + 1;
                    helpImage.setImageDrawable(imageViews.get(number));
                    if (number == imagenumber) {
                        next.setVisibility(View.GONE);
                    } else {
                        if (back.getVisibility() == View.GONE) {
                           back.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (number >=1){
                    number = number - 1;
                    helpImage.setImageDrawable(imageViews.get(number));
                    if (number == 0) {
                       back.setVisibility(View.GONE);
                         }else {
                    if (next.getVisibility() == View.GONE) {
                        next.setVisibility(View.VISIBLE);
                    }
                }
                }
            }
        });




    }

    public void addImage(){
        imageViews.add(getResources().getDrawable(R.drawable.h1));
        imageViews.add(getResources().getDrawable(R.drawable.h2));
        imageViews.add(getResources().getDrawable(R.drawable.h3));
        imageViews.add(getResources().getDrawable(R.drawable.h4));
        imageViews.add(getResources().getDrawable(R.drawable.h5));
        imageViews.add(getResources().getDrawable(R.drawable.h6));
        imageViews.add(getResources().getDrawable(R.drawable.h7));
        imageViews.add(getResources().getDrawable(R.drawable.h8));

    }
}
