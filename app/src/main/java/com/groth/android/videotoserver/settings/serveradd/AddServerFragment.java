package com.groth.android.videotoserver.settings.serveradd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.settings.AbstractConnectionConfigChangeFragment;

import java.util.ArrayList;
import java.util.List;

public class AddServerFragment extends AbstractConnectionConfigChangeFragment {

    private AddServerWizardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_server_pager, container, false);

        ViewPager pager = view.findViewById(R.id.addServerPager);
        List<String> pagerSteps = new ArrayList<>();
        pagerSteps.add(AddServerWizardStepGeneral.class.getName());
        pagerSteps.add(AddServerWizardStepCredentials.class.getName());
        pagerSteps.add(AddServerWizardStepPrivateKey.class.getName());
        pagerSteps.add(AddServerWizardStepTest.class.getName());

        adapter = new AddServerWizardAdapter(getChildFragmentManager(), pagerSteps, getContext());


        pager.setAdapter(adapter);


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);
        return view;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        if (childFragment instanceof AbstractConnectionConfigChangeFragment) {
            ((AbstractConnectionConfigChangeFragment) childFragment)
                    .setOnConnectionChangeListener(getOnConnectionChangeListener());
        }
        super.onAttachFragment(childFragment);

    }
}
