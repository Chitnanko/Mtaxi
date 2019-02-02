package mtaxi.cumonywa.com.mtaxi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataProvider extends BaseAdapter{
    private Context context;
    private List<ItemGenerator> list;
    public DataProvider(Context context, List<ItemGenerator> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view==null){
            LayoutInflater IF=(LayoutInflater.from(context));
            view=IF.inflate(R.layout.historylist,null);

        }

       // ImageView txtM=view.findViewById(R.id.tvN);
        TextView textView=view.findViewById(R.id.txtDriverName);
        TextView CarType=view.findViewById(R.id.txtDriverCarType);
        TextView txtPrice=view.findViewById(R.id.txtPrice);
        TextView startAdd=view.findViewById(R.id.txtStartPlace);
        TextView stopAdd=view.findViewById(R.id.txtPlaceEnd);

        Log.e("Adapter:",list.get(position).getDriverName().toString()+"$$$$"+list.get(position).getStartPlace()+"**********************");
//TextView textView1=view.findViewById(R.id.test);
        textView.setText(list.get(position).getDriverName());
        CarType.setText(list.get(position).getDriverCarType());
        txtPrice.setText(list.get(position).getPrice());
        startAdd.setText(list.get(position).getStartPlace());
        stopAdd.setText(list.get(position).getEndPlace());
        return view;
    }


}
