package com.example.ar_furniture_application.ProductFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.CatalogAdapter;
import com.example.ar_furniture_application.Home.Home_Fragments.CatalogFragment;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.WebServices.ApiService;
import com.example.ar_furniture_application.WebServices.ErrorResponse;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.example.ar_furniture_application.WebServices.Models.Keyword;
import com.example.ar_furniture_application.WebServices.RetrofitClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements CatalogAdapter.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view,searchSection;
    private AutoCompleteTextView searchEditText;
    private ArrayAdapter<String> arrayAdapter;
    private ImageButton topSearchImageButton;
    private TextView text;
    private WebView webView;
    private RecyclerView recyclerView;
    private String userID;
private UserSession userSession;
    CatalogAdapter productListsAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
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
        // Initialize FirebaseAnalytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        userSession = new UserSession(getContext());
        userID = userSession.getCurrentUser().getUserID();
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));
       setupViews();
        topSearchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String searchTerm = searchEditText.getText().toString();

                // Firebase Analytics - Log Search Event
                if (!searchTerm.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
                }
                loadData(new CatalogFragment.DataCallback() {
                    @Override
                    public void onDataLoaded(List<CatItem> catItems) {
                        // Handle the loaded data here
                        // For example, set it to your adapter
                        productListsAdapter.setCatItems(catItems);
                        recyclerView.setAdapter(productListsAdapter);
                    }

                    @Override
                    public void onDataError(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        text.setText(errorMessage);
                        recyclerView.setAdapter(null);
                    }
                });

                recyclerView.setAdapter(productListsAdapter);
//        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));// Inflate the layout for this fragment
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<List<Keyword>> call;
                call = apiService.searchKeywords(searchEditText.getText().toString());
                call.enqueue(new Callback<List<Keyword>>() {
                    @Override
                    public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<String> items = new ArrayList<>();
                            for(Keyword keyword:response.body()){
                                items.add(keyword.getName());
                            }
                            arrayAdapter = new ArrayAdapter<String>( getContext(), android.R.layout.simple_list_item_activated_1,items);
                            searchEditText.setAdapter(arrayAdapter);

                        } else {
                            try {
                                text.setText("searching");
                                Toast.makeText(getContext(),"Failed to get products: " + response.errorBody().string(),Toast.LENGTH_SHORT);
                            } catch (IOException e) {
                                text.setText("searchingg");
                                e.printStackTrace();
                                Toast.makeText(getContext(),"Failed to get products and parse error body",Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Keyword>> call, Throwable t) {
                        Toast.makeText(getContext(),"Error: " + t.getMessage(),Toast.LENGTH_SHORT);
                    }
                });

                if(searchEditText.getText().toString().length()>0){
                    loadData(new CatalogFragment.DataCallback() {
                        @Override
                        public void onDataLoaded(List<CatItem> catItems) {
                            // Handle the loaded data here
                            // For example, set it to your adapter
                            productListsAdapter.setCatItems(catItems);
                            recyclerView.setAdapter(productListsAdapter);
                        }

                        @Override
                        public void onDataError(String errorMessage) {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            text.setText(errorMessage);
                            recyclerView.setAdapter(null);
                        }
                    });

                    recyclerView.setAdapter(productListsAdapter);
//        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));// Inflate the layout for this fragment
                }

            }
        });

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

    private void setupViews() {
        searchSection = view.findViewById(R.id.search_section);
        searchEditText=view.findViewById(R.id.searchEditText);
        topSearchImageButton=view.findViewById(R.id.topSearchImageButton);
        text =view.findViewById(R.id.text);

        productListsAdapter = new CatalogAdapter(this);
        recyclerView = view.findViewById(R.id.catalog_recyclerView);

    }

    @Override
    public void onItemClickItem(CatItem item) {
        // Perform the fragment transaction to replace the fragment container with a new fragment
        ProductFragment productFragment = new ProductFragment();

        // Pass data to the new fragment if needed
//        Bundle bundle = new Bundle();
//        bundle.putString("productName", product.getName());
//        diagReportFragment.setArguments(bundle);

        Bundle args = new Bundle();
        args.putSerializable("item",item);

// Set the arguments to the fragment
        productFragment.setArguments(args);
        hideKeyboard();
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, productFragment)
                .setReorderingAllowed(true)
                .addToBackStack("product") // Name can be null
                .commit();
    }

    @Override
    public void onAddToCartClick(CatItem item){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        UserSession session = new UserSession(getContext());
        Call<CartItem> call;
        UserSession userSession = new UserSession(getContext());
        CartItem cartItem = new CartItem(userSession.getCurrentUser().getCartID(),item.getFurnitureID(),"1", item.getPrice());
        call = apiService.addCartItem(cartItem);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(),"Item added to cart",Toast.LENGTH_SHORT);
                } else {
                    try {
                        // Convert the error body to a string
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        String errorMessage = errorResponse.getError();
                        Toast.makeText(getContext(), "Failed to add item :" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to get add item and parse error body", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                Toast.makeText(getContext(),"Error: " + t.getMessage(),Toast.LENGTH_SHORT);
            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void loadData(CatalogFragment.DataCallback callback) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<CatItem>> call;
        call = apiService.searchItmes(userID,searchEditText.getText().toString());
        call.enqueue(new Callback<List<CatItem>>() {
            @Override
            public void onResponse(Call<List<CatItem>> call, Response<List<CatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onDataLoaded(response.body());
                } else {
                    try {
                        callback.onDataError("Failed to get products: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onDataError("Failed to get products and parse error body");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CatItem>> call, Throwable t) {
                callback.onDataError("Error: " + t.getMessage());
            }
        });
    }


    public interface DataCallback {
        void onDataLoaded(List<CatItem> catItems);
        void onDataError(String errorMessage);
    }

    void setSearchSectionColors(boolean state){

        if(state==true){
            searchSection.setBackground(getActivity().getDrawable(R.drawable.background_transparent_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setBackground(getActivity().getDrawable(R.drawable.background_round_edged_yelloshape));}
        else{
            searchSection.setBackground(getActivity().getDrawable(R.drawable.background_gradient_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.plain_yellow, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setBackground(getActivity().getDrawable(R.drawable.background_round_edged_blue_gradient_shape));
            //topSearchImageButton.setImageResource(R.drawable.ic_search_white);}
            Drawable topSearchImagebuttonDrawable = topSearchImageButton.getDrawable();
            topSearchImagebuttonDrawable = DrawableCompat.wrap(topSearchImagebuttonDrawable);
            DrawableCompat.setTint(topSearchImagebuttonDrawable, getResources().getColor(R.color.plain_yellow));}
    }
}