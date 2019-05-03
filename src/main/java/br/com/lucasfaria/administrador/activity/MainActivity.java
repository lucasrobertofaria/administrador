package br.com.lucasfaria.administrador.activity;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.fragment.ConfissoesFragment;
import br.com.lucasfaria.administrador.fragment.ContatosFragment;
import br.com.lucasfaria.administrador.fragment.MissaFragment;
import br.com.lucasfaria.administrador.fragment.ParoquiaHistoriaFragment;
import br.com.lucasfaria.administrador.listener.NavigationIconClickListener;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationIconClickListener navigationIconClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MissaFragment())
                    .commit();
        }
        setUpToolbar();

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setUpToolbar() {
        toolbar = findViewById(R.id.app_bar);

        MaterialButton btContatos = findViewById(R.id.btContatos);
        btContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirContatosFragment(v);
            }
        });
        MaterialButton btParoquia = findViewById(R.id.btParoquia);
        btParoquia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirParoquiaHistoriaFragment(v);
            }
        });
        MaterialButton btMissa = findViewById(R.id.btMissa);
        btMissa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMissaFragment(v);
            }
        });
        MaterialButton btConfissoes = findViewById(R.id.btConfissoes);
        btConfissoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirConfissoesFragment(v);
            }
        });

        setSupportActionBar(toolbar);
        navigationIconClickListener = new NavigationIconClickListener(
                this,
                findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                this.getResources().getDrawable(R.drawable.ic_church), // Menu open icon
                this.getResources().getDrawable(R.drawable.ic_close_menu));

        toolbar.setNavigationOnClickListener(navigationIconClickListener); // Menu close icon
    }


    public void abrirContatosFragment(View view) {
        navigationIconClickListener.forceClick();
        navigateTo(new ContatosFragment(), true);
    }

    public void abrirParoquiaHistoriaFragment(View view) {
        navigationIconClickListener.forceClick();
        navigateTo(new ParoquiaHistoriaFragment(), true);
    }

    public void abrirMissaFragment(View view) {
        navigationIconClickListener.forceClick();
        navigateTo(new MissaFragment(), true);
    }

    public void abrirConfissoesFragment(View view) {
        navigationIconClickListener.forceClick();
        navigateTo(new ConfissoesFragment(), true);
    }


}
