package com.example.ap_plato.fragments.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ap_plato.Game;
import com.example.ap_plato.MainActivity;
import com.example.ap_plato.MainPage;
import com.example.ap_plato.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class RankingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    class Item {
        String username;
        byte image[];
        int score;
        Bitmap bitmap;
    }

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    MyAdapter adapter;
    ListView listView;
    TextView thispos;
    TextView thisscore;
    ArrayList<Item> items;
    ArrayList<Item> tmp;
    SwipeRefreshLayout swipeRefreshLayout;
    int position;
    int score1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranking_fragment,container,false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh2);
        thispos =view.findViewById(R.id.playerpos);
        ImageView thisimage = view.findViewById(R.id.profile_image);
        TextView thisuser = view.findViewById(R.id.profile_user);
        thisscore = view.findViewById(R.id.profile_score);
        thisuser.setText(MainActivity.username);
        thisimage.setImageBitmap(MainPage.profileimagebitmap);
        listView = view.findViewById(R.id.ranking);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("Ranking");
                    dataOutputStream.writeUTF(Game.game);
                    items = new ArrayList<>();
                    tmp = new ArrayList<>();
                    while (true) {
                        String username = dataInputStream.readUTF();
                        if (username.equals("finish")) {
                            break;
                        }
                        int len = dataInputStream.readInt();
                        System.out.println(len);
                        byte[] image = new byte[len];
                        dataInputStream.readFully(image);
                        int score = dataInputStream.readInt();
                        Item item = new Item();
                        item.username=username;
                        item.score=score;
                        item.bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
                        if(item.bitmap==null) {
                            Drawable myDrawable = getResources().getDrawable(R.drawable.avatar_default);
                            item.bitmap = ((BitmapDrawable) myDrawable).getBitmap();
                        }
                         tmp.add(item) ;
                            if(item.username.equals(MainActivity.username)){
                                position=tmp.indexOf(item);
                                score1 = item.score;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        thispos.setText(String.valueOf(position+1));
                                        thisscore.setText(String.valueOf(score1));
                                    }
                                });
                            }
                        }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            items=tmp;
                            adapter = new MyAdapter(getActivity().getApplicationContext(), items);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        mainthread.start();
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }
    public void onRefresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        swipeRefreshLayout.setRefreshing(false);
    }

    class MyAdapter extends ArrayAdapter{
        Context context;
        ArrayList<Item> items;

        MyAdapter(Context c, ArrayList<Item> items) {
            super(c, R.layout.rankingitem_layout, items);
            this.context = c;
            this.items = items;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View listitem = layoutInflater.inflate(R.layout.rankingitem_layout, parent, false);
            TextView listposition = listitem.findViewById(R.id.listloc);
            listposition.setText(String.valueOf(position+1));
            ImageView image = listitem.findViewById(R.id.ranking_image);
            image.setImageBitmap(items.get(position).bitmap);
            TextView username = listitem.findViewById(R.id.ranking_username);
            username.setText(items.get(position).username);
            TextView score = listitem.findViewById(R.id.ranking_score);
            score.setText(String.valueOf(items.get(position).score));
            return listitem;
        }
    }
}
