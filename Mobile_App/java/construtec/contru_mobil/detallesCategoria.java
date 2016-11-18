package construtec.contru_mobil;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Database.DBHandler;
import models.Categoria;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;

public class detallesCategoria extends AppCompatActivity {
    static Categoria category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_categoria);

        String categoryID = getIntent().getStringExtra("id");
        String userID = getIntent().getStringExtra("userID");

        DBHandler db = DBHandler.getSingletonInstance(this);

        category = db.getCategoria(categoryID);

        TextView name = (TextView) findViewById(R.id.name);
        final TextView descripcion = (TextView) findViewById(R.id.description);

        View.OnClickListener updateDescription = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(v.getContext());
                d.setContentView(R.layout.text_popup);
                Button setValue = (Button) d.findViewById(R.id.set);
                Button cancelAction = (Button) d.findViewById(R.id.cancel);

                final EditText np = (EditText) d.findViewById(R.id.newValue);
                np.setInputType(TYPE_CLASS_TEXT);

                setValue.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        category.Descripcion = np.getText().toString();

                        DBHandler db = DBHandler.getSingletonInstance(null);
                        db.updateCategoria(category);

                        descripcion.setText(np.getText());
                        d.dismiss();
                    }
                });
                cancelAction.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        };
        descripcion.setOnClickListener(updateDescription);

        Button deleteBTN = (Button)  findViewById(R.id.delete);

        //TODO hide delete category if user isn't an admin

        name.setText(category.Nombre);
        descripcion.setText(category.Descripcion);

    }

    public void delete(View view){
        DBHandler db = DBHandler.getSingletonInstance(this);
        db.deleteCategoria(category.Nombre);

        finish();
    }

}
