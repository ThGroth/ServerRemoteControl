package com.groth.android.videotoserver.settings.serveradd;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class AddServerWizardAdapter extends FragmentPagerAdapter {
    public List<String> fragmentsClassNames;
    public Map<String, AbstractAddServerWizardStepFragment> fragmentClasses = new HashMap<>();
    private FragmentManager fragmentManager;
    private Context context;

    public AddServerWizardAdapter(FragmentManager fm, List<String> fragmentsClassNames, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentManager = fm;
        this.fragmentsClassNames = fragmentsClassNames;
        this.context = context;
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
            fragmentClasses.put(className, fragment);

        }
        fragment.setContext(context);
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

}
