package com.example.tp5;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText poids;
    private EditText taille;
    private EditText age;
    private Button ajouter;
    private Button effacer;
    private Button vider;
    private ListView lst;

    private ArrayAdapter<personne> adapter;
    private ArrayList<personne> personnes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        poids = findViewById(R.id.poids);
        taille = findViewById(R.id.taille);
        age = findViewById(R.id.age);
        ajouter = findViewById(R.id.ajouter);
        effacer = findViewById(R.id.effacer);
        vider = findViewById(R.id.vider);
        lst = findViewById(R.id.lst);

        personnes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, personnes);
        lst.setAdapter(adapter);

        ecouteurs();
    }
    private void ecouteurs() {
        ajouter.setOnClickListener(v -> {
            String ageStr = age.getText().toString();
            String poidsStr = poids.getText().toString();
            String tailleStr = taille.getText().toString();

            if (ageStr.isEmpty() || poidsStr.isEmpty() || tailleStr.isEmpty()) {
                Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int personAge = Integer.parseInt(ageStr);
                float personPoids = Float.parseFloat(poidsStr);
                float personTaille = Float.parseFloat(tailleStr);

                if (personAge <= 18) {
                    Toast.makeText(this, "L'âge doit être supérieur à 18 ans", Toast.LENGTH_SHORT).show();
                    return;
                }

                personne newPerson = new personne(personAge, personPoids, personTaille);
                personnes.add(newPerson);
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "Personne ajoutée avec succès", Toast.LENGTH_SHORT).show();
                effacerChamps();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Veuillez entrer des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            }
        });


        effacer.setOnClickListener(v -> effacerChamps());


        vider.setOnClickListener(v -> {
            personnes.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Liste vidée", Toast.LENGTH_SHORT).show();
        });

        lst.setOnItemClickListener((parent, view, position, id) -> {
            personne selectedPerson = personnes.get(position);
            showGenderDialog(selectedPerson);
            afficher (position);
        });
    }

    private void afficher(int position) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle("Homme/Femme");
        bld.setMessage("Vous ete ?");
        bld.setPositiveButton("Homme", (dialog, which) -> {
            personne p= (personne) lst.getItemAtPosition(position);

           String pp=p.toString();

            Toast.makeText(this, pp, Toast.LENGTH_LONG).show();
        });
        bld.setNegativeButton("Femme", (dialog, which) -> {

            personne p= (personne) lst.getItemAtPosition(position);

            String pp=p.toString();

            Toast.makeText(this, pp, Toast.LENGTH_LONG).show();
        });

    }


    private void showGenderDialog(personne person) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Homme / Femme");
        builder.setMessage("Choisissez le genre");


        builder.setPositiveButton("Homme", (dialog, which) -> {

            poidIdeal(person);
        });

        builder.setNegativeButton("Femme", (dialog, which) -> {

            boolean estHomme = false;
            float imc = person.getIMC();
            float poidsIdeal = person.getPoidsIdeal(estHomme);

            String interpretation = interpretIMC(imc);

            String result ="Poids Idéal = " + poidsIdeal +
                    "Interprétation = " + interpretation+
                    "IMC = " + imc + "\n" +
                     "\n"
                    ;

            Toast.makeText(this, result, Toast.LENGTH_LONG).show();

            Toast toast = Toast.makeText(this, result, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        builder.show();
    }

    private void poidIdeal(personne person) {
        boolean estHomme = true;
        float imc = person.getIMC();
        float poidsIdeal = person.getPoidsIdeal(estHomme);


        String interpretation = interpretIMC(imc);


        String result = "IMC = " + imc + "\n" +
                "Poids Idéal = " + poidsIdeal + "\n" +
                "Interprétation = " + interpretation;

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        Toast toast = Toast.makeText(this, result, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private String interpretIMC(float imc) {
        if (imc < 18.5) {
            return "Maigreur";
        } else if (imc >= 18.5 && imc < 30) {
            return "Corpulence normale";
        } else {
            return "Surpoids";
        }
    }


    private void effacerChamps() {
        age.setText("");
        poids.setText("");
        taille.setText("");
        age.requestFocus();
    }
}
