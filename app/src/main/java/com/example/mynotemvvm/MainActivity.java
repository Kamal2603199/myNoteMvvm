package com.example.mynotemvvm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotemvvm.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NoteViewModel noteViewModel;
    private static final int REQUEST_CODE_INSERT = 1; // Constant for request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        // Set up RecyclerView
        binding.Rv.setLayoutManager(new LinearLayoutManager(this));
        binding.Rv.setHasFixedSize(true);
        RVAdapter adapter = new RVAdapter();
        binding.Rv.setAdapter(adapter);

        // Observe LiveData from ViewModel and update RecyclerView when data changes
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes); // Submit the updated note list to the adapter
            }
        });

        // FloatingActionButton click listener to start DataInsertActivity
        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
            intent.putExtra("type", "addMode");
            startActivityForResult(intent, REQUEST_CODE_INSERT); // Start DataInsertActivity
        });

        // Add swipe to delete functionality to RecyclerView
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction==ItemTouchHelper.RIGHT)
                {
                    noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition())); // Delete the swiped note
                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent intent=new Intent(MainActivity.this, DataInsertActivity.class);
                    intent.putExtra("type","update");
                    intent.putExtra("title",adapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("disp",adapter.getNote(viewHolder.getAdapterPosition()).getDisp());
                    intent.putExtra("id",adapter.getNote(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent,2);
                }

            }
        }).attachToRecyclerView(binding.Rv);

        // Handle window insets (e.g., for edge-to-edge display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            String title=data.getStringExtra("title");
            String disp=data.getStringExtra("disp");
            Note note=new Note(title, disp);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();
        } else if (requestCode==2) {
            String title=data.getStringExtra("title");
            String disp=data.getStringExtra("disp");
            Note note=new Note(title, disp);
            note.setId(data.getIntExtra("id",0));
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }
    }

}
