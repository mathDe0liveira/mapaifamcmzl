package com.example.mapaifamcmzl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mapaifamcmzl.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonCadastrar;
    private FirebaseAuth mAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        mAuth = FirebaseAuth.getInstance();

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarDados();
                criarUsuario();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void criarUsuario() {
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    usuario.setId(user.getUid());
                    usuario.salvarDados();
                    startActivity(new Intent(CadastroActivity.this, PrincipalActivity.class));
                }
                else {
                    Toast.makeText(CadastroActivity.this, "Erro ao criar novo usuário.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recuperarDados() {
        if(editTextNome.getText().toString() == "" || editTextEmail.getText().toString() == "" || editTextSenha.getText().toString() == "") {
            Toast.makeText(this, "Você deve preencher todas as informações.", Toast.LENGTH_LONG);
        }
        else {
            usuario = new Usuario();
            usuario.setNome(editTextNome.getText().toString());
            usuario.setEmail(editTextEmail.getText().toString());
            usuario.setSenha(editTextSenha.getText().toString());
        }
    }
}