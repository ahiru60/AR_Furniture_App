package com.example.ar_furniture_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ConstraintLayout searchSection;
    TextView searchEditText;
    ImageButton topSearchImageButton;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
       searchSection = view.findViewById(R.id.search_section);
       searchEditText=view.findViewById(R.id.searchEditText);
       topSearchImageButton=view.findViewById(R.id.topSearchImageButton);

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    setSearchSectionColors(true);
                } else {
                    setSearchSectionColors(false); // Optional: to reset colors when EditText loses focus
                }
            }
        });
        return view;
    }

    void setSearchSectionColors(boolean state){

        if(state==true){
            searchSection.setBackground(getActivity().getDrawable(R.drawable.background_transparent_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.black, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.black, null));
            searchEditText.setBackground(getActivity().getDrawable(R.drawable.background_round_edged_black_shape));
            topSearchImageButton.setImageResource(R.drawable.ic_search_black);}
        else{
            searchSection.setBackground(getActivity().getDrawable(R.drawable.background_gradient_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setBackground(getActivity().getDrawable(R.drawable.background_round_edged_white_shape));
            topSearchImageButton.setImageResource(R.drawable.ic_search_white);}
    }
}