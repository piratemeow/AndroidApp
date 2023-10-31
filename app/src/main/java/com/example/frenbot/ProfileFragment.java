package com.example.frenbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.profile_fragment,container,false);
        FloatingActionButton edit=root.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the FinanceActivity when financeCard is clicked
                Intent intent = new Intent(getActivity(), Edit_Profile.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
