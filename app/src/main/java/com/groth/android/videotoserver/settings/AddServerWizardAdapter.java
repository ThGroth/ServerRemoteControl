package com.groth.android.videotoserver.settings;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.groth.android.videotoserver.ConnectionConfig;
import com.groth.android.videotoserver.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class AddServerWizardAdapter extends FragmentPagerAdapter {
    private final Context context;
    public List<String> fragmentsClassNames;
    public Map<String, AbstractAddServerWizardStepFragment> fragmentClasses = new HashMap<>();
    private FragmentManager fragmentManager;
    private final ConnectionConfig newConnectionConfig;

    public AddServerWizardAdapter(FragmentManager fm, List<String> fragmentsClassNames, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentManager = fm;
        this.fragmentsClassNames = fragmentsClassNames;
        this.context = context;
        newConnectionConfig = new ConnectionConfig();
        newConnectionConfig.setServer(new Server());
    }

    private AbstractAddServerWizardStepFragment loadFragmentForClassName(String className) {
        AbstractAddServerWizardStepFragment fragment = null;
        if (fragmentClasses.containsKey(className)) {
            fragment = fragmentClasses.get(className);
        }
        // modified else
        if (fragment == null || fragment.getView()==null)
        {
            fragment = (AbstractAddServerWizardStepFragment) fragmentManager.getFragmentFactory().
                    instantiate(ClassLoader.getSystemClassLoader(), className);
            fragment.setContext( context );
            fragment.setNewConnectionConfig( newConnectionConfig );
            fragmentClasses.put(className, fragment);
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        position = position % getCount();
        return loadFragmentForClassName(fragmentsClassNames.get(position));
    }

    @Override
    public int getCount() {
        return fragmentsClassNames.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position = position % getCount();
        return loadFragmentForClassName(fragmentsClassNames.get(position)).getPageTitle();
    }

    public ConnectionConfig getNewConnectionConfig() {
        return newConnectionConfig;
    }
}
