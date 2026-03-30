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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_furniture_application.Adapters.CatalogAdapter;
import com.example.ar_furniture_application.Models.Sessions.UserSession;
import com.example.ar_furniture_application.R;
import com.example.ar_furniture_application.ViewModel.SearchViewModel;
import com.example.ar_furniture_application.WebServices.Models.CartItem;
import com.example.ar_furniture_application.WebServices.Models.CatItem;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements CatalogAdapter.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View view, searchSection;
    private AutoCompleteTextView searchEditText;
    private ArrayAdapter<String> arrayAdapter;
    private ImageButton topSearchImageButton;
    private TextView text;
    private RecyclerView recyclerView;
    private String userID;
    private UserSession userSession;
    private CatalogAdapter productListsAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private SearchViewModel viewModel;

    public SearchFragment() {
        // Required empty public constructor
    }

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        userSession = new UserSession(getContext());
        userID = (userSession.getCurrentUser() != null) ? userSession.getCurrentUser().getUserID() : "0";
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.cool_blue, getActivity().getTheme()));

        setupViews();

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        viewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(), catItems -> {
            productListsAdapter.setCatItems(catItems);
            recyclerView.setAdapter(productListsAdapter);
        });

        viewModel.getKeywordsLiveData().observe(getViewLifecycleOwner(), keywords -> {
            ArrayList<String> keywordNames = new ArrayList<>();
            for (com.example.ar_furniture_application.WebServices.Models.Keyword keyword : keywords) {
                keywordNames.add(keyword.getName());
            }
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_activated_1, keywordNames);
            searchEditText.setAdapter(arrayAdapter);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            text.setText(errorMessage);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
        });

        viewModel.getAddToCartSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        topSearchImageButton.setOnClickListener(v -> {
            hideKeyboard();
            String searchTerm = searchEditText.getText().toString();
            if (!searchTerm.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
            }
            viewModel.searchProducts(userID, searchTerm);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = searchEditText.getText().toString();
                if (!query.isEmpty()) {
                    viewModel.searchKeywords(query);
                    viewModel.searchProducts(userID, query);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
        });

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> setSearchSectionColors(hasFocus));

        return view;
    }

    private void setupViews() {
        searchSection = view.findViewById(R.id.search_section);
        searchEditText = view.findViewById(R.id.searchEditText);
        topSearchImageButton = view.findViewById(R.id.topSearchImageButton);
        text = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.catalog_recyclerView);
        productListsAdapter = new CatalogAdapter(this);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    void setSearchSectionColors(boolean state) {
        if (state) {
            searchSection.setBackground(getActivity().getDrawable(R.drawable.background_transparent_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setBackground(getActivity().getDrawable(R.drawable.background_round_edged_yelloshape));
        } else {
            searchSection.setBackground(getActivity().getDrawable(R.drawable.background_gradient_shape));
            searchEditText.setTextColor(getResources().getColor(R.color.plain_yellow, null));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
            searchEditText.setBackground(getActivity().getDrawable(R.drawable.background_round_edged_blue_gradient_shape));
            Drawable topSearchImagebuttonDrawable = topSearchImageButton.getDrawable();
            topSearchImagebuttonDrawable = DrawableCompat.wrap(topSearchImagebuttonDrawable);
            DrawableCompat.setTint(topSearchImagebuttonDrawable, getResources().getColor(R.color.plain_yellow));
        }
    }

    @Override
    public void onItemClickItem(CatItem item) {
        ProductFragment productFragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        productFragment.setArguments(args);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, productFragment)
                .setReorderingAllowed(true)
                .addToBackStack("product")
                .commit();
    }

    @Override
    public void onAddToCartClick(CatItem item) {
        if (userSession.getCurrentUser() != null) {
            CartItem cartItem = new CartItem(
                    userSession.getCurrentUser().getCartID(),
                    item.getFurnitureID(), "1", item.getPrice());
            viewModel.addToCart(cartItem);
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_LONG).show();
        }
    }
}
