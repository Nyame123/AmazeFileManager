package com.amaze.filemanager.fragments;

import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.amaze.filemanager.R;
import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.utils.OpenMode;
import com.bisapp.customrecyclerview.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CustomRecyclerView.BindViewsListener {
    private MainActivity mainActivity;
    private static final String RECENT_FILE_PATH = "6";
    public static final String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        CustomRecyclerView categoriesRecyclerview = view.findViewById(R.id.category_recyclerview);
        view.findViewById(R.id.recent_files).setOnClickListener(v->{
            navigateTo(RECENT_FILE_PATH);
        });

        view.findViewById(R.id.storage_devices).setOnClickListener(v->{
            navigateTo(INTERNAL_STORAGE_PATH);
        });

        CategoryItem audios = new CategoryItem(R.drawable.ic_library_music_white_24dp,"Audios","2");
        CategoryItem images = new CategoryItem(R.drawable.ic_doc_image,"Images","0");
        CategoryItem videos = new CategoryItem(R.drawable.ic_video_library_white_24dp,"Videos","1");
        CategoryItem documents = new CategoryItem(R.drawable.ic_library_books_white_24dp,"Documents","3");
        CategoryItem apks = new CategoryItem(R.drawable.ic_apk_library_white_24dp,"APKs","4");

        List<CategoryItem> categoryItems = new ArrayList<>();
        categoryItems.add(audios);
        categoryItems.add(images);
        categoryItems.add(videos);
        categoryItems.add(documents);
        categoryItems.add(apks);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        categoriesRecyclerview
                .setCustomLayoutManager(gridLayoutManager)
                .setBindViewsListener(this);

        categoriesRecyclerview.addModels(categoryItems);
    }

    @Override
    public void bindViews(View view, List<?> objects, int position) {
        CategoryItem categoryItem = (CategoryItem) objects.get(position);
        TextView description = view.findViewById(R.id.description);
        ImageView imageView = view.findViewById(R.id.image);

        description.setText(categoryItem.getDescription());
        imageView.setImageResource(categoryItem.getDrawable());

        view.setOnClickListener(v -> {
            CategoryItem pressedItem = (CategoryItem) objects.get(position);
            navigateTo(pressedItem.pendingPath);
        });

    }

    private void navigateTo(String pendingPath) {
        MainFragment mainFrag = mainActivity.getCurrentMainFragment();
        if (mainFrag != null) {
            mainFrag.loadlist(pendingPath, false, OpenMode.UNKNOWN);
        } else {
            mainActivity.goToMain(pendingPath);
            return;
        }
        // pendingPath = null;

        mainActivity.supportInvalidateOptionsMenu();
    }

    private static class CategoryItem{
        @DrawableRes
        int drawable;
        String description;
        private String pendingPath;

        public CategoryItem() {
        }

        public CategoryItem(int drawable, String description, String pendingPath) {
            this.drawable = drawable;
            this.description = description;
            this.pendingPath = pendingPath;
        }

        public String getPendingPath() {
            return pendingPath;
        }

        public void setPendingPath(String pendingPath) {
            this.pendingPath = pendingPath;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(@DrawableRes int drawable) {
            this.drawable = drawable;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
