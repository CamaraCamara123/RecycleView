package ma.ensa.recycleview;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.StatefulAdapter;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.recycleview.R;
import ma.ensa.recycleview.adapter.StarAdapter;
import ma.ensa.recycleview.beans.Star;
import ma.ensa.recycleview.service.StarService;

public class ListActivity extends AppCompatActivity {
    private List<Star> stars;
    StarAdapter starAdapter = null;
    private RecyclerView recyclerView;
    private StarService service;

    //private StatefulAdapter starAdapter = null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        stars = new ArrayList<>();
        service = StarService.getInstance();
        init();
        recyclerView = findViewById(R.id.recycle_view);
        //insérer le code
        starAdapter = new StarAdapter(this, service.findAll());
        recyclerView.setAdapter(starAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (starAdapter != null){
                    starAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share){
            String txt = "Stars";
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle("Stars")
                    .setText(txt)
                    .startChooser();
        }
        return super.onOptionsItemSelected(item);
    }


    public void init(){
        service.create(new Star("Emmanuel Macron", "https://data.topquizz.com/distant/question/big/1/1/5/3/1273511_73428f0285.jpg", 3.5f));
        service.create(new Star("Vladmir Poutine", "https://file1.telestar.fr/var/telestar/storage/images/media/images/article/170626-vladimir/vladimir-poutine-a-la-cop-21-a-paris-en-novembre-2015/1719405-1-fre-FR/Vladimir-Poutine-a-la-Cop-21-a-Paris-en-novembre-2015.jpg?alias=exact1024x768_l&size=x100&format=jpeg", 3));
        service.create(new Star("Kim Jong-un",
                "https://sa.kapamilya.com/absnews/abscbnnews/media/2019/reuters/04/23/20190423-kim-jong-un.jpg", 5));
        service.create(new Star("Joe Biden", "https://th.bing.com/th/id/R.439dc55e0549191a2afeceabc21002e2?rik=LGvT9XEq22raVw&pid=ImgRaw&r=0", 1));
        service.create(new Star("Alassane Ouattara", "https://organe.pressecotedivoire.fr/img/article/1549866333-997.jpg", 5));
        service.create(new Star("Assimi Goita", "https://www.xibaaru.sn/wp-content/uploads/2021/06/Assimi-Goita.jpg", 1));
    }
}
