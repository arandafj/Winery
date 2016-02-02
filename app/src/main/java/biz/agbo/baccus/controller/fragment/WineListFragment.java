package biz.agbo.baccus.controller.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import biz.agbo.baccus.R;
import biz.agbo.baccus.controller.activity.WineryActivity;
import biz.agbo.baccus.model.Wine;
import biz.agbo.baccus.model.Winery;

/**
 * A simple {@link Fragment} subclass.
 */
public class WineListFragment extends Fragment {

    private OnWineSelectedListener mOnWineSelectedListener = null;
    private ProgressDialog mProgressDialog = null;


    public WineListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_wine_list, container, false);

        AsyncTask<Void, Void, Winery> wineryDownloader = new AsyncTask<Void, Void, Winery>() {
            @Override
            protected Winery doInBackground(Void... voids) {
                return Winery.getInstance();
            }

            @Override
            protected void onPostExecute(Winery winery) {
                ArrayAdapter<Wine> adapter = new ArrayAdapter<Wine>(getActivity(), android.R.layout.simple_list_item_1, winery.getWineList());

                ListView listView = (ListView) root.findViewById(android.R.id.list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (mOnWineSelectedListener != null) {
                            mOnWineSelectedListener.onWineSelected(i);
                        }
                    }
                });

                mProgressDialog.dismiss();
            }
        };

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));

        if (!Winery.isInstanceAvailable()) {
            mProgressDialog.show();
        }

        wineryDownloader.execute();

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnWineSelectedListener = (OnWineSelectedListener)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnWineSelectedListener = null;
    }

    public interface OnWineSelectedListener {
        void onWineSelected(int wineIndex);
    }
}
