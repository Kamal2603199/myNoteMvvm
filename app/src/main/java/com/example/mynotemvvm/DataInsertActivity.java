package com.example.mynotemvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mynotemvvm.databinding.ActivityDataInsertBinding;

public class DataInsertActivity extends AppCompatActivity {
ActivityDataInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDataInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type=getIntent().getStringExtra("type");
        if (type.equals("update")){
            setTitle("update");
            binding.title.setText(getIntent().getStringExtra("title"));
            binding.title.setText(getIntent().getStringExtra("disp"));
            int id=getIntent().getIntExtra("id",0);
            binding.add.setText("Update Note");
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("title", binding.title.getText().toString());
                    intent.putExtra("disp", binding.disp.getText().toString());
                    intent.putExtra("id",id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }else {
            setTitle("Add Mode");
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("title", binding.title.getText().toString());
                    intent.putExtra("disp", binding.disp.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        startActivity(new Intent(DataInsertActivity.this,MainActivity.class));
        finish();
        return null;
    }
}