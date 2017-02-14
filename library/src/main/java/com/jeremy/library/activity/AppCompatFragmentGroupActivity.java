package com.jeremy.library.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * Created by benbennest on 17/2/14.
 */

public abstract class AppCompatFragmentGroupActivity extends LifecycleDispatchAppCompatActivity {
    protected static final int INVALID_FRAGMENT_ID = -1;
    private static final String SAVE_INSTANCE_STATE_PRIMARY_FRAGMENT_TAG = "primary_fragment_tag";
    private static final String SAVE_INSTANCE_STATE_SECONDARY_FRAGMENT_TAG = "secondary_fragment_tag";
    private String mCurrentPrimaryFragmentTag;
    protected Fragment mCurrentPrimaryFragment;
    private String mCurrentSecondaryFragmentTag;
    protected Fragment mCurrentSecondaryFragment;
    private int mCurrentPrimaryFragmentId = -1;
    private int mCurrentSecondaryFragmentId = -1;
    protected FragmentManager mFragmentManager;

    public AppCompatFragmentGroupActivity() {
    }

    protected abstract void initPrimaryFragment();

    protected abstract Class<? extends Fragment> getPrimaryFragmentClass(int var1);

    protected abstract Bundle getPrimaryFragmentArguments(int var1);

    protected abstract int getPrimaryFragmentStubId(int var1);

    protected void onCreate(Bundle savedInstanceState) {
        this.mFragmentManager = this.getSupportFragmentManager();
        super.onCreate(savedInstanceState);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.mCurrentPrimaryFragmentTag = savedInstanceState.getString("primary_fragment_tag");
            if(!TextUtils.isEmpty(this.mCurrentPrimaryFragmentTag)) {
                this.mCurrentPrimaryFragment = this.mFragmentManager.findFragmentByTag(this.mCurrentPrimaryFragmentTag);
            }

            this.mCurrentSecondaryFragmentTag = savedInstanceState.getString("secondary_fragment_tag");
            if(!TextUtils.isEmpty(this.mCurrentSecondaryFragmentTag)) {
                this.mCurrentSecondaryFragment = this.mFragmentManager.findFragmentByTag(this.mCurrentSecondaryFragmentTag);
            }
        }

        if(this.mCurrentPrimaryFragment == null) {
            this.initPrimaryFragment();
        }

        if(this.mCurrentSecondaryFragment == null) {
            this.initSecondaryFragment();
        }

        super.onPostCreate(savedInstanceState);
    }

    protected FragmentTransaction beginPrimaryFragmentTransaction(int enterFragmentId, int exitFragmentId) {
        FragmentTransaction ft = this.mFragmentManager.beginTransaction();
        return ft;
    }

    public void switchPrimaryFragment(int fragmentId) {
        Class clz = this.getPrimaryFragmentClass(fragmentId);
        this.mCurrentPrimaryFragmentTag = clz.getName();
        Fragment fragment = this.mFragmentManager.findFragmentByTag(this.mCurrentPrimaryFragmentTag);
        FragmentTransaction ft = this.beginPrimaryFragmentTransaction(fragmentId, this.mCurrentPrimaryFragmentId);
        this.mCurrentPrimaryFragmentId = fragmentId;
        if(this.mCurrentPrimaryFragment != null) {
            ft.detach(this.mCurrentPrimaryFragment);
            if(fragment == this.mCurrentPrimaryFragment) {
                fragment = null;
            }
        }

        Bundle args = this.getPrimaryFragmentArguments(fragmentId);
        if(fragment == null) {
            fragment = Fragment.instantiate(this, clz.getName());
            fragment.setArguments(args);
            ft.replace(this.getPrimaryFragmentStubId(fragmentId), fragment, this.mCurrentPrimaryFragmentTag);
        } else {
            Bundle existedArgs = fragment.getArguments();
            if(existedArgs != null) {
                existedArgs.putAll(args);
            }

            ft.attach(fragment);
        }

        this.mCurrentPrimaryFragment = fragment;
        ft.commitAllowingStateLoss();
    }

    protected FragmentTransaction beginSecondaryFragmentTransaction(int enterFragmentId, int exitFragmentId) {
        FragmentTransaction ft = this.mFragmentManager.beginTransaction();
        return ft;
    }

    public void switchSecondaryFragment(int fragmentId) {
        Class clz = this.getSecondaryFragmentClass(fragmentId);
        if(clz != null) {
            this.mCurrentSecondaryFragmentTag = clz.getName();
            Fragment fragment = this.mFragmentManager.findFragmentByTag(this.mCurrentSecondaryFragmentTag);
            FragmentTransaction ft = this.beginSecondaryFragmentTransaction(fragmentId, this.mCurrentSecondaryFragmentId);
            this.mCurrentSecondaryFragmentId = fragmentId;
            if(this.mCurrentSecondaryFragment != null) {
                ft.detach(this.mCurrentSecondaryFragment);
                if(fragment == this.mCurrentSecondaryFragment) {
                    fragment = null;
                }
            }

            Bundle args = this.getSecondaryFragmentArguments(fragmentId);
            if(fragment == null) {
                fragment = Fragment.instantiate(this, clz.getName());
                fragment.setArguments(args);
                ft.replace(this.getSecondaryFragmentStubId(fragmentId), fragment, this.mCurrentSecondaryFragmentTag);
            } else {
                Bundle existedArgs = fragment.getArguments();
                if(existedArgs != null) {
                    existedArgs.putAll(args);
                }

                ft.attach(fragment);
            }

            this.mCurrentSecondaryFragment = fragment;
            ft.commitAllowingStateLoss();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("primary_fragment_tag", this.mCurrentPrimaryFragmentTag);
        outState.putString("secondary_fragment_tag", this.mCurrentSecondaryFragmentTag);
        super.onSaveInstanceState(outState);
    }

    protected void onDestroy() {
        if(this.mCurrentPrimaryFragment != null && this.mCurrentPrimaryFragment.isAdded()) {
            this.mFragmentManager.beginTransaction().remove(this.mCurrentPrimaryFragment).commitAllowingStateLoss();
        }

        this.mCurrentPrimaryFragment = null;
        if(this.mCurrentSecondaryFragment != null && this.mCurrentSecondaryFragment.isAdded()) {
            this.mFragmentManager.beginTransaction().remove(this.mCurrentSecondaryFragment).commitAllowingStateLoss();
        }

        this.mCurrentSecondaryFragment = null;
        super.onDestroy();
    }

    protected void initSecondaryFragment() {
    }

    protected Class<? extends Fragment> getSecondaryFragmentClass(int fragmentId) {
        return null;
    }

    protected Bundle getSecondaryFragmentArguments(int fragmentId) {
        return null;
    }

    protected int getSecondaryFragmentStubId(int fragmentId) {
        return 0;
    }
}
