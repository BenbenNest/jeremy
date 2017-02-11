package com.jeremy.lychee.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHandler {


    private class FragmentInfo {
        public Class<? extends Fragment> fragmentClass;
        public int idContainer;

        public FragmentInfo(Class<? extends Fragment> fragmentClass, int idContainer) {
            this.fragmentClass = fragmentClass;
            this.idContainer = idContainer;
        }
    }

    private static class NilFragmentHandler extends FragmentHandler {
        public NilFragmentHandler() {
        }

        @Override
        public void addFragment(Class<? extends Fragment> fragmentClass) {
            return;
        }

        @Override
        public Fragment getCurrentFragment() {
            return null;
        }

        @Override
        public <T extends Fragment> T getFragment(Class<T> fragmentClass) {
            return null;
        }

        @Override
        public boolean registerFragment(Class<? extends Fragment> fragmentClass, int idContainer) {
            return false;
        }

        @Override
        public boolean switchToFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack) {
            return false;
        }

        @Override
        public boolean switchToFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack, int enterAnimation, int exitAnimation, int popEnterAnimation, int popExitAnimation) {
            return false;
        }

        @Override
        public boolean unregisterFragment(Class<? extends Fragment> fragmentClass) {
            return false;
        }
    }

    public static final NilFragmentHandler NULL_HANDLER = new NilFragmentHandler();

    private Map<String, FragmentInfo> mapFragmentInfo = new HashMap<String, FragmentInfo>();
    private FragmentManager fragmentManager;

    public FragmentHandler() {
    }

    public FragmentHandler(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public boolean registerFragment(Class<? extends Fragment> fragmentClass, int idContainer) {
        if (fragmentClass == null)
            return false;

        if (mapFragmentInfo.containsKey(fragmentClass.getName()))
            return false;

        mapFragmentInfo.put(fragmentClass.getName(), new FragmentInfo(fragmentClass, idContainer));
        return true;
    }

    public boolean unregisterFragment(Class<? extends Fragment> fragmentClass) {
        if (fragmentClass == null)
            return false;

        if (!mapFragmentInfo.containsKey(fragmentClass.getName()))
            return false;

        mapFragmentInfo.remove(fragmentClass.getName());
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean switchToFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack) {
        return switchToFragment(fragmentClass, addToBackStack, -1, -1, -1, -1);
    }

    // Fragment切换时不销毁，提高切换Fragment的效率；
    // 因此需要注意切换Fragment时onResume、onPause、onDestroyView、onCreateView等消息不一定会触发
    @SuppressWarnings("unchecked")
    public boolean switchToFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack, int enterAnimation, int exitAnimation, int popEnterAnimation, int popExitAnimation) {
        if (fragmentClass == null) {
            return false;
        }

        if (!mapFragmentInfo.containsKey(fragmentClass.getName())) {
            return false;
        }

        FragmentInfo info = mapFragmentInfo.get(fragmentClass.getName());
        if (info == null || info.fragmentClass == null) {
            return false;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        if (fragment == null) {
            try {
                fragment = info.fragmentClass.getConstructor().newInstance();
                fragmentManager.beginTransaction().add(info.idContainer, fragment, fragmentClass.getName()).commit();
            } catch (Exception e) {
                return false;
            }
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (enterAnimation != -1 && exitAnimation != -1) {
            if (popEnterAnimation == -1 || popExitAnimation == -1) {


                ft.setCustomAnimations(enterAnimation, exitAnimation);
            } else {
                ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation);
            }
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment iter : fragments) {
                if (iter == null || iter == fragment || iter.isHidden())
                    continue;
                ft.hide(iter);
            }
        }

        ft.show(fragment);
        if (addToBackStack)
            ft.addToBackStack(null);

        ft.commitAllowingStateLoss();
        return true;
    }

    // 需要注意的是，getCurrentFragment和isHomeFragment都基于一个假设，即同一时刻，只有一个顶级Fragment处于显示状态，对于目前的App这个假设是合理的，
    // 但是后续如果有横屏或其他的需求导致的改动，则不应仍有以上假设
    public Fragment getCurrentFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment iter : fragments) {
                if (iter == null || iter.isHidden())
                    continue;

                return iter;
            }
        }
        return null;
    }

    public void addFragment(Class<? extends Fragment> fragmentClass) {
        if (fragmentClass == null)
            return;

        if (!mapFragmentInfo.containsKey(fragmentClass.getName())) {
            return;
        }

        FragmentInfo info = mapFragmentInfo.get(fragmentClass.getName());
        if (info == null || info.fragmentClass == null) {
            return;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        if (fragment == null) {
            try {
                fragment = info.fragmentClass.getConstructor().newInstance();
                fragmentManager.beginTransaction().add(info.idContainer, fragment, fragmentClass.getName()).hide(fragment).commitAllowingStateLoss();
            } catch (Exception e) {
            }
        }
    }

    public <T extends Fragment> T getFragment(Class<T> fragmentClass) {
        if (!mapFragmentInfo.containsKey(fragmentClass.getName())) {
            return null;
        }
        return fragmentClass.cast(fragmentManager.findFragmentByTag(fragmentClass.getName()));
    }
}
